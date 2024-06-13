package com.ecommerce.website.movie.model.criteria;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

@Data
public class WatchedMovieCriteria implements Serializable {
    private Long accountId;
    private Long movieId;
    public Specification<WatchedMovieCriteria> toSpecification() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("accountId"), accountId),
                    criteriaBuilder.equal(root.get("movieId"), movieId)
            );
        };
    }
}
