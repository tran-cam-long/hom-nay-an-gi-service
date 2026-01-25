package com.camlong.homnayangi.inbound.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CamelCase-friendly POJO for CSV-first mapping. Uses Lombok to reduce boilerplate.
 * Fields are mapped from CSV headers via @JsonProperty where header name differs from camelCase field.
 * Unknown CSV columns are preserved in otherProperties via @JsonAnySetter.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleMapsRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("input_id")
    private String inputId;

    private String link;

    @JsonProperty("title")
    private String title;

    private String category;

    private String address;

    @JsonProperty("open_hours")
    private String openHours;

    @JsonProperty("popular_times")
    private String popularTimes; // keep raw JSON text for later parsing

    private String website;

    private String phone;

    @JsonProperty("plus_code")
    private String plusCode;

    @JsonProperty("review_count")
    private Long reviewCount;

    @JsonProperty("review_rating")
    private Double reviewRating;

    @JsonProperty("reviews_per_rating")
    private String reviewsPerRating; // raw JSON text

    private Double latitude;

    private Double longitude;

    private String cid;

    private String status;

    private String descriptions;

    @JsonProperty("reviews_link")
    private String reviewsLink;

    private String thumbnail;

    private String timezone;

    @JsonProperty("price_range")
    private String priceRange;

    @JsonProperty("data_id")
    private String dataId;

    @JsonProperty("place_id")
    private String placeId;

    private String images; // JSON array as text

    private String reservations;

    @JsonProperty("order_online")
    private String orderOnline;

    private String menu;

    private String owner;

    @JsonProperty("complete_address")
    private String completeAddress;

    private String about;

    @JsonProperty("user_reviews")
    private String userReviews;

    @JsonProperty("user_reviews_extended")
    private String userReviewsExtended;

    private String emails;

    // store any extra columns that don't have explicit fields
    @JsonIgnore
    private final Map<String, Object> otherProperties = new LinkedHashMap<>();

    @JsonAnySetter
    public void setOtherProperty(String name, Object value) {
        otherProperties.put(name, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getOtherProperties() {
        return otherProperties;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Review implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("ProfilePicture")
        private String profilePicture;

        @JsonProperty("Rating")
        private Integer rating;

        @JsonProperty("Description")
        private String description;

        @JsonProperty("Images")
        private List<String> images = new ArrayList<>();

        @JsonProperty("When")
        private String when;
    }
}
