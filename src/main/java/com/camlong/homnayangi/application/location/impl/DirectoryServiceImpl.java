package com.camlong.homnayangi.application.location.impl;

import com.camlong.homnayangi.application.location.DirectoryService;
import com.camlong.homnayangi.inbound.cron.CsvLocationReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectoryServiceImpl implements DirectoryService {
    // Inbound
    private final CsvLocationReader csvLocationReader;

    // Outbound

    @Override
    public void importDirectory() {

    }
}
