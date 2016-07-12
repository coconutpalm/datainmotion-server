#Run using docker command
# docker run -d -p 8181:8181 –name karaf karaf:4.0.2
FROM java:8u66
MAINTAINER soucianceeqdamrashti <souciance.eqdam.rashti@gmail.com>
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV KARAF_VERSION=4.0.2
RUN mkdir /opt/karaf
ADD http://apache.openmirror.de/karaf/${KARAF_VERSION}/apache-karaf-${KARAF_VERSION}.tar.gz /opt/karaf/
WORKDIR /opt/karaf/
RUN tar –strip-components=1 -C /opt/karaf -xzf apache-karaf-${KARAF_VERSION}.tar.gz;
WORKDIR /opt/karaf/
RUN bin/start && \
#allow the Karaf process to start
sleep 10 && \
#install camel repo url and version
bin/client feature:repo-add camel 2.15.1 && \
#allow feature url installation to complete
sleep 5 && \
#install camel core
bin/client feature:install camel
RUN sleep 10
RUN bin/start && \
#allow the Karaf process to start
sleep 10 && \
#install camel repo url and version
bin/client feature:repo-add camel 2.15.1 && \
#allow feature url installation to complete
sleep 5 && \
#install camel core
bin/client feature:install camel
RUN sleep 10
RUN bin/start && \
#allow the Karaf process to start
sleep 10 && \
#install hawtio repo url and version
bin/client feature:repo-add hawtio 1.4.58 && \
#allow feature url installation to complete
sleep 5 && \
#install hawtio
bin/client feature:install hawtio
#COPY /config/org.ops4j.pax.logging.cfg /opt/karaf/etc/
EXPOSE 8181
ENTRYPOINT [”/opt/karaf/bin/karaf”, ”start”]

