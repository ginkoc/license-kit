package com.ginko.license.manager.exception;

/**
 * @author ginko
 * @date 7/21/19
 */
public class CommandException extends Exception {

    private static final long serialVersionUID = 5686564574054398702L;
    private String command;
    private String msg;

    public CommandException(String command, String msg) {
        super(String.format("Execute %1$s failed, reason:%2$s", command, msg));
        this.command = command;
        this.msg = msg;
    }

    public String getCommand() {
        return command;
    }

    public String getMsg() {
        return msg;
    }
}
