package com.ecommerce.website.movie.model;

import com.ecommerce.website.movie.model.audit.Auditable;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = TablePrefix.PREFIX_TABLE + "payment_detail")
public class PaymentDetail extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;
    private String cardHolder;
    private String expiredDate;
    private String cvv;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
