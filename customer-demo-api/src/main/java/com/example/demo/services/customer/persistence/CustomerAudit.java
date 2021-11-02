package com.example.demo.services.customer.persistence;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@RevisionEntity(AuditRevisionListener.class)
@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "CUSTOMER_REV")
@AttributeOverride(name = "timestamp", column = @Column(name = "REVTSTMP"))
@AttributeOverride(name = "id", column = @Column(name = "REV"))
public class CustomerAudit extends DefaultRevisionEntity implements Serializable {

    private static final long serialVersionUID = 8030082158763956879L;

    @Column(name = "USERNAME", nullable = false)
    private String username;
}
