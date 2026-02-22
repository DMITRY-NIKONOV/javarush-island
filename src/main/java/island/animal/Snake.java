package island.animal;

import island.model.Island;
import island.model.Location;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Snake extends Animal{

    private final static double WEIGHT = 15;
    private final static double MAX_SATIETY = 3;

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
