package com.camlong.homnayangi.application.domain.models;

import com.camlong.homnayangi.application.constants.Forum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ForumSource implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String url;

    private Forum forum;
}
