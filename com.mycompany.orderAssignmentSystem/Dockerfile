FROM openjdk:8

WORKDIR /app

COPY target/com.mycompany.orderAssignmentSystem-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar

ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /

RUN chmod +x /wait-for-it.sh

CMD ["/wait-for-it.sh", "postgres:5432", "--timeout=30", "--strict", "--", "java", "-cp", "/app/app.jar", "com.example.orderAssignmentSystem.app.OrderAssignmentSwingApp", "--postgres-database=orderWorkerTestDb", "--postgres-user=postgres", "--postgres-pass=postgres","--postgres-host=postgres","--postgres-port=5432"]
