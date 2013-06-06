#Projects

## Introduction

Currently two projects are ready for remote access service via REST API.

* **acme** - ACME JAXWS SOAP based service emulator.
* **rest-server** - retrieve the data from ACME, provides REST APIs for remote application.

*The workflow* works now.

1. acme will generate the test data by Databump.
2. rest-server can get the latest data via JAXWS SOAP based web service 5 minutes and add them in Infinispan cache.
3. the data in infinispan cache is can be accessed by REST [http://localhost:8080/rest-server/rest/members/](http://localhost:8080/rest-server/rest/members/). 
4. member data can be update via REST API, the change should be sync to acme by JAXWS SOAP web service.

## ACME

There is a Timer based service(DataBump) as a dummy service to insert test data.

*MemberWS* is the dummy SOAP based service.

Run the command, 

	mvn clean package jboss-as:deploy

to deploy the application to running Jboss 7 server.

SOAP based service url, http://localhost:8080/acme/MemberWSService?wsdl


## REST Server

A timer based service is provided to retrieve data from ACME web service.

A Infinispan base cache is configured to store the data from ACME.

REST API is exposed to remote access, eg. get members, update member.

Run the command, 

	mvn clean package jboss-as:deploy

to deploy the application to running Jboss 7 server.

All members in cache can be access via [http://localhost:8080/rest-server/rest/members/](http://localhost:8080/rest-server/rest/members/).

## Issues

1. The JAXWS client does not share the same models(Member) with the server side (acme).

2. The Date format processing in JAXWS...maybe change to Joda date and time to get united programming model.
 
3. Logger will be switched to log4j logger instead of the JDK logging, this is not an issue, just a small improvement.
