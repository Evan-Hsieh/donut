@echo off
set bin_dir=%~dp0
set donut_home=%bin_dir%/..
echo %donut_home%
java -jar -DDONUT_HOME=%donut_home% -Dlog4j.configuration=file:%donut_home%/conf/application-log4j.properties %donut_home%/server/webapp/web.war %donut_home%/server/webapp/web.war