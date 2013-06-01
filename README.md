server-android
==============

This project is about a Server Application plus a Android application.

For practical purposes the client company will be named ACME. Our company is Telematic, so our packages could be named
com.telematic.

Our unit of work will be a job assigment. Which consist of a in-company agent creating an *assigment* for a specific
**field agent**.


Server Application
-----------------

The server will be based on JBoss AS 7.1.1. 

The server app will communicate to existing company systems. 

Application Features:
* Google Maps v3 API. Simple markers to show position of both *android devices* and *job assigments*.  
* AngularJS on front end.

Android Application
-----------------

Then Android application is aimed for a single device, Samsung Galaxy 2 10" (P5100). 

We can use the stock 4.0.x or the updated Cyangenmod firmware, that represents Android Jelly Bean,
but there is no stable version yet. Could be either. 

There are approximately 15 persons that would be using the app, so 15 tablets should be used.

Application Features: 

* RESTFul application, that communicates  with the Server application.
* Application has to allow image selection of the Gallery in order to upload n images realated to specific job.
* Canvas Drawing with stylus pen is required in order for the customer to write their signature.
* Embedded Google Maps V2 API. Simple Overlays to show positions of assigments.



Existing ACME Applications
----------------

ACME haves 3 main application that we concern:

* Oracle Siebel - CRM 
* Commercial Sistem
* Document Content Provider
