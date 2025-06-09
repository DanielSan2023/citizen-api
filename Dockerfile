FROM openliberty/open-liberty:full-java17-openj9-ubi-minimal

COPY --chown=1001:0 target/citizen-api.war /config/apps/
COPY --chown=1001:0 src/main/liberty/config/server.xml /config/
COPY --chown=1001:0 src/main/liberty/config/resources/postgresql.jar /config/resources/
