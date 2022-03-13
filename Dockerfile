FROM maven:3.8.1-jdk-11-slim AS c2d431f3
WORKDIR /project
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM openjdk:11-jre
RUN mkdir -p /opt/logging \ && mkdir -p /opt/api-gateway/config
COPY --from=c2d431f3 /project/target/api-gateway.jar /opt/api-gateway/api-gateway.jar
ENTRYPOINT [ "java", "-Dfile.encoding=utf-8", "-Dsun.jnu.encoding=utf-8", "-DCONSUL_HOST=172.17.0.2", "-DDB_HOST=172.17.0.4", "-jar", "/opt/api-gateway/api-gateway.jar" ]