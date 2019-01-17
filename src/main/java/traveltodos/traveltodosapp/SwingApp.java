package traveltodos.traveltodosapp;

import com.jidesoft.swing.JideButton;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

@SpringBootApplication
public class SwingApp extends JFrame {

	private Logger logger = LoggerFactory.getLogger(SwingApp.class);

	private JFrame frame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;

	@Autowired
	MyMessageSender ms;
	@Autowired
	private ApplicationContext appContext;
	private Environment env;

	TravelPlan plan = new TravelPlan();

	HashMap<String, String> map = new HashMap<String, String>();

	/**
	 * Create the application.
	 */
	public SwingApp() {
		initialize();
		this.showButtonsImplementation();

	}

	private void localSend(String msg) {
		try {
			if (appContext != null) {
				ms = appContext.getBean(MyMessageSender.class);

				if (ms != null) {
					ms.sendMessage("todosQue", msg);
				} else {
					logger.error("message is null, can't send:" + msg);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingApp window = new SwingApp();
		window.showButtonsImplementation();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("TravelTodosApp");
		frame.setSize(700, 700);
		frame.setLayout(new GridLayout(5, 1));
		localSend("Start APP @ " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				localSend("QUIT");
				System.exit(0);
			}
		});
		headerLabel = new JLabel("", JLabel.CENTER);
		statusLabel = new JLabel("", JLabel.CENTER);
		statusLabel.setSize(100, 50);

		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		frame.add(headerLabel);
		frame.add(controlPanel);
		frame.add(statusLabel);
		
		frame.setVisible(true);
	}

	private void showButtonsImplementation() {
		headerLabel.setText("Who is travelling:");

		controlPanel.add(createButton("Female"));

		controlPanel.add(createButton("Male"));

		controlPanel.add(createButton("Kids"));

		controlPanel.add(createButton("Baby"));

		frame.add(createGenerateButton());
		
		frame.add(createJTextArea());

		frame.setVisible(true);
	}

	private JTextArea createJTextArea() {
		JTextArea comp = new JTextArea();
		comp.setEditable(true);
		comp.setText("Your result will be here. Press Generate!");
		comp.setSize(100, 50);
		return comp;
	}

	private JideButton createGenerateButton() {
		JideButton button = new JideButton("Generate");
		button.setName("button_Generate");
		button.addActionListener(e -> {
			logger.info(statusLabel.getText());
			String resultedString = map.values().toString();
			localSend(resultedString);
		});
		return button;
	}

	private JideButton createButton(String text) {
		JideButton button = new JideButton(text);
		button.setName("button_" + text);
		button.addActionListener(e -> {
			// localSend(statusLabel.getText());
			logger.info(statusLabel.getText());

			if (button.getForeground().getBlue() == 51 || button.getForeground().getBlue() == 0) {
				button.setForeground(Color.BLUE);
			} else {
				button.setForeground(Color.BLACK);
			}
			plan.setType(button.getText());

			KieSession session = openKieService();

			session.insert(plan);
			session.fireAllRules();

			if (map.containsKey(plan.getType())) {
				map.remove(plan.getType());
			} else {
				map.put(plan.getType(), plan.getWanted());
			}

		});

		return button;
	}

	private KieSession openKieService() {
		KieSession kSession = null;
		try {
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			kSession = kContainer.newKieSession("ksession-rule");
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return kSession;
	}

}
