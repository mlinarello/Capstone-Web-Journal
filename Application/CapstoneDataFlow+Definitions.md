Setup:

- HTML Button assigned a unique ID

- Event Listener set up on the html button by using the unique ID

- Change Listener set up on the dataStore

- Rest controller

- Service-template.yml is set up with the right endpoints to hit for the lambdas





Round Trip Steps:



1 User presses button on HTML element with associated event listener

2 EventListener triggers linked javascript function on the Javascript Page

3 Javascript Page function calls Javascript Client function with needed parameters

4 Javascript Client calls request function on the HTTP Client (axios)

5 HTTP Client generates HTTP Request with correct body and endpoint url and sends the request to the Backend (server)

6 The Server uses annotations to determine where to send the HTTP Request it received

7 REST Controller receives the HTTP Request and sends it to the right function according to it’s annotations

8 REST Controller calls the appropriate Java Service function(s)

9 Java Service does any necessary logic and calls the appropriate Java Client function(s)

10 Java Client does any necessary logic to prepare for the lambda call then calls the Endpoint Utility

11 Endpoint Utility determines where the lambda is on AWS and sends a HTTP request to it

13 The Lambda Handler receives and unpacks the HTTP request and calls the appropriate Lambda Service function(s)

14 The Lambda Service does logic and calls any appropriate Lambda DAO functions

15 The Lambda DAO makes calls out to the appropriate DynamoDB tables on AWS using annotation magic

     (Alternatively the DAO can make a call to an API instead using an HTTP request if so desired)

16 The DynamoDB tables return the requested data (or an error) to the Lambda DAO

     (Alternatively the API sends a HTTP response back to the DAO and the DAO handles that)

17 The Lambda DAO does any calculation or manipulation needed and returns the result to Lambda Service

18 The Lambda Service does any calculations or manipulation needed then returns the result to the Lambda Handler

19 The Lambda Handler turns the result into a HTTP response and sends it back to the Endpoint Utility (or if an error is not caught by this point it gets turned into a 502 response and is sent back)

20 The Endpoint Utility unpacks the HTTP response and returns the result to the Java Client (or throws an error depending on the result)

21 The Java Client returns control and any requested data to the Java Service

22 The Java Service returns control and any requested data to the REST Controller

23 The REST Controller returns a HTTP Response to the Server

24 The Server sends that HTTP Response to the HTTP Client (Frontend)

25 The HTTP Client returns the HTTP Response to the Javascript Client

26 The Javascript Client returns the response’s data to the Javascript Page

27 The Javascript Page stores that data in the dataStore

28 The ChangeListener on the dataStore detects that the dataStore has changed and calls it’s associated Javascript Page function

29 The Javascript Page function sets the .innerHtml on an element of the HTML Page, which updates the HTML, which changes what the user sees.





Definitions:

Controller    - a class that deals with receiving http calls in a java application in an organized manner

Client          - a class that deals with calling http endpoints and returning the relevant data in a usable fashion

             so that the rest of the classes don’t have to worry about how that happens

Service       - a class that does some sort of functionality and returns the result

DAO             - a class that deals with accessing and transforming data from an external source (usually a database or api call)

           so that the rest of the application (usually a service in particular) doesn’t need to worry about how it happens

Repository  - a class that deals with retrieving data from a database, generally used in spring and other integrated

        services that automate retrieving data from local databases (similar to a DAO)

Utility            - a class that deals exclusively in one complex operation, such as logging information,

             dealing with endpoints in aws, etc.

Handler      - a class that deals with receiving a http request and sending responses from a single endpoint,

        generally used when an application is not feasible or desirable, like for a lambda on aws





Created by Jacobus Crawford