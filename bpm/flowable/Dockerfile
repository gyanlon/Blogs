FROM flowable/all-in-one

COPY application.properties /opt/tomcat/lib
COPY mysql-connector-java-8.0.15.jar /opt/tomcat/lib

ENV CATALINA_HOME /opt/tomcat
ENV PATH $PATH:$CATALINA_HOME/bin
ENV JAVA_OPTS="-Xms512M -Xmx4G -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

WORKDIR /opt/tomcat

USER tomcat

CMD ["/opt/tomcat/bin/catalina.sh", "run"]

