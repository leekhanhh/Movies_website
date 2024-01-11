package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category,Long>, JpaSpecificationExecutor<Category> {
    Category findFirstByName(String name);
}
