#Project Setup

## Introduction

Currently two projects are ready for remote access service via REST API.

* **deludo** - deludo JAXWS SOAP based service emulator. http://107.21.233.48:8080/deludo/MemberWSService?wsdl
* **spark** - retrieve the data from deludo, provides REST APIs for remote application.

*The workflow* works now.

1. **deludo** will generate the test data by Databump.
2. **spark** can get the latest data via JAXWS SOAP based web service every 5 minutes and add them into Infinispan cache.
3. The data in infinispan cache is can be accessed by REST [http://localhost:8080/spark/rest/members/](http://localhost:8080/spark/rest/members/). 
4. The data can be update via REST API, the change can be sync to **deludo** by JAXWS SOAP web service.

## deludo

There is a Timer based service(Datapump) as a dummy service to insert test data.

*MemberWS* is the dummy SOAP based service.

Run the command, 

	mvn clean package jboss-as:deploy

to deploy the application to running Jboss 7 server.

SOAP based service url, http://107.21.233.48:8080/deludo/MemberWSService?wsdl


## Spark Server

A timer based service is provided to retrieve data from deludo web service.

A Infinispan base cache is configured to store the data from deludo.

REST API is exposed to remote access, eg. get members, update member.

Run the command, 

	mvn clean package jboss-as:deploy

to deploy the application to running Jboss 7 server.

All members in cache can be access via [http://localhost:8080/spark/rest/members/](http://localhost:8080/spark/rest/members/).

## Issues and workaround

1. If you encountered Eclipse IDE compilation errors, follow these steps to fix it.

 * Run `mvn clean compile` in the project folder.
 * Right click the project node, select *Maven/Update projects* to update Maven Eclipse settings.

 Now the project should be resolved by Eclipse IDE correctly.

2. If you encountered an Infinispan cache exception when adding data into cache, try to remove the cache folder("member-cache") in the <JBoss 7>/bin, and restart JBoss.
 
3. Logger will be switched to log4j logger instead of the JDK logging, this is not an issue, just a small improvement maybe change in future.
