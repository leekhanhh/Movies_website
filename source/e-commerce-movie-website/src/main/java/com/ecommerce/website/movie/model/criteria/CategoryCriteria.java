package com.ecommerce.website.movie.model.criteria;

import com.ecommerce.website.movie.model.Category;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryCriteria implements Serializable {
    private Long id;
    private String name;
    private Integer kind;
    private Long parentId;

    public Specification<Category> categorySpecification() {
        return new Specification<Category>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (getId() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("id"), getId()));
                }
                if (getName() != null) {
                    predicateList.add(criteriaBuilder.like(root.get("name"), "%" + getName().trim() + "%"));
                }
                if (getKind() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("kind"), getKind()));
                }
                if (getParentId() != null) {
                    Join<Category, Category> parentCategory = root.join("parentCategory", JoinType.INNER);
                    predicateList.add(criteriaBuilder.equal(parentCategory.get("id"), getParentId()));
                } else {
                    predicateList.add(criteriaBuilder.isNull(root.get("parentCategory")));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
