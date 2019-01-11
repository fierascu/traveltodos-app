package traveltodos.traveltodosapp;


import com.jidesoft.swing.JideButton;
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
import java.util.Date;

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

    /**
     * Create the application.
     */
    public SwingApp() {
        initialize();
        this.showButtonDemo();
    }

    private void localSend(String msg) {
        try {
            if (appContext != null) {
                ms = appContext.getBean(MyMessageSender.class);

                if (ms != null) {
                    ms.sendMessage("todosQue", msg);
                } else {
                    logger.error("message is null");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("Java Swing Examples");
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(3, 1));
        localSend("Start APP @ " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                localSend("QUIT");
                System.exit(0);
            }
        });
        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setSize(350, 100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        frame.add(headerLabel);
        frame.add(controlPanel);
        frame.add(statusLabel);
        frame.setVisible(true);

    }

    private void showButtonDemo() {
        headerLabel.setText("Control in action: Button");


        JideButton okButton = new JideButton("OK");
        JideButton javaButton = new JideButton("Submit");
        JideButton cancelButton = new JideButton("Cancel");
        cancelButton.setHorizontalTextPosition(SwingConstants.LEFT);

        okButton.addActionListener(e -> {
            statusLabel.setText("Ok Button clicked.");
            localSend(statusLabel.getText());
            logger.info(statusLabel.getText());
        });
        javaButton.addActionListener(e -> {
            statusLabel.setText("Submit Button clicked.");
            localSend(statusLabel.getText());
            logger.info(statusLabel.getText());
        });
        cancelButton.addActionListener(e -> {
            statusLabel.setText("Cancel Button clicked.");
            localSend(statusLabel.getText());
            logger.info(statusLabel.getText());
        });
        controlPanel.add(okButton);
        controlPanel.add(javaButton);
        controlPanel.add(cancelButton);

        frame.setVisible(true);
    }

}
