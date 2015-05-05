
package com.github.wokier.maven.progress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Keeps track of the reactor progress.
 */
public class ReactorProgress
{

    private static final String REACTOR_INDEX_KEY = "reactor.index";

    private static final String REACTOR_SIZE_KEY = "reactor.size";

    private int reactorSize = 0;

    private int reactorIndex = 0;

    private double reactorPercentage = 0;

    private String projectBaseDir;

    public String getProjectBaseDir() {
        return projectBaseDir;
    }

    public ReactorProgress(int reactorIndex, int reactorSize) {
        this.reactorIndex = reactorIndex;
        this.reactorSize = reactorSize;
    }

    public int getReactorIndex() {
        return reactorIndex;
    }

    public double getReactorPercentage() {
        return reactorPercentage;
    }

    public int getReactorSize() {
        return reactorSize;
    }

    public void updateProgress(int currentReactorSize, File currentProjectBasedir) {

        reactorIndex++;
        reactorSize = Math.max(currentReactorSize, reactorSize);
        reactorPercentage = reactorIndex * 1.0 / reactorSize * 100;
        projectBaseDir = clearFilePath(currentProjectBasedir);

    }

    public static ReactorProgress loadFromPropertiesFile(File file) throws IOException {

        Properties properties = new Properties();

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);

            int reactorIndex = Integer.valueOf(properties.getProperty(REACTOR_INDEX_KEY, "0"));
            int reactorSize = Integer.valueOf(properties.getProperty(REACTOR_SIZE_KEY, "0"));

            return new ReactorProgress(reactorIndex, reactorSize);
        }
        finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    public void saveToPropertiesFile(File file) throws IOException {
        Properties properties = new Properties();
        properties.setProperty(REACTOR_INDEX_KEY, Integer.toString(reactorIndex));
        properties.setProperty(REACTOR_SIZE_KEY, Integer.toString(reactorSize));

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            properties.store(fileOutputStream, null);
        }
        finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }

    }

    private String clearFilePath(File currentProjectBasedir) {

        if (File.separatorChar == '\\') {
            return currentProjectBasedir.toString().replace("\\", "\\ "); // avoid crunching 'n' characters in a path using windows and Snarl
        }
        return currentProjectBasedir.toString();

    }

    @Override
    public String toString() {
        return String.format("%d/%d (%.2f%%) %s", reactorIndex, reactorSize,
                reactorPercentage, projectBaseDir);
    }
}
