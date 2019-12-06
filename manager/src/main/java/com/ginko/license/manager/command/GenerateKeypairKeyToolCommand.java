package com.ginko.license.manager.command;

import com.ginko.license.manager.exception.CommandException;
import com.ginko.license.manager.utils.KeyToolCommandBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author ginko
 * @date 7/21/19
 */
public class GenerateKeypairKeyToolCommand extends AbstractKeyToolCommand {

    private static final Logger log = LoggerFactory.getLogger(GenerateKeypairKeyToolCommand.class);
    private static final String OPTION = "-genkeypair";

    public GenerateKeypairKeyToolCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public GenerateKeypairKeyToolCommand(Map<String, String> arguments, Executable parentCommand) {
        super(arguments, parentCommand);
    }

    @Override
    protected String getOption() {
        return OPTION;
    }

    @Override
    protected boolean executeRollback() {
        AbstractKeyToolCommand rollBackCommand = new KeyToolCommandBuilder(KeyToolCommandType.DELETE_KEY)
                .alias(getArguments().get(KeyToolCommandBuilder.ALIAS))
                .keystore(getArguments().get(KeyToolCommandBuilder.KEYSTORE))
                .storepass(getArguments().get(KeyToolCommandBuilder.STOREPASS))
                .build();
        try {
            rollBackCommand.execute();
        } catch (CommandException e) {
            log.error("failed on rollback command '{}', cause by '{}'", getClass().getSimpleName(), e);
            return false;
        }
        return true;
    }
}
