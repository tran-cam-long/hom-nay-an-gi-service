package com.camlong.homnayangi.application.domain.models;

import com.camlong.homnayangi.application.constants.District;
import lombok.Data;
import org.springframework.data.geo.Point;

import java.util.List;

@Data
public class Location {
    private Long id;

    private String name;

    private List<String> aliases;

    private District district;

    private String address;

    private String openingHours;

    private Point coordinate;

    private Float maxPrice;

    private Float minPrice;

    private List<String> cuisineTags;

    private List<String> ambianceTags;

    private Float credibilityScore;

    private GoogleSource googleSource;
}
