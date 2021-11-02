package com.example.demo.services.customer.persistence;

import org.hibernate.envers.RevisionListener;

public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        CustomerAudit audit = (CustomerAudit) revisionEntity;
        audit.setUsername("admin");
    }
}
