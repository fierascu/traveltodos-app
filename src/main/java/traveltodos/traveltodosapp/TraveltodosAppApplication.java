package traveltodos.traveltodosapp;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.jms.ConnectionFactory;
import java.awt.*;

import org.kie.api.KieServices;



@SpringBootApplication
@Configuration
@ComponentScan
public class TraveltodosAppApplication {


    @Autowired
    private Environment env;

    @Bean
    public ConnectionFactory connectionFactory() {
        String activeMqUrl = env.getProperty("spring.activemq.broker-url");
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(activeMqUrl);
        return connectionFactory;
    }

	public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(SwingApp.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {
            SwingApp ex = ctx.getBean(SwingApp.class);
            ex.setVisible(true);
        });

	}

}

