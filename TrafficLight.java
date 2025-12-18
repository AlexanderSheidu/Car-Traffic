package traffic;

import java.awt.Color;
import java.awt.Graphics;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

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

// TrafficLight class that defines the traffic lights

public class TrafficLight implements Runnable {
    private long countdownTime; // milliseconds
    private final int radius; // pixels
    private int x;
    private int y;
    private Color color;
    private String counter;
    private LocalTime start;
    private LocalTime pause;
    private boolean isPaused;
    private boolean isRunning;

    private boolean stop = false; // set to true to stop the simulation

    public TrafficLight(int radius, int x, int y) {
        this.counter = "";
        this.countdownTime = getRandomTime();
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.color = pickColor();
        this.start = LocalTime.now();
    }

    private Color pickColor() {
        switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0:
                return Color.GREEN;
            case 1:
                return Color.YELLOW;
            case 2:
                return Color.RED;
            default:
                return Color.GREEN;
        }
    }

    private long getRandomTime() {
        return (ThreadLocalRandom.current().nextInt(4) + 3) * 1000L; // 3-6 seconds in milliseconds
    }

    public void start() {
        this.start = LocalTime.now();
        this.isPaused = false;
        this.isRunning = true;
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
    }

    public Color getColor() {
        return color;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.drawString(counter, x, y);
        int gap = 10;
        int side = radius * 2;
        graphics.setColor(color);
        graphics.fillOval(x, y + gap, side, side);
        graphics.drawOval(x, y + gap, side, side);
        graphics.drawRect(x + radius * 2 / 3, y + gap + radius * 3, side / 4, side * 2);
        graphics.fillRect(x + radius * 2 / 3, y + gap + radius * 3, side / 4, side * 2);
    }

    private void changeColor() {
        if (color.equals(Color.RED))
            color = Color.GREEN;
        else if (color.equals(Color.YELLOW))
            color = Color.RED;
        else if (color.equals(Color.GREEN))
            color = Color.YELLOW;
    }

    public void run() {
        while (!stop) {
            try {
                if (isRunning) {
                    if (isPaused) {
                        counter = String.format("%.2f seconds", (countdownTime - start.until(pause, ChronoUnit.MILLIS)) / 1000.0);
                    } else {
                        long elapsedMillis = start.until(LocalTime.now(), ChronoUnit.MILLIS);
                        long remainingMillis = countdownTime - elapsedMillis;

                        if (remainingMillis <= 0) {
                            changeColor();
                            countdownTime = getRandomTime();
                            start = LocalTime.now();
                        } else {
                            counter = String.format("%.2f seconds", remainingMillis / 1000.0);
                        }
                    }
                }
                Thread.sleep(10); // Update every 10 milliseconds
            } catch (InterruptedException exc) {
                System.out.println(exc);
            }
        }
    }
}
