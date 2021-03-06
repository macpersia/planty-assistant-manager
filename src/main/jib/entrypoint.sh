#!/bin/sh

# Added by Hadi
apk update && apk add bash util-linux openssl
if [ -z "$JWT_SECRET_KEY" ]; then JWT_SECRET_KEY=$(openssl rand -base64 64 | tr '\n' -d); fi
JAVA_OPTS="${JAVA_OPTS} -Djhipster.security.authentication.jwt.secret=${JWT_SECRET_KEY}"
#echo "The secret JWT key is ${JWT_SECRET_KEY:?'absent'}."

echo "The application will start in ${JHIPSTER_SLEEP}s..." && sleep ${JHIPSTER_SLEEP}
#exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar "${HOME}/app.war" "$@"
exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "be.planty.managers.assistant.PlantyAssistantManagerApp"  "$@"
