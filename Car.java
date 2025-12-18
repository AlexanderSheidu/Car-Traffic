package traffic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
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

// Car class that defines the cars

public class Car {
    private int n;
    private final int side; // pixels
    private float x;
    private float y;
    private float speed; // speed 50-60 kph   
    private float distance; // meters
    private boolean paused;

    public Car(int n, int side, float x, float y) {
        this.n = n;
        this.side = side;
        this.x = x;
        this.y = y;
        this.speed = 50 + ThreadLocalRandom.current().nextFloat() * 10; // Initial speed between 50-60 kph
        this.paused = false;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getSide() {
        return side;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void pause() {
        paused = true;
        speed = 0; // Set speed to 0 when paused
    }

    public void resume() {
        paused = false;
    }

    public void updateDistanceAndSpeed(List<TrafficLight> lights) {
        if (!paused) {
            TrafficLight nearestLight = getNearestLight(lights);

            if (nearestLight != null) {
                float distanceToLight = nearestLight.getX() - x;
                distanceToLight = distanceToLight < 0 ? distanceToLight + 1000 : distanceToLight;

                // Check if the car has passed the intersection
                boolean hasPassedIntersection = (x + side) > (nearestLight.getX() + nearestLight.getRadius());

                if (!hasPassedIntersection) {
                    if ((distanceToLight <= 30) && nearestLight.getColor().equals(Color.YELLOW)) {
                        speed = 20 + ThreadLocalRandom.current().nextFloat() * 10; // Slow down to 20-30 kph
                    } else if (nearestLight.getColor().equals(Color.RED) && distanceToLight <= side) {
                        speed = 0; // Stop at red light
                    } else {
                        speed = 50 + ThreadLocalRandom.current().nextFloat() * 10; // Normal speed 50-60 kph
                    }
                } else {
                    // After passing the intersection, resume normal speed
                    speed = 50 + ThreadLocalRandom.current().nextFloat() * 10; // Normal speed 50-60 kph
                }

                float deltaX = (speed * 1000 / 3600) / 25; // Move per frame (40ms)
                x = (x + deltaX) % 1000; // Loop around
                distance += deltaX;
            }
        }
    }

    private TrafficLight getNearestLight(List<TrafficLight> lights) {
        return lights.stream()
                .min((l1, l2) -> Float.compare(Math.abs(l1.getX() - x), Math.abs(l2.getX() - x)))
                .orElse(null);
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.drawRect((int) x, (int) y, side, side);
        graphics.fillRect((int) x, (int) y, side, side);
        graphics.setColor(Color.WHITE);
        graphics.drawString(n + "", (int) x + side / 2, (int) y + side / 2);
        graphics.drawString(String.format("%d: %.0f m %.0f kph", n, distance, speed), (int) x, (int) y + 2 * side);
        graphics.setColor(Color.BLACK);
    }
}

