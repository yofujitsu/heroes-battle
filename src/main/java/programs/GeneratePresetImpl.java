package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GeneratePresetImpl implements GeneratePreset {

    private static final int MAX_UNITS_PER_TYPE = 11;
    private static final int FIELD_WIDTH = 3;
    private static final int FIELD_HEIGHT = 21;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        List<Unit> resultUnits = new ArrayList<>();
        int currentPoints = 0;

        // сортируем шаблоны по эффективности (атака + здоровье / стоимость)
        List<Unit> sortedTemplates = new ArrayList<>(unitList);
        sortedTemplates.sort(Comparator.comparingDouble((Unit u) ->
                (double) (u.getBaseAttack() + u.getHealth()) / u.getCost()).reversed());

        // рандомно шаффлим все возможные координаты
        List<int[]> availableCells = new ArrayList<>();
        for (int x = 0; x < FIELD_WIDTH; ++x) {
            for (int y = 0; y < FIELD_HEIGHT; ++y) availableCells.add(new int[]{x, y});
        }
        Collections.shuffle(availableCells);

        int cellIndex = 0;

        for (Unit template : sortedTemplates) {
            int unitsAdded = 0;

            while (unitsAdded < MAX_UNITS_PER_TYPE &&
                    (currentPoints + template.getCost()) <= maxPoints &&
                    cellIndex < availableCells.size()) {

                int[] coord = availableCells.get(cellIndex);
                int x = coord[0];
                int y = coord[1];

                Unit newUnit = new Unit(
                        template.getUnitType() + " " + (unitsAdded + 1),
                        template.getUnitType(),
                        template.getHealth(),
                        template.getBaseAttack(),
                        template.getCost(),
                        template.getAttackType(),
                        template.getAttackBonuses(),
                        template.getDefenceBonuses(),
                        x,
                        y
                );

                resultUnits.add(newUnit);
                currentPoints += template.getCost();
                unitsAdded++;
                cellIndex++; // берем следующую уникальную случайную клетку
            }
        }

        return new Army(resultUnits);
    }
}