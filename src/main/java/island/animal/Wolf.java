package island.animal;

import island.model.Island;
import island.model.Location;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class Wolf extends Animal {

    private final static double WEIGHT = 50;
    private final static double MAX_SATIETY = 8;
    private final static Map<Class<? extends Animal>, Integer> EATING_PROBABILITIES = Map.of(Rabbit.class, 60, Deer.class, 80);  //todo заменить магические числа!

    public Wolf() {
        super(WEIGHT, MAX_SATIETY);
        this.eatingProbabilities = EATING_PROBABILITIES;
    }

    @Override
    public void eat(Location location) {

        if (!alive) {
            return;
        }
        for (Animal prey : location.getAnimals()) {
            if (prey == this || !prey.isAlive()) {

                continue;
            }
            Integer prob = eatingProbabilities.get(prey.getClass());
            if (prob != null && ThreadLocalRandom.current().nextInt(100) < prob) {
                location.removeAnimal(prey);
                prey.die();
                currentSatiety = Math.min(maxSatiety, currentSatiety + prey.getWeight());
                log.debug("Волк съел {}", prey.getClass().getSimpleName());
                break;
            }
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
