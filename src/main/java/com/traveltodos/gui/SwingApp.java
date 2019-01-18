package com.traveltodos.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;

import com.jidesoft.swing.JideButton;
import com.traveltodos.drools.TravelPlan;
import com.traveltodos.messaging.MessageSender;

@SpringBootApplication
public class SwingApp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(SwingApp.class);

	private JFrame frame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private JTextArea textArea;

	private static KieSession kieSession;

	private MessageSender ms;

	private final ApplicationContext appContext;

	private final Environment env;

	@Autowired
	public SwingApp(ApplicationContext appContext, Environment env, MessageSender ms) throws HeadlessException {
		this.appContext = appContext;
		this.env = env;
		this.ms = ms;

		initialize();
		showButtonsImplementation();
		setUpDrools();

	}

	@PostConstruct
	public void init() {
		localSend("Start APP @ " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("TravelTodosApp");
		frame.setSize(700, 700);
		frame.setLayout(new GridLayout(5, 1));
		
		String imagePath = "frame-icon.jpg";
		Image icon = new javax.swing.ImageIcon(imagePath).getImage();
		frame.setIconImage(icon);

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

		textArea = createJTextArea();
		frame.add(textArea);

		frame.setVisible(true);
	}

	private void setUpDrools() {

		String rulesxlsFilename = env.getProperty("traveltodos.rulesxls-filename");

		KieServices kieServices = KieServices.Factory.get();
		Resource dt = ResourceFactory.newClassPathResource(rulesxlsFilename, getClass());

		KieFileSystem kieFileSystem = kieServices.newKieFileSystem().write(dt);

		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();

		KieRepository kieRepository = kieServices.getRepository();

		ReleaseId krDefaultReleaseId = kieRepository.getDefaultReleaseId();
		KieContainer kieContainer = kieServices.newKieContainer(krDefaultReleaseId);

		kieSession = kieContainer.newKieSession();
	}

	private void localSend(String msg) {
		try {
			ms.sendMessage(msg);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
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
			genarateButtonAction(button);

		});
		return button;
	}

	private void genarateButtonAction(JideButton button) {
		logger.info(statusLabel.getText());

		HashMap<String, String> travelResultsMap = new HashMap<String, String>();
		Component[] panelComponents = controlPanel.getComponents();
		for (int i = 0; i < panelComponents.length; i++) {
			JideButton jideButton = (JideButton) panelComponents[i];
			if (jideButton.getName().startsWith("button_") && jideButton.getForeground() == Color.BLUE) {

				TravelPlan plan = new TravelPlan(jideButton.getText());
				if (kieSession != null) {
					kieSession.insert(plan);
					kieSession.fireAllRules();

					travelResultsMap.put(plan.getType(), plan.getWanted());

					plan.setType(button.getText());
				}
			}
		}

		String resultedString;
		if (travelResultsMap.isEmpty()) {
			resultedString = "Please select at least one button!";			
		} else {
			resultedString = travelResultsMap.values().toString();
		}
		textArea.setText(resultedString);
		localSend(resultedString);
	}

	private JideButton createButton(String text) {
		JideButton button = new JideButton(text);
		button.setName("button_" + text);
		button.setForeground(Color.BLACK);
		button.addActionListener(e -> {

			if (button.getForeground() == Color.BLACK) {
				button.setForeground(Color.BLUE);
			} else {
				button.setForeground(Color.BLACK);
			}

		});

		return button;
	}

}
