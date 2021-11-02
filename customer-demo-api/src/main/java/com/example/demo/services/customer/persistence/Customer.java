package com.example.demo.services.customer.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;

@Audited
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"id", "createdTimestamp", "lastModifiedTimestamp"})
@EqualsAndHashCode(exclude = {"id", "createdTimestamp", "lastModifiedTimestamp"/*, "accounts"*/})
@Table(name = "CUSTOMER", uniqueConstraints = @UniqueConstraint(columnNames = {"UNIQUE_ID"}))
public final class Customer implements Serializable {

    private static final long serialVersionUID = 2291551380857896495L;

    @Id
    @SequenceGenerator(name = "customer_seq_gen", sequenceName = "customer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq_gen")
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(name = "UNIQUE_ID", unique = true, nullable = false, updatable = false, length = 10)
    private String uniqueId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 15)
    private String telephone;

    @Column(nullable = false, updatable = false, length = 50)
    private String createdBy;

    @Column(nullable = false, length = 50)
    private String lastModifiedBy;

    @CreationTimestamp
    private LocalDateTime createdTimestamp;

    @UpdateTimestamp
    private LocalDateTime lastModifiedTimestamp;

    /*@NotEmpty*/
    /*@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Account> accounts;*/

    /*@NotEmpty*/
    /*@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Address> addresses;*/

}
