package com.traveltodos.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

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
	private KnowledgeBase knowledgeBase;
	private static StatelessKnowledgeSession session;

	@Autowired
	MessageSender ms;

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private Environment env;

	/**
	 * Create the application.
	 */
	public SwingApp() {
		initialize();
		showButtonsImplementation();
		setUpDrools();
	}

	private void setUpDrools() {
		try {
			knowledgeBase = createKnowledgeBaseFromSpreadsheet();
		} catch (Exception e1) {
			logger.error(e1.getMessage());
		}
		session = knowledgeBase.newStatelessKnowledgeSession();

	}

	private void localSend(String msg) {
		try {
			if (appContext != null) {
				ms = appContext.getBean(MessageSender.class);

				if (ms != null) {
					ms.sendMessage(msg);
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

		String imagePath = "frame-icon.jpg";
		Image icon = new javax.swing.ImageIcon(imagePath).getImage();
		frame.setIconImage(icon);

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

		textArea = createJTextArea();
		frame.add(textArea);

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

			HashMap<String, String> travelResultsMap = new HashMap<String, String>();
			Component[] panelComponents = controlPanel.getComponents();
			for (int i = 0; i < panelComponents.length; i++) {
				if (((JideButton) panelComponents[i]).getName().startsWith("button_")
						&& ((JideButton) panelComponents[i]).getForeground() == Color.BLUE) {

					String buttonText = ((JideButton) panelComponents[i]).getText();

					TravelPlan plan = new TravelPlan(buttonText);
					session.execute(plan);
					travelResultsMap.put(plan.getType(), plan.getWanted());
				}
			}

			String resultedString = travelResultsMap.values().toString();
			textArea.setText(resultedString);
			localSend(resultedString);

		});
		return button;
	}

	private JideButton createButton(String text) {
		JideButton button = new JideButton(text);
		button.setName("button_" + text);
		button.setForeground(Color.BLACK);
		button.addActionListener(e -> {
			logger.info(statusLabel.getText());

			if (button.getForeground() == Color.BLACK) {
				button.setForeground(Color.BLUE);
			} else {
				button.setForeground(Color.BLACK);
			}
//			plan.setType(button.getText());
//
//			KieSession session = openKieService();
//
//			session.insert(plan);
//			session.fireAllRules();
//
//			if (map.containsKey(plan.getType())) {
//				map.remove(plan.getType());
//			} else {
//				map.put(plan.getType(), plan.getWanted());
//			}

		});

		return button;
	}

//	private KieSession openKieService() {
//		KieSession kSession = null;
//		try {
//			KieServices ks = KieServices.Factory.get();
//			KieContainer kContainer = ks.getKieClasspathContainer();
//			kSession = kContainer.newKieSession("ksession-rule");
//		} catch (Throwable t) {
//			t.printStackTrace();
//		}
//		return kSession;
//	}

	private static KnowledgeBase createKnowledgeBaseFromSpreadsheet() throws Exception {
		DecisionTableConfiguration dtconf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
		dtconf.setInputType(DecisionTableInputType.XLS);

		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory.newClassPathResource("travel-plan-rules.xls"), ResourceType.DTABLE,
				dtconf);

		if (knowledgeBuilder.hasErrors()) {
			throw new RuntimeException(knowledgeBuilder.getErrors().toString());
		}

		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
		return knowledgeBase;
	}

}
