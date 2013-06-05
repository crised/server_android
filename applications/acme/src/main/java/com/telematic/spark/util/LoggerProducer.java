package com.telematic.spark.util;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Helper class to make Logger available in CDI context
 * 
 * @author lcestari
 *
 */
public class LoggerProducer {
	
    @Produces
    //@Dependent //Default CDI scope
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
