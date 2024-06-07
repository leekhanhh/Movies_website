package com.ecommerce.website.movie.form.movie.episode;

import lombok.Data;

@Data
public class CreateEpisodeForm {
    private String title;
    private String description;
    private String url;
    private int episodeNumber;
    private Long movieId;
}
