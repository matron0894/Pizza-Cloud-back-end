package com.springproject.shavermacloud.domain;


import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "ALL_ORDERS")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 3234567L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE", nullable = false, updatable = false)
    private Date createdAt;

    @NotBlank(message = "Name is required")
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Zip code is required")
    private String zip;

    @CreditCardNumber(message = "Not a valid credit card number", ignoreNonDigitCharacters = true)
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message = "Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    @ManyToOne//(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    // @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "sum_product_order",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "shaverma_id")
    )
    private List<Shaverma> products = new ArrayList<>();

    public void addProduct(Shaverma shaverma) {
        this.products.add(shaverma);
    }

    public void removeProduct(Shaverma shaverma) {
        this.products.remove(shaverma);

    }

}
