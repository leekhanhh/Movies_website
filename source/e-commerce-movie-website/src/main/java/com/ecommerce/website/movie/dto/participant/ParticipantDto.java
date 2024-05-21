package com.ecommerce.website.movie.dto.participant;

import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
public class ParticipantDto {
    private Long id;
    private String name;
    private Integer kind;
    private String image;
    private String filmCharacter;
    private Long movieId;
}
