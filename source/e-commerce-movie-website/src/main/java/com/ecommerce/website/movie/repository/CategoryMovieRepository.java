package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.CategoryMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CategoryMovieRepository extends JpaRepository<CategoryMovie,Long>, JpaSpecificationExecutor<CategoryMovie> {
    List<CategoryMovie> findAllByMovieId(Long movieId);

}
