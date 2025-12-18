package traffic;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

// ClockDisplay class that defines the clock

public class ClockDisplay extends JPanel implements Runnable {
    private final int width;
    private final int height;
    private JLabel clockLabel;
    private LocalTime start;
    private LocalTime pause;
    private boolean isPaused;
    private boolean isRunning;

    public ClockDisplay(TrafficManager trafficManager) {
        super();
        width = trafficManager.getWidth();
        height = trafficManager.getHeight();
        setMinimumSize(new Dimension(width, height));

        clockLabel = new JLabel();
        this.add(clockLabel);
    }

    public void start() {
        this.start = LocalTime.now();
        this.isPaused = false;
        this.isRunning = true;
        clockLabel.setVisible(true);
    }

    public void pause() {
        this.isPaused = true;
        this.pause = LocalTime.now();
    }

    public void contin() {
        this.isPaused = false;
        this.start = this.start.plusNanos(pause.until(LocalTime.now(), ChronoUnit.NANOS));
    }

    public void stop() {
        this.isRunning = false;
        clockLabel.setVisible(false);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (isRunning) {
                    if (isPaused) {
                        clockLabel.setText(String.format("%.2f seconds", start.until(pause, ChronoUnit.MILLIS) / 1000.0));
                    } else {
                        clockLabel.setText(String.format("%.2f seconds", start.until(LocalTime.now(), ChronoUnit.MILLIS) / 1000.0));
                    }
                }

                clockLabel.setForeground(Color.blue);
                clockLabel.setFont(new Font("Times New Roman", Font.PLAIN, 24));
                repaint();

                Thread.sleep(10);  // update every 10 milliseconds
            } catch (InterruptedException ignored) {
            }
        }
    }
}
