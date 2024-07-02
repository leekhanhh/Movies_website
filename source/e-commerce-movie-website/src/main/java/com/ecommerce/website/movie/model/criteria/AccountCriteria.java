package com.ecommerce.website.movie.model.criteria;

import com.ecommerce.website.movie.model.Account;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AccountCriteria implements Serializable {
    Long id;
    String email;
    String fullname;
    String phone;
    Integer role;

    public Specification<Account> getSpecification() {
        return new Specification<Account>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (getId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("id"), getId()));
                }
                if (getFullname() != null) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullname")), "%" + getFullname().toLowerCase() + "%"));
                }
                if (getEmail() != null) {
                    predicates.add(criteriaBuilder.like(root.get("email"), "%" + getEmail() + "%"));
                }
                if (getPhone() != null) {
                    predicates.add(criteriaBuilder.like(root.get("phone"), "%" + getPhone() + "%"));
                }
                if(getRole() != null){
                    predicates.add(criteriaBuilder.equal(root.get("role"), getRole()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}