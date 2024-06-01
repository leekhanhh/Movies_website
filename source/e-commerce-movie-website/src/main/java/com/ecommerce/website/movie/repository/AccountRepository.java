package com.ecommerce.website.movie.repository;

import com.ecommerce.website.movie.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    Account findFirstByEmail(String email);
    Optional<Account> findByEmail(String email);
}
