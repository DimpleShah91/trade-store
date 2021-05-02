# trade-store application

This project is based on Java / Maven / Spring boot(version 2.4.4) used to store trade information.
Application is build using eclipse IDE.

# How to run

This application is packaged as jar with embedded tomcat. You can run it as java application.

1. clone this repository
2. Make sure your are using java 1.8 version
3. Make sure to set environmental variables in JM Argument as -Dspring.profiles.active=dev
4. Use Junit4 to run the test cases individually
5. You can build this project using mvn clean install
6. Execute Spring boot application

Once the application is running you can use some of the rest api features.

Scheduler jobs runs every 5 minutes to update expiry trade flag

# How to run Junit test cases

1. Right click on src/test/java to run all the test cases under package.
2. Run As-> Run configuration
3. New Junit and select "Run all the test case in selected project, package" 
4. Select Test Runner as "Junit 4".
5. Apply and run

Same step as above can be used to run test class individually.

