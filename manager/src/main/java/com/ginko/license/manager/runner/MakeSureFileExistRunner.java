package com.ginko.license.manager.runner;

import com.ginko.license.manager.contants.Constants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author ginko
 * @date 8/7/19
 */
@Component
public class MakeSureFileExistRunner implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {
        File rootFile = new File(Constants.LICENSE_ROOT);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
    }
}
