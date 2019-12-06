package com.ginko.license.manager.command;

import com.ginko.license.common.custom.CustomKeyStoreParam;
import com.ginko.license.manager.contants.Constants;
import com.ginko.license.manager.parameters.CreateLicenseParameters;
import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * @author ginko
 * @date 7/24/19
 */
public class CreateLicenseCommand extends BaseCommand {

    private CreateLicenseParameters parameters;

    public CreateLicenseCommand(CreateLicenseParameters parameters) {
        this(null, parameters);
    }

    public CreateLicenseCommand(Executable parentCommand, CreateLicenseParameters parameters) {
        super(parentCommand);
        this.parameters = parameters;
    }

    @Override
    protected void executeCommand() {
        final LicenseManager manager = new LicenseManager(buildLicenseParam());
        try {
            File output = new File(getParameters().getLicenseFile());
            manager.store(getParameters().getLicenseContent(), output);
        } catch (Exception e) {
            setSucceeded(false);
            setResultMessage(e.getMessage());
            return;
        }
        setSucceeded(true);
    }

    @Override
    protected boolean executeRollback() {
        return true;
    }

    private LicenseParam buildLicenseParam() {
        Preferences preferences = getParameters().getPreferences() == null ? null : getParameters().getPreferences();
        CipherParam cipherParam = new DefaultCipherParam(getParameters().getCipher());
        KeyStoreParam storeParam =
                new CustomKeyStoreParam(Constants.STORE_PATH, getParameters().getCipher(), Constants.STORE_PASS, Constants.KEY_PASS);
        return new DefaultLicenseParam(getParameters().getSubject(), preferences, storeParam, cipherParam);
    }

    public CreateLicenseParameters getParameters() {
        return parameters;
    }
}
