package com.ginko.license.common.utils.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author ginko
 * @date 7/20/19
 */
public class CommandExecutor {

    private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);
    private static final String DEFAULT_TEMPORARY_DIR = System.getProperty("user.home");
    private static final long DEFAULT_TIMEOUT = 3000;

    public static CommandReturnValue runCommand(List<String> command) {
        return runCommand(command, DEFAULT_TIMEOUT);
    }

    public static CommandReturnValue runCommand(final List<String> command, final long timeout) {
        CommandReturnValue returnValue = new CommandReturnValue(CommandReturnCode.FAIL);

        File stdoutFile = new File(DEFAULT_TEMPORARY_DIR);
        Process process = null;
        try {
            // Create file where stdout/stderr will be redirected:
            stdoutFile = Files.createTempFile(stdoutFile.toPath(), "command-output", ".tmp").toFile();

            ProcessBuilder builder = new ProcessBuilder()
                    .command(command)
                    //combine stdout and stderr
                    .redirectErrorStream(true)
                    .redirectOutput(stdoutFile);

            // Execute the command:
            process = builder.start();

            // Wait for process to finish:
            if (!process.waitFor(timeout, TimeUnit.MILLISECONDS)) {
                throw new Exception("Timeout occurred while executing command " + getCommandInLine(command));
            }

            returnValue.setReturnCode(process.exitValue() == 0 ? CommandReturnCode.SUCCESS : CommandReturnCode.FAIL);
            returnValue.setMessage(new String(Files.readAllBytes(stdoutFile.toPath())));
        } catch (Throwable t) {
            String message = t.getMessage() != null ? t.getMessage() : t.getClass().getName();
            log.error("Command execution failed: {}", message);
            log.debug("Exception:", t);
            returnValue.setMessage(message);
        } finally {
            if (process != null) {
                process.destroy();
            }
            //remove tmp file
            removeFile(stdoutFile.toPath());

            if (returnValue.getReturnCode() == CommandReturnCode.FAIL) {
                log.error("Execute command '{}' failed, cause by: {}",
                        getCommandInLine(command), returnValue.getMessage());
            } else {
                log.info("Execute command '{}' successfully, info: {}",
                        getCommandInLine(command), returnValue.getMessage());
            }
        }
        return returnValue;
    }

    private static String getCommandInLine(List<String> command) {
        return command.stream().map(cmd -> cmd).collect(Collectors.joining(" "));
    }

    private static void removeFile(Path path) {
        if (path != null && !path.equals(Paths.get(DEFAULT_TEMPORARY_DIR))) {
            try {
                Files.delete(path);
            } catch (IOException ex) {
                log.debug("Failed to delete temporary file '{}': {}", path, ex.getMessage());
            }
        }
    }
}
