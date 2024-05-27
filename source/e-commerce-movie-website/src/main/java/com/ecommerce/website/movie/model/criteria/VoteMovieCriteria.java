package com.ecommerce.website.movie.model.criteria;

import com.ecommerce.website.movie.model.VoteMovie;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class VoteMovieCriteria implements Serializable {
    private Long movieId;
    private Long accountId;

    public Specification<VoteMovie> getSpecification() {
        return new Specification<VoteMovie>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<VoteMovie> root, javax.persistence.criteria.CriteriaQuery<?> criteriaQuery, javax.persistence.criteria.CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (getMovieId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("movie").get("id"), getMovieId()));
                }
                if (getAccountId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("account").get("id"), getAccountId()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
