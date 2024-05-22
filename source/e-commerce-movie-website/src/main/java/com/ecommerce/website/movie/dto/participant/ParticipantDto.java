package com.ecommerce.website.movie.dto.participant;

import com.ecommerce.website.movie.dto.movie.MovieDto;
import lombok.Data;

@Data
public class ParticipantDto {
    private Long id;
    private String name;
    private Integer kind;
    private String image;
    private String filmCharacter;
    private MovieDto movie;
}
