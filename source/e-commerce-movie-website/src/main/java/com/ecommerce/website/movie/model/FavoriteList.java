package com.ecommerce.website.movie.model;

import com.ecommerce.website.movie.model.audit.Auditable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = TablePrefix.PREFIX_TABLE + "favorite_list")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteList extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    Account account;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    Movie movie;
}
