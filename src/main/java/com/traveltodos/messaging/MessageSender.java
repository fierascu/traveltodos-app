package com.traveltodos.messaging;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.traveltodos.config.GlobalProperties;

@Service
public class MessageSender {

	private Logger logger = LoggerFactory.getLogger(MessageSender.class);

	@Autowired
	private ConnectionFactory connectionFactory;
	private JmsTemplate jmsTemplate;

	@Autowired
	private GlobalProperties global;
	
	private static String queName;

	@PostConstruct
	public void init() {
		this.jmsTemplate = new JmsTemplate(connectionFactory);
		queName = global.getActivemqQueName();
	}

	public void sendMessage(String queueName, String message) {
		logger.info("sending to que [{}]: ", message);
		jmsTemplate.send(queueName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	}

	public void sendMessage(String message) {
		sendMessage(queName, message);
	}

}