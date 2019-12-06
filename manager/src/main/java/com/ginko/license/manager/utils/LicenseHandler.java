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
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ginko
 * @date 8/3/19
 */
public class LicenseHandler {

    private static final Logger log = LoggerFactory.getLogger(LicenseHandler.class);

    public static String createLicense(LicenseDto dto) throws CommandException {
        final String ticket = UUID.randomUUID().toString();

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

        //设置ticket在redis缓存中的TTL以及缓存失效后多久开始执行定时任务
        cacheTicketAndSetTask(ticket, licFile, 300, 60);
        return ticket;
    }

    public static File zipLicense(String ticket) throws CommandException, IOException {
        //checking ticket expire through redis
        String file = (String) RedisUtil.get(ticket);
        if (file == null) {
            throw new UnifiedException(-1, "ticket has expired");
        }

        if (!new File(file).exists()) {
            throw new FileNotFoundException(String.format("File %1$s doesn't exist.", file));
        }

        String cert = CommonUtils.getUniqueCertName("cert");
        String publicKeystore = CommonUtils.getUniquePubKeystoreName("publicKeystore");

        AbstractKeyToolCommand exportCertCommand = new KeyToolCommandBuilder(KeyToolCommandType.EXPORT_CERT)
                .alias(ticket)
                .file(cert)
                .keystore(Constants.STORE_PATH)
                .storepass(Constants.STORE_PASS)
                .build();
        exportCertCommand.execute();

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
        content.setHolder(new X500Principal(dto.getHolder()));
        content.setSubject(dto.getSubject());
        content.setConsumerType(Constants.LICENSE_CONSUMER_TYPE);
        content.setConsumerAmount(Constants.LICENSE_CONSUMER_AMOUNT);
        String issuer = dto.getIssuer() == null ? Constants.DEFAULT_ISSUER : dto.getIssuer();
        content.setIssuer(new X500Principal(issuer));
        Date date = new Date();
        content.setIssued(date);
        content.setNotBefore(date);
        content.setNotAfter(dto.getNotAfter());

        if (dto.getContents() != null && !dto.getContents().isEmpty()) {
            dto.getContents().forEach(cwv -> {
                LicenseContentType contentType = LicenseContentType.valueOf(cwv.getType());
                content.setContentValue(contentType, cwv.getValue());
            });
        }
        parameters.setLicenseContent(content);
        return parameters;
    }

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
