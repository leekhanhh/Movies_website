package com.ecommerce.website.movie.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "favorite_list")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteList {
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
