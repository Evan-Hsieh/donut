#!/bin/bash
DONUT_HOME="$( cd "$( dirname "$0" )/.." && pwd  )"

java -jar -DDONUT_HOME=$DONUT_HOME -Dlog4j.configuration=file:$DONUT_HOME/conf/application-log4j.properties $DONUT_HOME/server/webapp/web.war $DONUT_HOME/server/webapp/web.war