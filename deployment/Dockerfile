FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY ./applications/app-service/build/libs/ms-nequi-franchises-hub.jar ms-nequi-franchises-hub.jar
ENV JAVA_OPTS="-XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
# Replace with a non-root user to avoid running the container with excessive privileges
USER appuser
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar ms-nequi-franchises-hub.jar" ]