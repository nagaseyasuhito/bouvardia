package com.github.nagaseyasuhito.bouvardia;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

public class FullTextEntityManagerProvider implements Provider<FullTextEntityManager> {

    @Inject
    private Provider<EntityManager> entityManager;

    @Override
    public FullTextEntityManager get() {
        return Search.getFullTextEntityManager(this.entityManager.get());
    }
}
