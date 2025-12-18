package traffic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
* As a new engineer for a traffic congestion mitigation company, I have been tasked with developing a 
* Java GUI that displays time, traffic signals and other information for traffic analysts. The final GUI design
* should include viewing ports/panels to display the following components of the simulation.
*/

/**
* @author Alexander Sheidu
* Project 3
* Date: 7/30/2024
* Course: CMSC 335/6980
*/

// Main class and where everything is run in the main method

@SuppressWarnings("serial")
public class Main extends JFrame {
    Main() {
        super("Road");
        setResizable(false);
        
        TrafficManager trafficManager = new TrafficManager(1200, 260, 40);

        ClockDisplay clockDisplay = new ClockDisplay(trafficManager);
        Thread clockThread = new Thread(clockDisplay);

        TrafficDisplay trafficDisplay = new TrafficDisplay(trafficManager);

        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new FlowLayout());
        JButton btn1 = new JButton("Play");  // play/stop
        btn1.setFocusPainted(false);
        JButton btn2 = new JButton("Pause");  //pause/continue
        btn2.setEnabled(false);

        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btn1.setFocusPainted(true);
                if (btn1.getText().equals("Play")) {
                    if (!clockThread.isAlive()) {
                        clockThread.start();
                    }
                    clockDisplay.start();
                    trafficManager.start();
                    trafficDisplay.start();

                    btn1.setText("Stop");
                    btn2.setEnabled(true);
                } else {
                    clockDisplay.stop();
                    trafficManager.stop();
                    trafficDisplay.stop();
                    btn1.setText("Play");
                    btn2.setText("Pause");
                    btn2.setEnabled(false);
                }

                trafficDisplay.repaint();
            }
        });

        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btn2.getText().equals("Pause")) {
                    clockDisplay.pause();
                    trafficManager.pause();
                    btn2.setText("Continue");
                } else {
                    clockDisplay.contin();
                    trafficManager.contin();
                    btn2.setText("Pause");
                }

                trafficDisplay.repaint();
            }
        });

        mainPanel.add(btn1);
        mainPanel.add(btn2);
        
        add(clockDisplay, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.SOUTH);
        add(trafficDisplay, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1200, 300));
        setLocationRelativeTo(null);  // Center the window on the screen
        
        new Thread(trafficManager).start();
        new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ignored) {
                trafficDisplay.repaint();                
            }
        }).start();
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}
