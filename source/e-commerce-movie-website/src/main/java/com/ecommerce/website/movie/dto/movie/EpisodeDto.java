package com.ecommerce.website.movie.dto.movie;

import com.ecommerce.website.movie.dto.BaseInfo;
import lombok.Data;

@Data
public class EpisodeDto extends BaseInfo {
    private String title;
    private String description;
    private String url;
    private int episodeNumber;
    private Long movieId;
}
