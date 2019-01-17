echo OFF
# -t -i to close it from with CTRL+C
# container list: docker container ls
# stop detached container instance: docker stop activemq-docker

call docker run --name=activemq-docker -t -i -p 61616:61616 -p 8161:8161 rmohr/activemq 

# use pause to keep window
pause