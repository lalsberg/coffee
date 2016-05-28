FROM java:8
EXPOSE 8080
RUN mkdir /usr/src/coffee
COPY coffee-1.0.0.jar /usr/src/coffee
WORKDIR /usr/src/coffee
CMD java -jar coffee-1.0.0.jar
