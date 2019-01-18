package com.traveltodos.messaging;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

	private Logger logger = LoggerFactory.getLogger(MessageSender.class);

	@Autowired
	private ConnectionFactory connectionFactory;
	private JmsTemplate jmsTemplate;

	@Autowired
	private Environment env;
	private String queName;

	@PostConstruct
	public void init() {
		this.jmsTemplate = new JmsTemplate(connectionFactory);
		this.queName = env.getProperty("spring.activemq.que-name");
	}

	public void sendMessage(String queueName, String message) {
		logger.info("sending to que [" + queueName + "]: " + message);
		jmsTemplate.send(queueName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	}

	public void sendMessage(String message) {
		sendMessage(this.queName, message);
	}

}