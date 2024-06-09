package com.ecommerce.website.movie.dto.movie;

import com.ecommerce.website.movie.dto.BaseInfo;
import com.ecommerce.website.movie.dto.category.CategoryDto;
import com.ecommerce.website.movie.dto.moviegenre.MovieGenreDto;
import com.ecommerce.website.movie.dto.votemovie.VoteMovieDto;
import com.ecommerce.website.movie.model.VoteMovie;
import lombok.Data;

import java.util.List;

@Data
public class MovieDto extends BaseInfo {
    private String title;
    private String overview;
    private Double price;
    private String imagePath;
    private CategoryDto category;
    private List<MovieGenreDto> genres;
    private String videoGridFs;
    private List<EpisodeDto> episodes;
    private Integer voteCount;
}
