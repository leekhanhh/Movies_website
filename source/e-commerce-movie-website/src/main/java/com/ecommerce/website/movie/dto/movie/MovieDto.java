package com.ecommerce.website.movie.dto.movie;

import com.ecommerce.website.movie.dto.BaseInfo;
import com.ecommerce.website.movie.dto.category.movie.CategoryMovieDto;
import com.ecommerce.website.movie.dto.movie.participant.MovieParticipantDto;
import lombok.Data;

import java.util.HashSet;
import java.util.List;

@Data
public class MovieDto extends BaseInfo {
    private String title;
    private String overview;
    private Integer voteCount;
    private Double price;
    private String imagePath;
    private List<CategoryMovieDto> categoryMovieList;
    private HashSet<MovieParticipantDto> movieParticipantList;
}
