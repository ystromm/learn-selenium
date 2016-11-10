package com.github.ystromm.learn_selenium.webapp.webdriver;

import com.google.common.base.Charsets;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Firefox {

    public static FirefoxDriver firefoxDriver() throws IOException {
        final FirefoxBinary firefoxBinary = Travis.onTravis() ? new FirefoxBinary(new File(getFirefoxPath())) : new FirefoxBinary();
        return new FirefoxDriver(firefoxBinary, new FirefoxProfile());
    }

    static String getFirefoxPath() throws IOException {
        final ProcessBuilder processBuilder = new ProcessBuilder("which", "firefox");
        processBuilder.redirectErrorStream(true);
        final Process process = processBuilder.start();
        try (final InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), Charsets.UTF_8);
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return bufferedReader.readLine();
        }
    }

    // hidden
    private Firefox() {
        // empty
    }
}
