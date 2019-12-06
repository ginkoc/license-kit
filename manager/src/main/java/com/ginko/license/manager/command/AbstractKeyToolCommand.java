package com.ginko.license.manager.command;

import com.ginko.license.common.utils.cmd.CommandExecutor;
import com.ginko.license.common.utils.cmd.CommandReturnCode;
import com.ginko.license.common.utils.cmd.CommandReturnValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author ginko
 * @date 7/21/19
 */
public abstract class AbstractKeyToolCommand extends BaseCommand implements Executable {

    private static final String KEYTOOL_COMMAND = "keytool";

    private Map<String, String> arguments;

    public AbstractKeyToolCommand(Map<String, String> arguments) {
        this(arguments, null);
    }

    public AbstractKeyToolCommand(Map<String, String> arguments, Executable parentCommand) {
        super(parentCommand);
        this.arguments = arguments;
    }

    @Override
    protected void executeCommand() {
        CommandReturnValue returnValue = CommandExecutor.runCommand(buildCommandWithArgs());
        if (!returnValue.getReturnCode().equals(CommandReturnCode.SUCCESS)) {
            setResultMessage(returnValue.getMessage());
            setSucceeded(false);
            return;
        }

        setSucceeded(true);
    }

    protected List<String> buildCommandWithArgs() {
        List<String> systemCommand = new ArrayList<>();
        systemCommand.add(KEYTOOL_COMMAND);
        systemCommand.add(getOption());

        getArguments().forEach((k, v) -> {
            if (k != null) {
                systemCommand.add(k);

                if (v != null) {
                    systemCommand.add(v);
                }
            }
        });
        return systemCommand;
    }

    /**
     * return true command
     * @return command
     */
    protected abstract String getOption();

    public Map<String, String> getArguments() {
        return arguments;
    }
}
