package com.ginko.license.common.utils.cmd;

/**
 * @author ginko
 * @date 7/20/19
 */
public class CommandReturnValue {

    private CommandReturnCode returnCode;

    private String message;

    public CommandReturnValue(CommandReturnCode returnCode) {
        this(returnCode, null);
    }

    public CommandReturnValue(CommandReturnCode returnCode, String message) {
        this.returnCode = returnCode;
        this.message = message;
    }

    public CommandReturnCode getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(CommandReturnCode returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
