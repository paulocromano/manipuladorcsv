FROM openjdk:11
COPY target/manipuladorcsv-1.0.jar /app/manipuladorcsv.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "manipuladorcsv.jar"]