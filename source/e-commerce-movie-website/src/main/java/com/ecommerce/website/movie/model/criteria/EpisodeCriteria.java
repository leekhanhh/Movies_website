package com.ecommerce.website.movie.model.criteria;

import com.ecommerce.website.movie.model.Movie;
import com.ecommerce.website.movie.model.SubMovie;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EpisodeCriteria implements Serializable {
    private Long id;
    private String title;
    private String description;
    private String url;
    private int episodeNumber;
    private Long movieId;
    public Specification<SubMovie> getSpecification() {
        return new Specification<SubMovie>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<SubMovie> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (getId() != null) {
                    return criteriaBuilder.equal(root.get("id"), getId());
                }
                if(getTitle() != null){
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%" + getTitle() + "%"));
                }
                if(getDescription() != null){
                    predicateList.add(criteriaBuilder.like(root.get("description"), "%" + getDescription() + "%"));
                }
                if(getUrl() != null){
                    predicateList.add(criteriaBuilder.like(root.get("url"), "%" + getUrl() + "%"));
                }
                if(getEpisodeNumber() != 0){
                    predicateList.add(criteriaBuilder.equal(root.get("episodeNumber"), getEpisodeNumber()));
                }
                if(getMovieId() != null){
                    Join<SubMovie, Movie> joinMovie = root.join("movie", JoinType.INNER);
                    predicateList.add(criteriaBuilder.equal(joinMovie.get("id"), getMovieId()));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }


}
