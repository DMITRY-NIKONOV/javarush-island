package island.animal;

import island.model.Island;
import island.model.Location;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Bear extends Animal{

    private final static double WEIGHT = 500;
    private final static double MAX_SATIETY = 80;

    public Bear() {
        super(WEIGHT, MAX_SATIETY);
    }

    @Override
    public void eat(Location location) {

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
