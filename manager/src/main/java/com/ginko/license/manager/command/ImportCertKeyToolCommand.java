package com.ginko.license.manager.command;

import com.ginko.license.manager.utils.CommonUtils;
import com.ginko.license.manager.utils.KeyToolCommandBuilder;

import java.io.File;
import java.util.Map;

/**
 * @author ginko
 * @date 7/21/19
 */
public class ImportCertKeyToolCommand extends AbstractKeyToolCommand {

    private static final String OPTION = "-importcert";

    public ImportCertKeyToolCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public ImportCertKeyToolCommand(Map<String, String> arguments, Executable parentCommand) {
        super(arguments, parentCommand);
    }

    @Override
    protected String getOption() {
        return OPTION;
    }

    @Override
    protected boolean executeRollback() {
        String storeFileName = getArguments().get(KeyToolCommandBuilder.FILE);
        if (storeFileName != null) {
            CommonUtils.deleteFileIfExist(storeFileName);
        }
        return true;
    }
}
