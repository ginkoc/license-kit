package com.ginko.license.manager.utils;

import com.ginko.license.common.custom.CustomLicenseContent;
import com.ginko.license.common.custom.LicenseContentType;
import com.ginko.license.manager.api.restentity.LicenseDto;
import com.ginko.license.manager.command.AbstractKeyToolCommand;
import com.ginko.license.manager.command.CreateLicenseCommand;
import com.ginko.license.manager.command.KeyToolCommandType;
import com.ginko.license.manager.contants.Constants;
import com.ginko.license.manager.exception.CommandException;
import com.ginko.license.manager.exception.UnifiedException;
import com.ginko.license.manager.parameters.CreateLicenseParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ginko
 * @date 8/3/19
 */
public class LicenseHandler {

    private static final Logger log = LoggerFactory.getLogger(LicenseHandler.class);

    /**
     * 生成license文件和获得下载license的ticket，ticket有效时间为300s（5分钟）
     * 当ticket失效1分钟以后定时任务会将license文件删除
     * @param dto 生成license需要的参数对象
     * @return 用于下载license的ticket
     * @throws CommandException 执行生成license命令时出错抛出的异常
     */
    public static String createLicense(LicenseDto dto) throws CommandException {
        final String ticket = UUID.randomUUID().toString();

        // 封装keytool命令和参数信息，用于生成密钥对，密钥对在生成license时需要用到
        // 实际执行的命令类似于：
        // keytool -genkeypair -alias ginko -sigalg SHA1withDSA -keypass abcd1234 -keysize 1024 \
        // -storepass abcd1234 -keyalg DSA -dname cn=commonName,ou=license,o=ginko,c=CN -validity 156 \
        // -keystore /home/ginko/.license_manager/ginko.pks -storetype PKCS12
        AbstractKeyToolCommand genKeyCommand = new KeyToolCommandBuilder(KeyToolCommandType.GENERATE_KEYPAIR)
                .alias(ticket)
                .defaultKeyalg()
                .defaultKeysize()
                .defaultSigalg()
                .validity(CommonUtils.daysBetween(dto.getNotBefore(), dto.getNotAfter()))
                .keypass(Constants.KEY_PASS)
                .keystore(Constants.STORE_PATH)
                .storetype("PKCS12")
                .storepass(Constants.STORE_PASS)
                .dname("cn=" + ticket + "," + Constants.DNAME_SUFFIX)
                .build();
        genKeyCommand.execute();

        final String licFile = CommonUtils.getUniqueLicenseName(dto.getSubject());
        CreateLicenseCommand createLicenseCommand =
                new CreateLicenseCommand(genKeyCommand, buildCreateLicenseParams(ticket, dto, licFile));
        createLicenseCommand.execute();

        // 设置ticket在redis缓存中的TTL以及缓存失效后多久开始执行定时任务
        cacheTicketAndSetTask(ticket, licFile, 300, 60);
        return ticket;
    }

    /**
     * 通过前台传过来的ticket，找到指定的license提供下载
     * @param ticket 创建license后返回的用于下载license的令牌
     * @return 文件路径
     * @throws CommandException 执行keytool命令失败时抛出的异常
     * @throws IOException 文件操作过程中可能出现的异常
     */
    public static File zipLicense(String ticket) throws CommandException, IOException {
        //checking ticket expire through redis
        String file = (String) RedisUtil.get(ticket);
        if (file == null) {
            throw new UnifiedException(-1, "ticket doesn't exist or expired");
        }

        if (!new File(file).exists()) {
            throw new FileNotFoundException(String.format("File %1$s doesn't exist.", file));
        }

        String cert = CommonUtils.getUniqueCertName("cert");
        String publicKeystore = CommonUtils.getUniquePubKeystoreName("publicKeystore");

        // 这是一个中间过程，功能是将私钥库中的秘钥对所含有的证书导出到指定文件，为下一步导出公钥做准备
        // 实际执行的命令类似于：
        // keytool -exportcert -alias ginko -file /home/ginko/.license_manager/cert.cert
        // -storepass abcd1234 -keystore /home/ginko/.license_manager/ginko.pks
        AbstractKeyToolCommand exportCertCommand = new KeyToolCommandBuilder(KeyToolCommandType.EXPORT_CERT)
                .alias(ticket)
                .file(cert)
                .keystore(Constants.STORE_PATH)
                .storepass(Constants.STORE_PASS)
                .build();
        exportCertCommand.execute();

        // 将证书文件中的公钥导出到公钥库，license和公钥将一起交给用户，公钥用于解密license信息
        // 实际执行的命令类似于：
        // keytool -exportcert -alias ginko -file /home/ginko/.license_manager/cert.cert
        // -storepass abcd1234 -keystore /home/ginko/.license_manager/ginko.pks
        AbstractKeyToolCommand importCertCommand = new KeyToolCommandBuilder(KeyToolCommandType.IMPORT_CERT)
                .alias(ticket)
                .file(cert)
                //use a random uuid as keypass
                .keypass(UUID.randomUUID().toString())
                .keystore(publicKeystore)
                .storepass(ticket)
                .noprompt()
                .build();
        importCertCommand.setParentCommand(exportCertCommand);
        importCertCommand.execute();

        File zipFile;
        try {
            String zipFileName = CommonUtils.getUniqueZipName("license");
            // 将公钥库和license打包
            zipFile = CommonUtils.zipFiles(zipFileName, file, publicKeystore);
        } finally {
            CommonUtils.deleteFileIfExist(cert);
            CommonUtils.deleteFileIfExist(publicKeystore);
        }

        return zipFile;
    }

    private static CreateLicenseParameters buildCreateLicenseParams(String cipher, LicenseDto dto, String outputFile) {
        CreateLicenseParameters parameters =
                new CreateLicenseParameters(cipher, dto.getSubject(), outputFile, false);

        final CustomLicenseContent content = new CustomLicenseContent();
        // 代表证书的持有者
        content.setHolder(new X500Principal(dto.getHolder()));
        // 代表证书的主题，可以是一款产品的名字或者软件的代号
        content.setSubject(dto.getSubject());
        // 目前不清楚代表什么，字面意思为消费类型
        content.setConsumerType(Constants.LICENSE_CONSUMER_TYPE);
        // 目前不清楚代表什么，字面意思为消费数量
        content.setConsumerAmount(Constants.LICENSE_CONSUMER_AMOUNT);
        String issuer = dto.getIssuer() == null ? Constants.DEFAULT_ISSUER : dto.getIssuer();
        // 代表证书发行者
        content.setIssuer(new X500Principal(issuer));
        Date date = new Date();
        // 证书发行日期
        content.setIssued(date);
        // 证书最早能使用的时间
        content.setEffectiveDate(dto.getNotBefore());
        // 证书过期时间
        content.setNotAfter(dto.getNotAfter());

        // 设置额外的控制参数
        if (dto.getContents() != null && !dto.getContents().isEmpty()) {
            final Map<LicenseContentType, String> valMap = new HashMap<>();
            dto.getContents().forEach(cwv -> {
                LicenseContentType contentType = LicenseContentType.valueOf(cwv.getType());
                valMap.put(contentType, cwv.getValue());
            });
            content.setContentValueMap(valMap);
        }
        parameters.setLicenseContent(content);
        return parameters;
    }

    /**
     * 将ticket存入redis缓存中，并且在ticket过期后删除license文件
     * @param ticket 要缓存的票据
     * @param licFile license文件路径
     * @param ticketTTL ticket的有效时间
     * @param delayTime 在ticket过期后延迟删除license的时间
     */
    private static void cacheTicketAndSetTask(final String ticket,
                                              final String licFile,
                                              long ticketTTL,
                                              long delayTime) {
        RedisUtil.set(ticket, licFile, ticketTTL);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                log.info("Deleting license file '{}' when ticket expired", licFile);
                CommonUtils.deleteFileIfExist(licFile);

                try {
                    log.info("Deleting private key '{}' on keystore '{}' when ticket expired.",
                            ticket, Constants.STORE_PATH);

                    // 封装keytool命令和参数信息，用于删除秘钥库中的密钥对
                    // 实际执行的命令类似于：
                    // keytool -delete -alias ginko -storepass abcd1234 -keystore /home/ginko/.license_manager/ginko.pks
                    AbstractKeyToolCommand deleteCommand = new KeyToolCommandBuilder(KeyToolCommandType.DELETE_KEY)
                            .alias(ticket)
                            .keystore(Constants.STORE_PATH)
                            .storepass(Constants.STORE_PASS)
                            .build();
                    deleteCommand.execute();
                } catch (CommandException e) {
                    log.error("Delete private key '{}' failed when ticket expired.", ticket);
                }
            }
        };

        ScheduledTaskManager.getInstance().execute(task, ticketTTL + delayTime, TimeUnit.SECONDS);
    }
}
