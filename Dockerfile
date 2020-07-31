FROM hub.c.163.com/library/openjdk:8-jre-alpine

COPY build/libs/krcon.jar /app/krcon.jar

CMD ["java","-jar","/app/krcon.jar"]