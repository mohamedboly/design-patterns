package com.numeriquepro;

public abstract class BaseGameLoader {
    public final void load() {
        loadLocalData();
        createObjects();
        downloadAdditionalFiles();
        cleanTempFiles();
        initializeProfiles();
    }

    protected abstract void loadLocalData();
    protected abstract void createObjects();
    protected void downloadAdditionalFiles() {} // Hook (facultatif)
    protected void cleanTempFiles() {
        System.out.println("Cleaning temp files...");
    }
    protected abstract void initializeProfiles();
}