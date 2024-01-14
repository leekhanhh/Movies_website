package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParticipantRepository extends JpaRepository<Participant,Long>, JpaSpecificationExecutor<Participant>{

}
