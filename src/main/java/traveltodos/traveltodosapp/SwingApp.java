package traveltodos.traveltodosapp;


import com.jidesoft.swing.JideButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


@SpringBootApplication
public class SwingApp extends JFrame {

    private Logger logger = LoggerFactory.getLogger(SwingApp.class);

    private JFrame frame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JideButton jideBtn;

    /**
     * Create the application.
     */
    public SwingApp() {
        initialize();
        this.showButtonDemo();
    }

    /**
     * Launch the application.
     */
//    public static void main(String[] args) {
//
//        SwingApp window = new SwingApp();
////        window.showButtonDemo();
//    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("Java Swing Examples");
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(3, 1));

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
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

            logger.info(statusLabel.getText());
        });
        javaButton.addActionListener(e -> {
            statusLabel.setText("Submit Button clicked.");

            logger.info(statusLabel.getText());
        });
        cancelButton.addActionListener(e -> {
            statusLabel.setText("Cancel Button clicked.");

            logger.info(statusLabel.getText());
        });
        controlPanel.add(okButton);
        controlPanel.add(javaButton);
        controlPanel.add(cancelButton);

        frame.setVisible(true);
    }

}
