package com.ginko.license.manager.command;

import java.util.Map;

/**
 * @author ginko
 * @date 7/21/19
 */
public class DeleteKeyKeyToolCommand extends AbstractKeyToolCommand {

    private static final String OPTION = "-delete";

    public DeleteKeyKeyToolCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public DeleteKeyKeyToolCommand(Map<String, String> arguments, Executable parentCommand) {
        super(arguments, parentCommand);
    }

    @Override
    protected String getOption() {
        return OPTION;
    }

    @Override
    protected boolean executeRollback() {
        return true;
    }
}
