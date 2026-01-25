package com.camlong.homnayangi.domain.models;

import com.camlong.homnayangi.application.constants.MediaType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MediaSource implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String url;

    private MediaType mediaType;
}
