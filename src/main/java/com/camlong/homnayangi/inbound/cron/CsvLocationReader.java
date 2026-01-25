package com.camlong.homnayangi.inbound.cron;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CsvLocationReader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CsvLocationReader.class);

    @Override
    public void run(String... args) throws Exception {
//        GenericCsvReader reader = CsvReaderFactory.createForResourceFolder("locations/csv");
//        try {
//            List<GoogleMapsRecord> records = reader.readAll("hu_tieu_mi_quan11.csv", GoogleMapsRecord.class);
//            logger.info("Read {} google maps records from CSV", records.size());
//            if (!records.isEmpty()) {
//                GoogleMapsRecord sample = records.get(0);
//                logger.info("Sample title: {} | lat: {} | lon: {}", sample.getTitle(), sample.getLatitude(), sample.getLongitude());
//            }
//        } catch (Exception e) {
//            logger.error("Failed to read locations CSV: {}", e.getMessage(), e);
//        }
    }
}
