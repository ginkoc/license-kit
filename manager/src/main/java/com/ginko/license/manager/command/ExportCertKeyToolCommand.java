package com.ginko.license.manager.command;

import com.ginko.license.manager.utils.CommonUtils;
import com.ginko.license.manager.utils.KeyToolCommandBuilder;

import java.io.File;
import java.util.Map;

/**
 * @author ginko
 * @date 7/21/19
 */
public class ExportCertKeyToolCommand extends AbstractKeyToolCommand {

    private static final String OPTION = "-exportcert";

    public ExportCertKeyToolCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public ExportCertKeyToolCommand(Map<String, String> arguments, Executable parentCommand) {
        super(arguments, parentCommand);
    }

    @Override
    protected String getOption() {
        return OPTION;
    }

    @Override
    protected boolean executeRollback() {
        String certFileName = getArguments().get(KeyToolCommandBuilder.FILE);
        if (certFileName != null) {
            CommonUtils.deleteFileIfExist(certFileName);
        }
        return true;
    }
}
