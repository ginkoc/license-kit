package com.ginko.license.manager.command;

/**
 * @author ginko
 * @date 7/21/19
 */
public enum KeyToolCommandType {
    /**生成秘钥对命令选项*/
    GENERATE_KEYPAIR,

    /**导出证书命令选项*/
    EXPORT_CERT,

    /**导入证书命令选项*/
    IMPORT_CERT,

    /**删除秘钥命令选项*/
    DELETE_KEY
}
