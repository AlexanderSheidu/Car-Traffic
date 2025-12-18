package traffic;

import java.util.ArrayList;

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

// TrafficManager class that defines the way the traffic is managed

public class TrafficManager implements Runnable {
    private final ArrayList<TrafficLight> lights = new ArrayList<>();
    private final ArrayList<Car> cars = new ArrayList<>();

    private final int width;
    private final int height;
    private final long deltaMilliseconds;
    private boolean isPaused;
    private boolean isRunning;

    public TrafficManager(int width, int height, long deltaMilliseconds) {
        this.width = width;
        this.height = height;
        this.deltaMilliseconds = deltaMilliseconds;
        /*
        this.isPaused = false;
        this.isRunning = false;
        addLights((int) Math.round(Math.random() * 3) + 2);
        addCars((int) Math.round(Math.random() * 3) + 2); 
        */
    }

    public ArrayList<TrafficLight> getLights() {
        return lights;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }	

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void start() {
        this.isPaused = false;
        this.isRunning = true;
        addLights((int) Math.round(Math.random() * 3) + 2);
        addCars((int) Math.round(Math.random() * 3) + 2);      
        lights.forEach(TrafficLight::start);
        // cars.forEach(Car::resume);
    }

    public void pause() {
        isPaused = true;
        isRunning = false;
        lights.forEach(TrafficLight::pause);
        cars.forEach(Car::pause);
    }

    public void contin() {
        isPaused = false;
        isRunning = true;
        lights.forEach(TrafficLight::contin);
        cars.forEach(Car::resume);
    }

    public void stop() {
        isPaused = false;
        isRunning = false;
        lights.forEach(TrafficLight::stop);
        cars.forEach(Car::pause);
        lights.clear();
        cars.clear();
    }

    private void addLights(int n) {
        synchronized (lights) {
            for (int i=0; i<n; i++) {
                TrafficLight light = new TrafficLight(9, (i+1) * width/(n+1), 20);
                lights.add(light); 
            }
        }
    }

    private void addCars(int n) {
        synchronized (cars) {
            for (int i=0; i<n; i++) {
                Car car = new Car(i, 26, (i+1) * width/(n+1), 60);
                cars.add(car); 
            }
        }
    }	

    private void move() {
        if (isRunning && !isPaused) {
            synchronized(cars) {
                for (Car car : cars) {
                    car.updateDistanceAndSpeed(lights);
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                move();
                Thread.sleep(deltaMilliseconds);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
