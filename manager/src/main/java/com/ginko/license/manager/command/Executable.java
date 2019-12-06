package com.ginko.license.manager.command;

import com.ginko.license.manager.exception.CommandException;

/**
 * @author ginko
 * @date 7/21/19
 */
public interface Executable {

    void execute() throws CommandException;

    void rollBack();
}
