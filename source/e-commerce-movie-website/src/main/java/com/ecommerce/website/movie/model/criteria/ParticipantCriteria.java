package com.ecommerce.website.movie.model.criteria;

import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.Participant;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ParticipantCriteria implements Serializable {
    private Long id;
    private String name;
    private Integer kind;
    private String filmCharacter;
    private Long movieId;
    public Specification<Participant> getSpecification() {
        return new Specification<Participant>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Participant> root, javax.persistence.criteria.CriteriaQuery<?> criteriaQuery, javax.persistence.criteria.CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (getId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("id"), getId()));
                }
                if (getName() != null) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + getName() + "%"));
                }
                if (getKind() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("kind"), getKind()));
                }
                if (getFilmCharacter() != null) {
                    predicates.add(criteriaBuilder.like(root.get("filmCharacter"), "%" + getFilmCharacter() + "%"));
                }
                if(getMovieId()!=null){
                    Join<Participant, Movie> joinMovie = root.join("movie", JoinType.INNER);
                    predicates.add(criteriaBuilder.equal(joinMovie.get("id"),getMovieId()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
