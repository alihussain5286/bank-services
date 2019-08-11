# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
MAINTAINER Ali Hussain <alihussain5286@gmail.com>

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8303

# The application's jar file
ARG JAR_FILE=target/statement-1.0.0.jar

# Add the application's jar to the container
ADD ${JAR_FILE} statement.jar

# Run the jar file 
ENTRYPOINT ["java","-XshowSettings:vm","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-XX:MaxRAMFraction=1","-Xms64m","-XX:+UseG1GC","-jar","/statement.jar"]

