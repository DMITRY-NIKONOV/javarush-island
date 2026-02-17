package island.animal;

import island.model.Island;
import island.model.Location;

import java.util.Map;

public abstract class Animal {

    protected double weight;
    protected int maxQuantity;
    protected int speed;
    protected double maxSatiety;
    protected double currentSatiety;
    protected boolean alive = true;

    protected Map<Class<? extends Animal>, Integer> eatingProbabilities;

    public Animal(double weight, double maxSatiety, double currentSatiety) {
        this.weight = weight;
        this.maxSatiety = maxSatiety;
        this.currentSatiety = currentSatiety;
    }

    public abstract void eat(Location location);

    public abstract void move(Island island, int currentX, int currentY);

    public abstract void reproduce(Location location);

    public  void die() {
        this.alive = false;
    };



}
