FROM openjdk:8-jre-apline

COPY build/libs/kcron.jar /app/kcron.jar

WORKDIR /app

CMD ["java","-jar","./kcron.jar"]