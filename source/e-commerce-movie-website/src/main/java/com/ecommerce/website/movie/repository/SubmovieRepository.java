package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.SubMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubmovieRepository extends JpaRepository<SubMovie, Long>, JpaSpecificationExecutor<SubMovie> {
    SubMovie findByEpisodeNumberAndMovieId(int episodeNumber, Long movieId);
}
