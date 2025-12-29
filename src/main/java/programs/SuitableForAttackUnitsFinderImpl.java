package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> result = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            if (row == null || row.isEmpty()) continue;

            Set<Integer> occupiedY = new HashSet<>();
            for (Unit unit : row) occupiedY.add(unit.getyCoordinate());

            for (Unit unit : row) {
                int y = unit.getyCoordinate();
                boolean isCovered;
                if (isLeftArmyTarget) // если атакует игрок - проверяем справа
                    isCovered = occupiedY.contains(y + 1);
                else // если компьютер - проверяем слева
                    isCovered = occupiedY.contains(y - 1);
                if (!isCovered) result.add(unit);
            }
        }
        return result;
    }
}

