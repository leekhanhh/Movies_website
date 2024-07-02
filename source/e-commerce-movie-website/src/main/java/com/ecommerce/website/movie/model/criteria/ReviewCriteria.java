package com.ecommerce.website.movie.model.criteria;

import com.ecommerce.website.movie.model.Review;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewCriteria implements Serializable {
    private Long movieId;
    private Long userId;
    private String content;

    public Specification<Review> getSpecification() {
        return new Specification<Review>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Review> root, javax.persistence.criteria.CriteriaQuery<?> criteriaQuery, javax.persistence.criteria.CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (getMovieId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("movie").get("id"), getMovieId()));
                }
                if (getUserId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("account").get("id"), getUserId()));
                }
                if (getContent() != null) {
                    predicates.add(criteriaBuilder.like(root.get("content"), "%" + getContent() + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
