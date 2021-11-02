package com.example.demo.services.customer.stub;

import com.example.demo.services.customer.persistence.Customer;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Collection;
import java.util.Collections;

public final class AssemblerStub {

    public CollectionModel<EntityModel<Object>> getCollectionModel() {
        final Collection<Link> links;
        final EntityModel<Object> entityModel;
        final Collection<EntityModel<Object>> collectionModels;

        entityModel = getEntityModel();
        collectionModels = Collections.singletonList(entityModel);

        links = getLinks();

        return CollectionModel.of(collectionModels, links);
    }

    public EntityModel<Object> getEntityModel() {
        final Customer customer;
        final Collection<Link> links;

        customer = new CustomerStub().getCustomer();
        links = getLinks();

        return EntityModel.of(customer, links);
    }

    public Collection<Link> getLinks() {
        return Collections.singletonList(getLink());
    }

    public Link getLink() {
        return Link.of("link");
    }

}
