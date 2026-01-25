package com.camlong.homnayangi.inbound.utils;

public class CsvReaderFactory {

    private CsvReaderFactory() {
    }

    public static GenericCsvReader createForResourceFolder(String resourceFolderPath) {
        return new GenericCsvReader(resourceFolderPath);
    }
}
