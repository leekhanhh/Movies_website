package com.ecommerce.website.movie.model;

import com.ecommerce.website.movie.model.audit.Auditable;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.*;

import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "movie")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Movie extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String overview;
    private Integer voteCount;
    private Double price;
    private String imagePath;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.REMOVE)
    private List<CategoryMovie> categoryMovieList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.REMOVE)
    private Set<MovieParticipant> movieParticipantList;
}
