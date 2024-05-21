package com.ecommerce.website.movie.model;

import com.ecommerce.website.movie.model.audit.Auditable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class User extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Date dateOfBirth;
    Integer gender;
    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    Account userAccount;
}
