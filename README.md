# traveltodos-app

## HoTos
1. run activemq with cmds
2. run JAR /target/traveltodos-app-0.0.1-SNAPSHOT.jar

## ActiveMQ
 - src: https://hub.docker.com/r/rmohr/activemq/
 
### Docker cmds:
	docker pull rmohr/activemq
	docker run -p 61616:61616 -p 8161:8161 rmohr/activemq

### Webconsole
	http://localhost:8161/admin/ admin/admin/
 
## ToDos
1. move credentials and port config to environment file
2. change default user/pass
3. deliveribles: asembly target & bat to start both 

## Inspiration
https://www.travelschecklist.com/
