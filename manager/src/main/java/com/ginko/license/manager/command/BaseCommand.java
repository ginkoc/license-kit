package com.ginko.license.manager.command;

import com.ginko.license.manager.exception.CommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ginko
 * @date 7/21/19
 */
public abstract class BaseCommand implements Executable {

    private static final Logger log = LoggerFactory.getLogger(BaseCommand.class);

    private Executable parentCommand;
    private boolean succeeded;
    private String resultMessage;

    public BaseCommand(Executable parentCommand) {
        this.parentCommand = parentCommand;
    }

    @Override
    public void execute() throws CommandException {
        try {
            log.info("Command '{}' begin executing...", getClass().getSimpleName());
            executeCommand();
        } catch (Exception e) {
            setResultMessage(e.getMessage());
            setSucceeded(false);
        } finally {
            if (!getSucceeded()) {
                rollBack();
            }
        }

        if (!getSucceeded()) {
            log.error("Executing command '{}' failed, cause by: {}", getClass().getSimpleName(), getResultMessage());
            throw new CommandException(getClass().getSimpleName(), getResultMessage());
        }

        log.info("Executing command '{}' successfully", getClass().getSimpleName());
    }

    @Override
    public void rollBack() {
        log.info("Command '{}' beginning rollback", getClass().getSimpleName());
        if (getSucceeded()) {
            executeRollback();
        }

        if (getParentCommand() != null) {
            parentCommand.rollBack();
        }
        log.info("Command '{}' ending rollback", getClass().getSimpleName());
    }

    /**
     * 命令执行的实际逻辑
     */
    protected abstract void executeCommand();

    /**
     * 命令回滚的实际逻辑
     * @return 是否回滚成功
     */
    protected abstract boolean executeRollback();

    public Executable getParentCommand() {
        return parentCommand;
    }

    public void setParentCommand(Executable parentCommand) {
        this.parentCommand = parentCommand;
    }

    protected void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    protected boolean getSucceeded() {
        return this.succeeded;
    }

    protected void setResultMessage(String msg) {
        this.resultMessage = msg;
    }

    public String getResultMessage() {
        return this.resultMessage;
    }
}
