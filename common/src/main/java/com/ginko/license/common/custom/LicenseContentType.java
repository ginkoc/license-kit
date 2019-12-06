package com.ginko.license.common.custom;

/**
 * @author ginko
 * @date 7/20/19
 */
public enum LicenseContentType {

    /**mac*/
    MAC("^([0-9a-fA-F]{2})(([/\\s:][0-9a-fA-F]{2}){5})$"),

    /**ip*/
    IP("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$")
    ;

    /**content key's regexp*/
    private final String regexp;

    LicenseContentType(String regexp) {
        this.regexp = regexp;
    }

    public String getRegexp() {
        return regexp;
    }
}
