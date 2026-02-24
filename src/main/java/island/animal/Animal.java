package island.animal;

import island.model.Island;
import island.model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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
    protected volatile Location currentLocation; //текущее местонахождение животного

    protected Map<Class<? extends Animal>, Integer> eatingProbabilities;

    public Animal(double weight, double maxSatiety) {
        this.weight = weight;
        this.maxSatiety = maxSatiety;
        this.currentSatiety = maxSatiety;
    }

    public abstract void eat(Location location);

    public void move(Island island, int currentX, int currentY) {
        if (!alive) {
            return;
        }
        if (currentLocation == null) {
            log.warn("Животное {} не имеет текущей локации. Перемещение не возможно", this);
            return;
        }
        int direction = ThreadLocalRandom.current().nextInt(4);
        int newX = currentX;
        int newY = currentY;

        //0..3
        switch (direction) {
            case 0:
                //вверх Y
                newY = Math.max(0, currentY - 1);
                break;
            case 1:
                //вправо X
                newX = Math.min(island.getWidth() - 1, currentX + 1);
                break;
            case 2:
                //вниз Y
                newY = Math.min(island.getHeight() - 1, currentY + 1);
                break;
            case 3:
                //влево X
                newX = Math.max(0, currentX - 1);
                break;
        }
    };

    public abstract void reproduce(Location location);

    public void die() {
        this.alive = false;
    };



}
