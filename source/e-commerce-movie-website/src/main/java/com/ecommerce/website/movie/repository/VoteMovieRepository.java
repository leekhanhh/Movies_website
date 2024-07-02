package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.VoteMovie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface VoteMovieRepository extends JpaRepository<VoteMovie, Long>, JpaSpecificationExecutor<VoteMovie> {
    @Query("SELECT vm FROM VoteMovie vm WHERE vm.account.id = :accountId AND vm.movie.id = :movieId")
    VoteMovie findByAccountIdAndMovieId(Long accountId, Long movieId);

    @Query("SELECT vm FROM VoteMovie vm WHERE vm.account.id = :accountId")
    Page<VoteMovie> findAllByAccountId(Long accountId, Pageable pageable);
}
