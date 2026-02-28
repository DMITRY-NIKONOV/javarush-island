package island.animal;

import island.model.Island;
import island.model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@Slf4j
public abstract class Animal {

    private static final int CHANCE_OF_REPRODUCTION = 30;//шанс размножения
    protected double weight;
    protected int maxQuantity;
    protected int speed;
    protected double maxSatiety;
    protected double currentSatiety;
    protected boolean alive = true;
    //volatile гарантирует, что все потоки увидят актуальное значение
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
    }

    /**
     * Многопоточный метод reproduce()
     * @param location
     */
    public void reproduce(Location location) {
        if (!alive) {
            return;
        }
        //Подсчет особей того же вида (фильтруем только живых и того же класса, кроме самого себя)
        long sameSpeciesCount = location.getAnimals().stream()
                .filter(a -> a.getClass() == this.getClass() && a != this && a.isAlive()) //промежуточная
                .count(); //терминальная
        //Условие для размножения животных: наличие хотя бы одной особи того же вида (sameSpeciesCount), шанс размножения
        if (sameSpeciesCount > 0 && ThreadLocalRandom.current().nextInt(100) < CHANCE_OF_REPRODUCTION) {
            try {
                //Создание потомка через рефлексию (не требуется знание конкретного класса во время компиляции)
                Animal baby = this.getClass().getDeclaredConstructor().newInstance();
                baby.setCurrentSatiety(baby.getMaxSatiety() / 2);//установка начальной сытости, половина от максимальной для животного
                location.addAnimal(baby);//родившееся животное добавляем в локацию
                log.debug("Родилось животное {}", baby.getClass().getSimpleName());

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("Ошибка при создании нового животного!");
                throw new RuntimeException(e);
            }
        }
    }

    public void die() {
        this.alive = false;
    }
}
