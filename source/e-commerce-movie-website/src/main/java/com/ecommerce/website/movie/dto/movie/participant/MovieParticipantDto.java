package com.ecommerce.website.movie.dto.movie.participant;

import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.dto.participant.ParticipantDto;
import lombok.Data;

@Data
public class MovieParticipantDto {
    private Long id;
    private MovieDto movie;
    private ParticipantDto participant;
}
