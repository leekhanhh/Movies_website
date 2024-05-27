package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.FavoriteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteListRepository extends JpaRepository<FavoriteList, Long>, JpaSpecificationExecutor<FavoriteList> {
    @Query("SELECT fl FROM FavoriteList fl WHERE fl.account.id = :accountId AND fl.movie.id = :movieId")
    FavoriteList findByAccountIdAndMovieId(Long accountId, Long movieId);
    @Query("DELETE FROM FavoriteList fl WHERE fl.account.id = :accountId AND fl.movie.id = :movieId")
    void deleteByUserIdAndMovieId(Long accountId, Long movieId);
}
