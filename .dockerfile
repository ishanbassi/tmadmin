FROM ibm-semeru-runtimes:open-17-jre-focal
COPY target/baby-0.0.1-SNAPSHOT.jar tmadmin-0.0.1-SNAPSHOT.jar
ENV _JAVA_OPTIONS="-XX:MaxRAM=256m"
CMD java $_JAVA_OPTIONS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -Dspring.datasource.url=$SPRING_DATASOURCE_URL -Dspring.liquibase.url=$SPRING_LIQUIBASE_URL -Dspring.datasource.username=$SPRING_DATASOURCE_USERNAME -Dspring.datasource.password=$SPRING_DATASOURCE_PASSWORD -jar baby-0.0.1-SNAPSHOT.jar
