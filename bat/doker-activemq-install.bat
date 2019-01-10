echo OFF
REM -t -i to close it from with CTRL+C
REM container list: docker container ls
REM stop detached container instance: docker stop activemq-docker

call docker run --name=activemq-docker -t -i -p 61616:61616 -p 8161:8161 rmohr/activemq 

REM use pause to keep window
pause