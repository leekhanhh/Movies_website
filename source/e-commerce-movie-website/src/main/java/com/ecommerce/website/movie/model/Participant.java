package com.ecommerce.website.movie.model;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "participant")
@Data
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer kind;
    private String image;
    private String character;
}
