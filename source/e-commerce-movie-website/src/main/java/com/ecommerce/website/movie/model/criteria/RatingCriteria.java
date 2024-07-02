package com.ecommerce.website.movie.model.criteria;

import com.ecommerce.website.movie.model.Rating;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
@Data
public class RatingCriteria implements Serializable {
    private Long movieId;
    private Long accountId;
    private Double evaluation;

    public Specification<Rating> getSpecification() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (getMovieId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("movie").get("id"), getMovieId()));
            }
            if (getAccountId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("account").get("id"), getAccountId()));
            }
            if (getEvaluation() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("evaluation"), getEvaluation()));
            }
            return predicate;
        };
    }
}
