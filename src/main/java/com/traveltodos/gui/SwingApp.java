package com.traveltodos.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import com.jidesoft.swing.JideButton;
import com.traveltodos.config.GlobalProperties;
import com.traveltodos.drools.TravelPlanConfiguration;
import com.traveltodos.messaging.MessageBrowser;
import com.traveltodos.messaging.MessageReceiver;
import com.traveltodos.messaging.MessageSender;
import com.traveltodos.utils.Utils;

@SpringBootApplication
public class SwingApp extends JFrame {

	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(SwingApp.class);

	private JFrame frame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private JTextArea textArea;
	private JTextArea textAreaReceive;
	private JTextArea textAreaBrowse;

	private TravelPlanConfiguration tpc;

	private final Environment env;


	private static KieSession kieSession;
	private MessageSender ms;
	private MessageReceiver mr;
	private MessageBrowser mb;

	private GlobalProperties global;

	@Autowired
	public SwingApp(MessageSender ms, MessageReceiver mr, MessageBrowser mb, Environment env, GlobalProperties global) throws HeadlessException {
		String file = env.getProperty("traveltodos.rulesxls-filename");
		this.ms = ms;
		this.mr = mr;
		this.mb = mb;
		this.env = env;
		this.tpc = new TravelPlanConfiguration(file);
		this.global = global;

		initialize();
		showButtonsImplementation();

	}

	@PostConstruct
	public void init() {
		localSend("Start APP @ " + Utils.getTimeStampAsTime());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame(global.getAppName());
		frame.setSize(global.getFrameX(), global.getFrameY());
		frame.setLayout(new GridLayout(5, 1));

		String imagePath = "frame-icon.jpg";
		Image icon = new ImageIcon(imagePath).getImage();
		frame.setIconImage(icon);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				localSend("Quit APP @ " + Utils.getTimeStampAsTime());
				System.exit(0);
			}
		});

		headerLabel = new JLabel("", JLabel.LEFT);
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
		frame.add(createConsumeButton());
		frame.add(createBrowseButton());
		
		textArea = createJTextArea();
		frame.add(textArea);
		
		textAreaReceive = createJTextArea();
		frame.add(textAreaReceive);
		
		textAreaBrowse = createJTextArea();
		frame.add(textAreaBrowse);

		frame.setVisible(true);
	}

	private void localSend(String msg) {
		try {
			ms.sendMessage(msg);
		} catch (Exception e) {
			logger.error("Error sending: {} with errorMsg: {}", msg, e.getMessage());
			logger.debug("Error sending: {}", msg, e);
		}
	}
	
	private String localReceive() {
		try {
			return mr.receiveMessage(global.getActivemqQueName());
		} catch (Exception e) {
			logger.error("Error receiving on Q: {} with errorMsg: {}", global.getActivemqQueName(), e.getMessage());
			logger.debug("Error receiving on Q: {}", global.getActivemqQueName(), e);
		}
		
		return "NA";
	}
	
	private List<Message> localBrowse() {
		List<Message> messages = new ArrayList<Message>();
		try {
			messages = mb.browseMessages(global.getActivemqQueName());
		} catch (Exception e) {
			logger.error("Error browsing Q: {} with errorMsg: {}", global.getActivemqQueName(), e.getMessage());
			logger.debug("Error browsing Q: {}", global.getActivemqQueName(), e);
		}
		
		return messages;
	}

	private JTextArea createJTextArea() {
		JTextArea comp = new JTextArea();
		comp.setEditable(true);
		comp.setText("Your result will be here. Press Generate!");
		comp.setSize(100, 50);
		return comp;
	}

	private JideButton createConsumeButton() {
		JideButton button = new JideButton("Consume");
		button.setName("button_Consume");
		button.addActionListener(e -> {
			consumeButtonAction(button);

		});
		return button;
	}
	
	private JideButton createBrowseButton() {
		JideButton button = new JideButton("Browse");
		button.setName("button_Browse");
		button.addActionListener(e -> {
			browseButtonAction(button);

		});
		return button;
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
		HashMap<String, String> travelResultsMap = new HashMap<String, String>();
		Component[] panelComponents = controlPanel.getComponents();
		for (int i = 0; i < panelComponents.length; i++) {
			JideButton jideButton = (JideButton) panelComponents[i];
			if (jideButton.getName().startsWith("button_") && jideButton.getForeground() == Color.BLUE) {
				String keepType = jideButton.getText();
				String wanted = tpc.getWantedValue(keepType);
				travelResultsMap.put(keepType, wanted);
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
	
	private void consumeButtonAction(JideButton button) {
		textAreaReceive.setText(localReceive());
	}
	
	private void browseButtonAction(JideButton button) {
		textAreaBrowse.setAutoscrolls(true);
		textAreaBrowse.setText("");
		localBrowse().forEach(e -> {
			try {
				String msg = ((TextMessage) e).getText();
				
				textAreaBrowse.append(msg + "\n");
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}

	private JideButton createButton(String text) {
		JideButton button = new JideButton(text);
		button.setSize(50, 20);
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
