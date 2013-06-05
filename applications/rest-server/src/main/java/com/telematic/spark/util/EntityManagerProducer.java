package com.telematic.spark.util;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Helper class to make EM available in CDI context
 * 
 * @author lcestari
 *
 */
public class EntityManagerProducer {
	
    @Produces
    @PersistenceContext
    //@Dependent //Default CDI scope
    private EntityManager em;
}
