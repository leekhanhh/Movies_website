package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long>, JpaSpecificationExecutor<Category> {
    Category findFirstByName(String name);
    @Query("SELECT c.id, c.name FROM Category c WHERE c.kind = :kind")
    List<Object[]> findAllByKind(@Param("kind") Integer kind);
}
