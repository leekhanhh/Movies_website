package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.MovieParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MovieParticipantRepository extends JpaRepository<MovieParticipant,Long>, JpaSpecificationExecutor<MovieParticipant> {
}
