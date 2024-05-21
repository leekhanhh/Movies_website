package com.ecommerce.website.movie.model.criteria;

import com.ecommerce.website.movie.model.Movie;
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
public class MovieCriteria implements Serializable {
    private Long id;
    private String title;
    private Double price;
    private Double fromPrice;
    private Double toPrice;
    public Specification<Movie> getSpecification() {
        return new Specification<Movie>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (getId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("id"), getId()));
                }
                if (getTitle() != null) {
                    predicates.add(criteriaBuilder.like(root.get("title"), "%" + getTitle() + "%"));
                }
                if (getPrice() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("price"), getPrice()));
                }
                if(getFromPrice() != null){
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"),getPrice()));
                }
                if (getToPrice() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), getPrice()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
