package com.ecommerce.website.movie.model;

import com.ecommerce.website.movie.model.audit.Auditable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "review")
@EntityListeners(AuditingEntityListener.class)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    Account account;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id")
    Movie movie;
    String content;
}
