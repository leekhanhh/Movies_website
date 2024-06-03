package com.ecommerce.website.movie.model.criteria;

import com.ecommerce.website.movie.model.FavoriteList;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class FavoriteListCriteria implements Serializable {
    private Long accountId;
    private Long movieId;

    public Specification<FavoriteList> getSpecification() {
        return new Specification<FavoriteList>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<FavoriteList> root, javax.persistence.criteria.CriteriaQuery<?> criteriaQuery, javax.persistence.criteria.CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (getAccountId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("account").get("id"), getAccountId()));
                }
                if (getMovieId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("movie").get("id"), getMovieId()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
