package island.config;

import lombok.Builder;
import lombok.Data;

/**
 * Конфигурация приложения
 */
@Data
@Builder
public class SimulationConfig {
    //Размеры острова
    private int islandWidth;
    private int islandHeight;
    //Популяции
    private int initialWolves;
    private int initialRabbits;
    private int initialDeer;
    //Кол-во растений, которые будут добавляться за 1н такт в каждую клетку
    private int plantsPerCell;
    //Дюрация
    private long tickDurationMs;
    //Вероятности

}


