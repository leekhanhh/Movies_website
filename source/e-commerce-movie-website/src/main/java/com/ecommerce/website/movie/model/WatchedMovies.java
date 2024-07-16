package com.ecommerce.website.movie.model;


import com.ecommerce.website.movie.model.audit.Auditable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Document(collection = "watched-time-movie")
@EntityListeners(AuditingEntityListener.class)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WatchedMovies extends Auditable<ObjectId> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ObjectId id;
    private Long accountId;
    private Long movieId;
    private Long watchedTime;
    private Long duration;
}
