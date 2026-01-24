package com.camlong.homnayangi.inbound.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GenericCsvReader {

    private final CsvMapper csvMapper;
    private final String resourcesBasePath;

    public GenericCsvReader(String resourcesBasePath) {
        this.csvMapper = new CsvMapper();
        // Ignore unknown properties in CSV to be tolerant to extra columns
        this.csvMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.resourcesBasePath = resourcesBasePath;
    }

    public <T> List<T> readAll(String fileName, Class<T> clazz) throws Exception {
        String path = resourcesBasePath + (resourcesBasePath.endsWith("/") ? "" : "/") + fileName;
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }

            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<T> it = csvMapper.readerFor(clazz).with(schema).readValues(in);
            List<T> result = new ArrayList<>();
            while (it.hasNext()) {
                result.add(it.next());
            }
            return result;
        }
    }
}
