package island.animal;

import island.model.Island;
import island.model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@NoArgsConstructor
@Slf4j
public abstract class Animal {

    protected double weight;
    protected int maxQuantity;
    protected int speed;
    protected double maxSatiety;
    protected double currentSatiety;
    protected boolean alive = true;

    protected Map<Class<? extends Animal>, Integer> eatingProbabilities;

    public Animal(double weight, double maxSatiety) {
        this.weight = weight;
        this.maxSatiety = maxSatiety;
        this.currentSatiety = maxSatiety;
    }

    public abstract void eat(Location location);

    public abstract void move(Island island, int currentX, int currentY);

    public abstract void reproduce(Location location);

    public void die() {
        this.alive = false;
    };



}
