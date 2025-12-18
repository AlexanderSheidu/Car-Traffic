package traffic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

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

// TrafficDisplay class that defines how the traffic is displayed

@SuppressWarnings("serial")
public class TrafficDisplay extends JPanel {
    private final List<TrafficLight> lights;
    private final List<Car> cars;
    private final int width;
    private final int height;

    public TrafficDisplay(TrafficManager trafficManager) {
        super();
        lights = trafficManager.getLights();
        cars = trafficManager.getCars();
        width = trafficManager.getWidth()-100;
        height = trafficManager.getHeight();
        setMinimumSize(new Dimension(width, height));
    }

    public void start() {
        synchronized (lights) {
            lights.forEach((light) -> {
                new Thread(light).start();
            });	
        }	        
    }

    public void stop() {
        synchronized (lights) {
            lights.forEach((light) -> {
                light.stop();
            });	
        }	        
    }    

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        float widthOffset = (getWidth() - width) / 2f;
        float heightOffset = (getHeight() - height) / 2f;
        graphics.setColor(Color.blue);
        graphics.fillRect((int) widthOffset, (int) heightOffset, width, height);
        synchronized (lights) {
            lights.forEach((light) -> {
                light.draw(graphics);
            });
        }

        synchronized (cars) {
            cars.forEach((car) -> {
                car.draw(graphics);
            });
        }		
    }
}
