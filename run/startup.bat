
java -agentlib:jdwp=transport=dt_socket,server=y,address=8006,suspend=n -Dlogging.config=file:logback.xml -jar TestUser-0.0.1-SNAPSHOT.jar