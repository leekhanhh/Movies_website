package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.CategoryMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryMovieRepository extends JpaRepository<CategoryMovie,Long>, JpaSpecificationExecutor<CategoryMovie> {
}
