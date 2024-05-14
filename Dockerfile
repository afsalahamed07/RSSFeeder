# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the project jar file into the container at /app
COPY target/RSSFeeder-1.0-SNAPSHOT.jar /app/rss-feeder.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/rss-feeder.jar"]
