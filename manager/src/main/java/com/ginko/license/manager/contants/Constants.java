package com.ginko.license.manager.contants;

import java.io.File;

/**
 * @author ginko
 * @date 8/7/19
 */
public class Constants {

    public static final String SEPARATOR = File.separator;

    public static final String USER_HOME = System.getProperty("user.home");

    public static final String ROOT = ".license_manager";

    public static final String LICENSE_ROOT = USER_HOME + SEPARATOR + ROOT;

    public static final String LICENSE_SUFFIX = ".lic";

    public static final String CERT_SUFFIX = ".cert";

    public static final String STORE_NAME = "ginko";

    public static final String STORE_SUFFIX = ".pks";

    public static final String ZIP_SUFFIX = ".zip";

    public static final String STORE_PATH = LICENSE_ROOT + SEPARATOR + STORE_NAME + STORE_SUFFIX;

    public static final String STORE_PASS = "abcd1234";

    public static final String KEY_PASS = "abcd1234";

    public static final String DNAME_SUFFIX = "ou=license,o=ginko,c=CN";

    public static final String DEFAULT_ISSUER = "CN=license.ginko.com";

    public static final String LICENSE_CONSUMER_TYPE = "User";

    public static final int LICENSE_CONSUMER_AMOUNT = 1;

    public static final String DEFAULT_HOLDER = "CN=user";
}
