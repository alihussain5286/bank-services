# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
MAINTAINER Ali Hussain <alihussain5286@gmail.com>

COPY ./target/bank-client-1.0.0.jar /usr/app/
 
WORKDIR /usr/app
 
RUN sh -c 'touch bank-client-1.0.0.jar'

# Run the jar file 
ENTRYPOINT ["java","-XshowSettings:vm","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-XX:MaxRAMFraction=1","-Xms64m","-XX:+UseG1GC","-jar","bank-client-1.0.0.jar"]

