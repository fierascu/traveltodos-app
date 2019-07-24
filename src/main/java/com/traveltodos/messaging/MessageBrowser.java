package com.traveltodos.messaging;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageBrowser {

	private Logger logger = LoggerFactory.getLogger(MessageBrowser.class);

	@Autowired
	private ConnectionFactory connectionFactory;
	private JmsTemplate jmsTemplate;

	@PostConstruct
	public void init() {
		this.jmsTemplate = new JmsTemplate(connectionFactory);
	}

	public List<Message> browseMessages(String queueName) throws JMSException {
		
		List<Message> messages = jmsTemplate.browse(queueName, new BrowserCallback<List<Message>>() {

			@Override
			public List<Message> doInJms(Session session, QueueBrowser browser) throws JMSException {
				Enumeration<?> enumeration = browser.getEnumeration();
				List<Message> messages = new ArrayList<Message>();
				while(enumeration.hasMoreElements()) {
					Message message = (Message) enumeration.nextElement();
					logger.info("Found message: {}", message);
					messages.add(message);
				}
				return messages;
			}
			
		});
		
		logger.info("There are {} message in {}", messages.size(), queueName);
		
		return messages;
	}

}