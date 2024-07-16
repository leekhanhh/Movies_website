package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie>{
    List<Movie> findAllByIdIn(List<Long> ids);
    List<Movie> findAllByCategoryId(Long categoryId);
}