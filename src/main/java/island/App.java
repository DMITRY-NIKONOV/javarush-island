package island;

import island.config.SimulationConfig;
import island.simulation.MultithreadedSimulation;
import island.simulation.SimpleSimulation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    private static final int SIMPLE_SIMULATION_TICKS = 10;

    public static void main(String[] args) {

        //см аннотацию @Builder на классе SimulationConfig
        SimulationConfig config = SimulationConfig.builder()
                .islandWidth(10) //ширина острова
                .islandHeight(10) //высота острова
                .initialWolves(5)//популяция волков
                .initialRabbits(50)//популяция кроликов
                .initialDeer(20)//популяция оленей
                .plantsPerCell(3)//число растений на 1 ячейку
                .tickDurationMs(2000)
                .build();

        //Однопоточная симуляция
//        SimpleSimulation simpleSimulation = new SimpleSimulation(config);
//        simpleSimulation.initialize();

        //Выводим сконфигурированное состояние: err, info, debug
//        log.info("Начальное состояние симуляции");
//        simpleSimulation.printStatistics();
//
//        try {
//            simpleSimulation.run(SIMPLE_SIMULATION_TICKS);
//        } catch (InterruptedException e) {
//            log.error("Ошибка при работе simpleSimulation");
//            throw new RuntimeException(e);
//        }

        MultithreadedSimulation multithreadedSimulation = new MultithreadedSimulation(config);
        multithreadedSimulation.initialize();

        log.info("Изначальное состояние: ");
        multithreadedSimulation.printStatistics();

        multithreadedSimulation.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        multithreadedSimulation.stop();

        log.info("Работа симуляции завершена");
    }
}
