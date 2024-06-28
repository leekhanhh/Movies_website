package com.ecommerce.website.movie.form.movie.episode;

import lombok.Data;

@Data
public class UpdateEpisodeForm {
    private Long id;
    private String title;
    private String description;
    private String url;
    private Integer episodeNumber;
    private Long movieId;
}
