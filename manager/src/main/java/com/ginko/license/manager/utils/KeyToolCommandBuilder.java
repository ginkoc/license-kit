package com.ginko.license.manager.utils;

import com.ginko.license.manager.command.AbstractKeyToolCommand;
import com.ginko.license.manager.command.DeleteKeyKeyToolCommand;
import com.ginko.license.manager.command.ExportCertKeyToolCommand;
import com.ginko.license.manager.command.GenerateKeypairKeyToolCommand;
import com.ginko.license.manager.command.ImportCertKeyToolCommand;
import com.ginko.license.manager.command.KeyToolCommandType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ginko
 * @date 7/21/19
 */
public class KeyToolCommandBuilder {

    public static final String ALIAS = "-alias";
    public static final String KEYALG = "-keyalg";
    public static final String KEYSIZE = "-keysize";
    public static final String VALIDITY = "-validity";
    public static final String KEYPASS = "-keypass";
    public static final String KEYSTORE = "-keystore";
    public static final String STOREPASS = "-storepass";
    public static final String DNAME = "-dname";
    public static final String FILE = "-file";
    public static final String NOPROMPT = "-noprompt";
    public static final String NEW = "-new";
    public static final String SIGALG = "-sigalg";
    public static final String STORE_TYPE = "-storetype";

    private static final String DEFAULT_KEYALG = "DSA";
    private static final String DEFAULT_KEYSIZE = "1024";
    private static final String DEFAULT_SIGALG = "SHA1withDSA";

    private final KeyToolCommandType keyToolCommandType;
    private final Map<String, String> arguments;

    public KeyToolCommandBuilder(KeyToolCommandType keyToolCommandType) {
        this.keyToolCommandType = keyToolCommandType;
        this.arguments = new HashMap<>();
    }

    /**
     * 指定keytool命令的秘钥别名参数
     * @param alias 秘钥在秘钥库中的别名
     * @return this
     */
    public KeyToolCommandBuilder alias(String alias) {
        arguments.put(ALIAS, alias);
        return this;
    }

    /**
     * 指定keytool命令的签名算法参数
     * @param sigalg 签名算法
     * @return this
     */
    public KeyToolCommandBuilder sigalg(String sigalg) {
        arguments.put(SIGALG, sigalg);
        return this;
    }

    /**
     * 指定keytool命令的加密算法参数
     * @param keyalg 加密算法
     * @return this
     */
    public KeyToolCommandBuilder keyalg(String keyalg) {
        arguments.put(KEYALG, keyalg);
        return this;
    }

    /**
     * 指定keytool命令的秘钥大小参数
     * @param keysize 秘钥大小
     * @return this
     */
    public KeyToolCommandBuilder keysize(String keysize) {
        arguments.put(KEYSIZE, keysize);
        return this;
    }

    /**
     * 指定keytool命令的秘钥有效天数参数
     * @param days 秘钥有效天数
     * @return this
     */
    public KeyToolCommandBuilder validity(long days) {
        String validity = String.valueOf(days);
        arguments.put(VALIDITY, validity);
        return this;
    }

    /**
     * 指定keytool命令的秘钥密码参数
     * @param keypass 秘钥密码
     * @return this
     */
    public KeyToolCommandBuilder keypass(String keypass) {
        arguments.put(KEYPASS, keypass);
        return this;
    }

    /**
     * 指定keytool命令的秘钥库参数
     * @param keystore 秘钥库
     * @return this
     */
    public KeyToolCommandBuilder keystore(String keystore) {
        arguments.put(KEYSTORE, keystore);
        return this;
    }

    /**
     * 指定keytool命令的秘钥库类型参数
     * @param storetype 秘钥库类型
     * @return this
     */
    public KeyToolCommandBuilder storetype(String storetype) {
        arguments.put(STORE_TYPE, storetype);
        return this;
    }

    /**
     * 指定keytool命令的秘钥库密码参数
     * @param storepass 秘钥库密码
     * @return this
     */
    public KeyToolCommandBuilder storepass(String storepass) {
        arguments.put(STOREPASS, storepass);
        return this;
    }

    /**
     * 指定keytool命令的dname参数
     * @param dname dname
     * @return this
     */
    public KeyToolCommandBuilder dname(String dname) {
        arguments.put(DNAME, dname);
        return this;
    }

    /**
     * 指定keytool命令的文件名参数
     * @param file 文件名
     * @return this
     */
    public KeyToolCommandBuilder file(String file) {
        arguments.put(FILE, file);
        return this;
    }

    /**
     * 指定执行keytool命令时不进行提示
     * @return this
     */
    public KeyToolCommandBuilder noprompt() {
        arguments.put(NOPROMPT, null);
        return this;
    }

    /**
     * 指定keytool命令的新密码参数
     * @param newPass 新密码
     * @return this
     */
    public KeyToolCommandBuilder newPass(String newPass) {
        arguments.put(NEW, newPass);
        return this;
    }

    /**
     * 指定执行keytool命令时使用默认的加密算法
     * @return this
     */
    public KeyToolCommandBuilder defaultKeyalg() {
        return keyalg(DEFAULT_KEYALG);
    }

    /**
     * 指定执行keytool命令时使用默认秘钥大小
     * @return this
     */
    public KeyToolCommandBuilder defaultKeysize() {
        return keysize(DEFAULT_KEYSIZE);
    }

    /**
     * 指定执行keytool命令时使用默认的签名算法
     * @return this
     */
    public KeyToolCommandBuilder defaultSigalg() {
        return sigalg(DEFAULT_SIGALG);
    }

    public AbstractKeyToolCommand build() {
        switch (this.keyToolCommandType) {
            case GENERATE_KEYPAIR:
                return new GenerateKeypairKeyToolCommand(arguments);
            case EXPORT_CERT:
                return new ExportCertKeyToolCommand(arguments);
            case IMPORT_CERT:
                return new ImportCertKeyToolCommand(arguments);
            case DELETE_KEY:
                return new DeleteKeyKeyToolCommand(arguments);
            default:
                throw new IllegalArgumentException("Unsupported command Type");
        }
    }
}
