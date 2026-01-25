package com.camlong.homnayangi.outbound.db.entities;

import com.camlong.homnayangi.application.constants.Cuisine;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "dish_choice")
public class DishChoiceEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @ManyToOne
    private ApplicationUserEntity user;

    @Column
    private Date chosenDate;

    @Column
    private Cuisine cuisine;

}
