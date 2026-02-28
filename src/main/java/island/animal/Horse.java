package island.animal;

import island.model.Island;
import island.model.Location;
import island.model.Plant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Horse extends Animal {

    private final static double WEIGHT = 400;
    private final static double MAX_SATIETY = 60;

    @Override
    public void eat(Location location) {
        if (!alive) {
            return;
        }
        Plant plant = location.removePlant();
        if (plant != null) {
            currentSatiety = Math.min(maxSatiety, currentSatiety + plant.getWeight());
            log.debug("Лошадь съела растение");
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
