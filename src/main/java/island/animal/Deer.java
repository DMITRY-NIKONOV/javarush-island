package island.animal;

import island.model.Island;
import island.model.Location;
import island.model.Plant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Deer extends Animal {

    private final static double WEIGHT = 300;
    private final static double MAX_SATIETY = 50;


    @Override
    public void eat(Location location) {
        if (!alive) {
            return;
        }
        Plant plant = location.removePlant();
        if (plant != null) {
            currentSatiety = Math.min(maxSatiety, currentSatiety + plant.getWeight());
            log.debug("Олень съел растение");
            
        }
    }

    @Override
    public void move(Island island, int currentX, int currentY) {
        //todo пока что заглушка

    }

    @Override
    public void reproduce(Location location) {
        //todo пока что заглушка

    }
}
