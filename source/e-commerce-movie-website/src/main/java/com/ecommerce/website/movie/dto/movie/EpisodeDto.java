package com.ecommerce.website.movie.dto.movie;

import lombok.Data;

@Data
public class EpisodeDto {
    private String title;
    private String description;
    private String url;
    private int episodeNumber;
    private Long movieId;
}
