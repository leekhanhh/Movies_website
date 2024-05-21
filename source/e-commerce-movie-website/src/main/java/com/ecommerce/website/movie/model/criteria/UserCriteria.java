package com.ecommerce.website.movie.model.criteria;

import com.ecommerce.website.movie.model.Account;
import com.ecommerce.website.movie.model.User;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCriteria implements Serializable {
    Long id;
    String email;
    String username;
    Integer gender;
    Date dateOfBirth;

    public Specification<User> getSpecification() {
        return new Specification<User>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                Join<User, Account> accountJoin = root.join("account", JoinType.INNER);
                if (getId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("id"), getId()));
                }
                if (getGender() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("gender"), getGender()));
                }
                if (getDateOfBirth() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("dateOfBirth"), getDateOfBirth()));
                }
                if (getEmail() != null) {
                    predicates.add(criteriaBuilder.like(accountJoin.get("email"), "%" + getEmail() + "%"));
                }
                if (getUsername() != null) {
                    predicates.add(criteriaBuilder.like(accountJoin.get("username"), "%" + getUsername() + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
