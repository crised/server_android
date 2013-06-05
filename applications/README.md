#Projects

## Introduction

Currently the two project severs for remote access.

* **acme** - ACME SOAP based service emulator.
* **rest-server** - retrieve the data from ACME, provides REST APIs for remote application.

*Some problem is encountered, the whole workflow does not work now*.

## ACME

There is a Timer based service(DataBump) as a dummy service to insert test data.

*MemberWS* is the dummy SOAP based service.

Run the command, 

	mvn clean package jboss-as:deploy

to deploy the application to running Jboss 7 server.

SOAP based service url, [http://localhost:8080/acme/MemberWS?wsdl](http://localhost:8080/acme/MemberWS?wsdl)


## REST Server

A timer based service is provided to retrieve data from ACME web service.

A Infinispan base cache is configured to store the data from ACME.

REST API is exposed to remote access, eg. get members, update member.

Run the command, 

	mvn clean package jboss-as:deploy

to deploy the application to running Jboss 7 server.

## Issues

1. EJB timer is problematic when use CDI together, the @Schedule dose not work as expected. Must get CDI bean manually in @Schedule type EJB.
 
2. Logger will switch to log4j one instead of the JDK logging, this is not an issue, just a small improvement.
