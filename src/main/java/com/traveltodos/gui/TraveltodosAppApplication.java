package com.traveltodos.gui;

import java.awt.EventQueue;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.traveltodos.config.GlobalProperties;

@SpringBootApplication
@Configuration
@ComponentScan({ "com.traveltodos", "com.traveltodos.messaging" })
public class TraveltodosAppApplication {

	@Autowired
	private GlobalProperties global;

	@Bean
	public ConnectionFactory connectionFactory() {
		String activeMqUrl = global.getBrokerurl();
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(activeMqUrl);
		return connectionFactory;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(TraveltodosAppApplication.class)
				.headless(false).run(args);

		EventQueue.invokeLater(() -> {
			ctx.getBean(SwingApp.class);
		});

	}

}
