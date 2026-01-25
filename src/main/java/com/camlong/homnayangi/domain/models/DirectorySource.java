package com.camlong.homnayangi.domain.models;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
public class DirectorySource implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String url;

    private String orderOnline;

    private String placeId;

    private Float reviewRating;

    private Long reviewsCount;

    private Map<String, Map<String, Integer>> popularTimes;

    private Map<String, Integer> reviewPerRating;
}
