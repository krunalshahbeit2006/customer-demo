/*
package com.example.demo.services.customer.addresses.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import com.example.demo.services.customer.persistence.Customer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Audited
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"id", "createdTimestamp", "lastModifiedTimestamp", "customer"})
@EqualsAndHashCode(exclude = {"id", "createdTimestamp", "lastModifiedTimestamp", "customer"})
@JsonIgnoreProperties({*/
/*"id", "createdTimestamp", "lastModifiedTimestamp", *//*
"customer"})
@Table(name = "ADDRESS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"CUSTOMER_ID", "UNIQUE_ID"})
)
public final class Address implements Serializable {

    private static final long serialVersionUID = -7917436381892824336L;

    @Id
    @SequenceGenerator(name = "address_seq_gen", sequenceName = "address_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq_gen")
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(name = "UNIQUE_ID", unique = true, nullable = false, updatable = false, length = 10)
    private String uniqueId;

    @Column(nullable = false, length = 50)
    private String addressLine1;

    @Column(nullable = false, length = 50)
    private String addressLine2;

    @Column(nullable = false, length = 35)
    private String city;

    @Column(nullable = false, length = 20)
    private String state;

    @Column(nullable = false, length = 6)
    private String zipCode;

    @Column(nullable = false, length = 3)
    private String countryCode;

    @Column(nullable = false, length = 15)
    private String country;

    @Column(nullable = false, updatable = false, length = 50)
    private String createdBy;

    @Column(nullable = false, length = 50)
    private String lastModifiedBy;

    @CreationTimestamp
    private LocalDateTime createdTimestamp;

    @UpdateTimestamp
    private LocalDateTime lastModifiedTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false, updatable = false)
    private Customer customer;

}
*/
