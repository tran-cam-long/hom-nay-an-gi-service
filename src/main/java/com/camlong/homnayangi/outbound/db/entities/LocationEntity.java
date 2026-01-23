package com.camlong.homnayangi.outbound.db.entities;

import com.camlong.homnayangi.application.constants.District;
import com.camlong.homnayangi.application.domain.models.GoogleSource;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "location")
public class LocationEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id

    private Long id;

    @Column
    private String name;

    @Column
    private List<String> aliases;

    @Column
    private District district;

    @Column
    private Float lat;

    @Column
    private Float lng;

    @Column
    private Float maxPrice;

    @Column
    private Float minPrice;

    @Column
    private List<String> cuisineTags;

    @Column
    private List<String> ambianceTags;

    @Column
    private Float credibilityScore;

    @Column(columnDefinition = "jsonb")
    private GoogleSource googleSource;


}
