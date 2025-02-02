FROM maven:3.9-eclipse-temurin-21

# go into the directory cd /app
WORKDIR /app

#everything after this is in /app

#copy files into /app directory
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

#Build the application - this run is for building
RUN mvn package -Dmaven.test.skip=true

# run the application 
# define railway environment variable
ENV PORT=8080
ENV SPRING_DATA_MONGODB_DATABASE=bggdb
ENV SPRING_DATA_MONGODB_URI=

#expose this port - reference environment variable
EXPOSE ${PORT} 

#run the program 
ENTRYPOINT SERVER_PORT=${PORT} java -jar target/d27workshop-0.0.1-SNAPSHOT.jar	
