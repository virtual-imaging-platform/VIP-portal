#!/bin/bash
mvn clean install -DskipTests
docker cp vip-portal/target/vip-portal-4.6.1.war vip-portal1:/vip/tomcat/webapps/ROOT.war
docker exec vip-portal1 rm -rf /vip/tomcat/webapps/ROOT
docker restart vip-portal1
