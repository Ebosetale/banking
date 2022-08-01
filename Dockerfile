FROM openjdk:17-jdk-alpine AS builder
COPY . /usr/src/banking
WORKDIR /usr/src/banking
RUN ./mvnw package

FROM openjdk:17-alpine AS deploy
WORKDIR /usr/src/banking
COPY --from=builder /usr/src/banking/target/*.jar ./banking.jar
ENTRYPOINT ["java","-jar","banking.jar"]