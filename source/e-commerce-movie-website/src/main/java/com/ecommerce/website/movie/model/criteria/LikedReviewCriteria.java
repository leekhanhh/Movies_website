package com.ecommerce.website.movie.model.criteria;

import com.ecommerce.website.movie.model.LikedReview;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class LikedReviewCriteria implements Serializable {
    private Long accountId;
    private Long reviewId;
    private Integer emotion;
    public Specification<LikedReview> getSpecification() {
        return new Specification<LikedReview>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<LikedReview> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (getAccountId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("account").get("id"), getAccountId()));
                }
                if (getReviewId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("review").get("id"), getReviewId()));
                }
                if (getEmotion() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("emotion"), getEmotion()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
