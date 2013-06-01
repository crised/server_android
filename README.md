server-android
==============

This project is about a Server Application plus a Android application.

For practical purposes the client company will be named ACME. Our company is Telematic, so our packages could be named
com.telematic.

Our unit of work will be a job assignment. Which consist of a back office agent creating an *assignment* for a specific **field agent**.

Important Relationships:

* 3 ACME applications.
* 1 Telematic Server Application.
* Any number of clients connected to Admin Client.
* 12 android tablets for field agents.
* Each created Job must be assigned to one specific agent.


Telematic Server Application
-----------------

The server will be based on JBoss AS 7.1.1.  This server will be behind Apache
httpd server, in order to manage SSL encryption.

The server app will communicate to existing ACME company services with SOAP web services on the back end, on the front end side: with RESTful to Admin and Android client applications.

JAX-WS services will fetch XML data from two different Web Services. The idea is to have one POJO (@Entity), named e.g. *JobTask*, that packs all the information needed for the field agent to perform his job. (Customer#Id, Address, GPS Position, Payment Status, field agent?, etc).  

One idea can be to extend both classes that consume the 2 web services, (assuming each SOAP/XML Web Service maps to a specific class).  Then instance and persist JobTask. After persisting succeeds,  the RESTful resource should be available. 

Keep in mind that Siebel Task can be CREATE/UPDATE/DELETE , so each notice should be send  to the device. We would have to think of some locking mechanism when a specific job task is being changed by Siebel, at the same time android app is uploading finished information.


Admin Browser Client Application
----------------

Local ACME intranet website, embedded in Telematic Server application, which communicates with the same RESTful API interface that android devices communicate. 

Purpose is to show markers in the map about the current jobs to do, showing an overlay of job details. 


Features:

* Google Maps v3 API. Simple markers to show position of both *android devices* and *job assignments*.  Each marker provides information about job task. 

* AngularJS on front end. 

Android Application
-----------------

Then Android application is aimed for a single device, Samsung Galaxy 2 10" (P5100). 

We can use the stock 4.0.x or the updated Cyanogenmod firmware, that represents Android Jelly Bean, but there is no stable version yet. Could be either. Latter is prefered in order to limit the number of applications so users won't be able to drain battery on other stuff but our application.


Application Features: 

* RESTFul application, that communicates  with the Server application.
* Application has to allow image selection of the Gallery in order to upload n images related to specific job.
* Canvas Drawing with stylus pen is required in order for the customer to write their signature.
* Embedded Google Maps V2 API. Simple Overlays to show positions of assignments.
* Google Cloud Messaging for Android, this notification will be received to wake 
the device in order to GET a specific URL for a list of jobs.


Existing ACME Applications
----------------

ACME haves 3 main application that we concern:

* Oracle Siebel - CRM 
* Commercial System
* Document Content Provider

Other Considerations
----------------
Sonar is considered for application analysis, both on server code and android code.

2 micro instances with fixed ip will be setted in order to have 2 working servers (ACME & Telematic).
