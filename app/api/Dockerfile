FROM openjdk:jre-slim
RUN adduser --system web
VOLUME /tmp
ADD target/api.jar api.jar
ENV JAVA_OPTS=""
USER web
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /api.jar" ]