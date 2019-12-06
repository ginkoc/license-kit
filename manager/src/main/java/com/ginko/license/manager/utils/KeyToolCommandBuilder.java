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
     * 设置秘钥别名
     * @param alias key's alias in keystore
     * @return this
     */
    public KeyToolCommandBuilder alias(String alias) {
        arguments.put(ALIAS, alias);
        return this;
    }

    public KeyToolCommandBuilder sigalg(String sigalg) {
        arguments.put(SIGALG, sigalg);
        return this;
    }

    public KeyToolCommandBuilder keyalg(String keyalg) {
        arguments.put(KEYALG, keyalg);
        return this;
    }

    public KeyToolCommandBuilder keysize(String keysize) {
        arguments.put(KEYSIZE, keysize);
        return this;
    }

    //设置秘钥有效天数
    public KeyToolCommandBuilder validity(long days) {
        String validity = String.valueOf(days);
        arguments.put(VALIDITY, validity);
        return this;
    }

    //设置秘钥密码
    public KeyToolCommandBuilder keypass(String keypass) {
        arguments.put(KEYPASS, keypass);
        return this;
    }

    //设置公钥/私钥库
    public KeyToolCommandBuilder keystore(String keystore) {
        arguments.put(KEYSTORE, keystore);
        return this;
    }

    public KeyToolCommandBuilder storetype(String storetype) {
        arguments.put(STORE_TYPE, storetype);
        return this;
    }

    //设置公钥/私钥库密码
    public KeyToolCommandBuilder storepass(String storepass) {
        arguments.put(STOREPASS, storepass);
        return this;
    }

    //设置dname
    public KeyToolCommandBuilder dname(String dname) {
        arguments.put(DNAME, dname);
        return this;
    }

    //设置文件选项
    public KeyToolCommandBuilder file(String file) {
        arguments.put(FILE, file);
        return this;
    }

    //设置执行命令时不进行询问(非阻塞)
    public KeyToolCommandBuilder noprompt() {
        arguments.put(NOPROMPT, null);
        return this;
    }

    //设置新密码
    public KeyToolCommandBuilder newPass(String newPass) {
        arguments.put(NEW, newPass);
        return this;
    }

    //设置默认的加密算法
    public KeyToolCommandBuilder defaultKeyalg() {
        return keyalg(DEFAULT_KEYALG);
    }

    //设置默认的秘钥大小
    public KeyToolCommandBuilder defaultKeysize() {
        return keysize(DEFAULT_KEYSIZE);
    }

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
