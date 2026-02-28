package island.simulation;

import island.animal.Animal;
import island.animal.Deer;
import island.animal.Rabbit;
import island.animal.Wolf;
import island.config.SimulationConfig;
import island.model.Island;
import island.model.Location;
import island.model.Plant;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class MultithreadedSimulation {
    private static final int CORE_POOL_SIZE = 1;
    private static final int THREADS = 10;

    private final Island island;
    private final SimulationConfig config;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
    private final ExecutorService workPool = Executors.newFixedThreadPool(THREADS);
    private volatile boolean running = true;

    public MultithreadedSimulation(SimulationConfig config) {
        this.island = new Island(config.getIslandWidth(), config.getIslandHeight());
        this.config = config;
    }

    public void initialize() {
        //волки
        for (int i = 0; i < config.getInitialWolves(); i++) {
            int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
            int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
            Wolf wolf = new Wolf();
            island.getLocation(x, y).addAnimal(wolf);
        }
        //кролики
        for (int i = 0; i < config.getInitialRabbits(); i++) {
            int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
            int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
            Rabbit rabbit = new Rabbit();
            island.getLocation(x, y).addAnimal(rabbit);
        }
            //олени
            for (int i = 0; i < config.getInitialDeer(); i++) {
                int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
                int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
                Deer deer = new Deer();
                island.getLocation(x, y).addAnimal(deer);
            }
                //растения
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location location = island.getLocation(x, y);
                for (int p = 0; p < 5; p++) { //todo вынести в настройки
                    location.addPlant(new Plant());
                }
            }
        }
        log.info("Инициализация завершена. Животные и растения размещены.");
    }

    private void tick() {
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location location = island.getLocation(x, y);
                for (int i = 0; i < config.getPlantsPerCell(); i++) {
                    location.addPlant(new Plant());
                }
            }
        }
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location location = island.getLocation(x, y);
                int finalX = x;
                int finalY = y;
                for (Animal animal : location.getAnimals()) {
                    if (!animal.isAlive()) {
                        continue;
                    }
                    tasks.add(() -> {
                        animal.eat(animal.getCurrentLocation());
                        animal.move(island, finalX, finalY);
                        animal.reproduce(animal.getCurrentLocation());
                        animal.setCurrentSatiety(animal.getCurrentSatiety() - 1);
                        if (animal.getCurrentSatiety() <= 0) {
                            animal.die();
                            animal.getCurrentLocation().removeAnimal(animal);
                        }
                        return null;
                    });
                }
            }
        }
        try {
            List<Future<Void>> futures = workPool.invokeAll(tasks);//отправляем действия в пул рабочих потоков
            for (Future<Void> f : futures) {
                f.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Такт прерван");
        } catch (ExecutionException e) {
            log.error("Ошибка при выполнении задачи животного", e.getCause());
        }

        printStatistics();
    }
    public void printStatistics() {
        int wolves = 0;
        int rabbits = 0;
        int deer = 0;
        int plants = 0;
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location location = island.getLocation(x, y);
                for (Animal animal : location.getAnimals()) {
                    if (animal instanceof Wolf) wolves++;
                    else if (animal instanceof Rabbit) rabbits++;
                    else if (animal instanceof Deer) deer++;
                }
                plants += location.getPlants().size();
                }
            }
            log.info("Статистика: Волки={}, Кролики={}, Олени={}, Растения={}", wolves, rabbits, deer, plants);
        }

    public void start() {
        scheduler.scheduleAtFixedRate(()-> {
            if (running) {
                tick();
            }
        }, 0, config.getTickDurationMs(), TimeUnit.MILLISECONDS);
        log.info("Симуляция запущена с тактом {} мс", config.getTickDurationMs());
    }

    public void stop() {
        running = false;
        scheduler.shutdown();
        workPool.shutdown();
        log.info("Симуляция остановлена!");
    }
    }



