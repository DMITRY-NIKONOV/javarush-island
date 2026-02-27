package island;

import island.config.SimulationConfig;
import island.simulation.SimpleSimulation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    private static final int SIMPLE_SIMULATION_TICKS = 10;

    public static void main(String[] args) {

        //см аннотацию @Builder на классе SimulationConfig
        SimulationConfig config = SimulationConfig.builder()
                .islandWidth(5) //ширина острова
                .islandHeight(5) //высота острова
                .initialWolves(2)//популяция волков
                .initialRabbits(10)//популяция кроликов
                .initialDeer(5)//популяция оленей
                .plantsPerCell(1)//число растений на 1 ячейку
                .build();

        //Однопоточная симуляция
        SimpleSimulation simpleSimulation = new SimpleSimulation(config);
        simpleSimulation.initialize();

        //Выводим сконфигурированное состояние: err, info, debug
        log.info("Начальное состояние симуляции");
        simpleSimulation.printStatistics();

        try {
            simpleSimulation.run(SIMPLE_SIMULATION_TICKS);
        } catch (InterruptedException e) {
            log.error("Ошибка при работе simpleSimulation");
            throw new RuntimeException(e);
        }

        log.info("Работа симуляции завершена");
    }
}
