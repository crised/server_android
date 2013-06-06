package com.telematic.spark.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Path where the JAX-RS classes will be available
 */
@ApplicationPath("/rest") // could use web.xml
public class RestActivator extends Application {
}
