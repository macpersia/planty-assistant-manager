FROM openjdk:8-jre-alpine

# Added by Hadi
RUN apk update && apk add bash && apk add util-linux
#ARG JWT_SECRET_KEY
#RUN ["/bin/bash", "-c", ": ${JWT_SECRET_KEY:?Build argument needs to be set and not null.}"]
#ENV JAVA_OPTS "-Djhipster.security.authentication.jwt.secret=$JWT_SECRET_KEY"

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    # Changed by Hadi
    #JAVA_OPTS=""
    JAVA_OPTS="-Djhipster.security.authentication.jwt.secret=$JWT_SECRET_KEY"

# Add a jhipster user to run our application so that it doesn't need to run as root
RUN adduser -D -s /bin/sh jhipster
WORKDIR /home/jhipster

ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown jhipster:jhipster entrypoint.sh
USER jhipster

ADD *.war app.war

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8080 5701/udp

