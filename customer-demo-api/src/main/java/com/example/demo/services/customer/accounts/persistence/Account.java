package com.example.demo.services.customer.accounts.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.example.demo.services.customer.persistence.Customer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;
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
@JsonIgnoreProperties({"customer"})
@Table(name = "ACCOUNT",
        uniqueConstraints = @UniqueConstraint(columnNames = {"CUSTOMER_ID", "UNIQUE_ID"})
)
public final class Account implements Serializable {

    private static final long serialVersionUID = 5969645649536847525L;

    @Id
    @SequenceGenerator(name = "account_seq_gen", sequenceName = "account_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq_gen")
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(name = "UNIQUE_ID", unique = true, nullable = false, updatable = false, length = 10)
    private String uniqueId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 35)
    private String iban;

    @Column(nullable = false, length = 11)
    private String bic;

    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false)
    private BigDecimal amount;

    @Column(updatable = false, length = 50)
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
