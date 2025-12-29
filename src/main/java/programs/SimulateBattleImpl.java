package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {

        while (hasAlive(playerArmy) && hasAlive(computerArmy)) {

            List<Unit> currentTurn = new ArrayList<>();
            addAliveUnits(currentTurn, playerArmy);
            addAliveUnits(currentTurn, computerArmy);

            currentTurn.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            Iterator<Unit> iterator = currentTurn.iterator();

            while (iterator.hasNext()) {
                Unit attacker = iterator.next();
                if (!attacker.isAlive()) continue;

                Army enemyArmy = isPlayerUnit(attacker) ? computerArmy : playerArmy;
                if (!hasAlive(enemyArmy)) break;

                Unit target = attacker.getProgram().attack();
                if (target == null || !target.isAlive()) continue;
                target.getProgram().attack();
                if (target.isAlive()) printBattleLog.printBattleLog(attacker, target);
                // если цель умерла, то она больше не должна ходить в этом раунде
                if (!target.isAlive()) iterator.remove();
            }
        }
    }

    private boolean hasAlive(Army army) {
        return army.getUnits().stream().anyMatch(Unit::isAlive);
    }

    private void addAliveUnits(List<Unit> queue, Army army) {
        for (Unit unit : army.getUnits()) if (unit.isAlive()) queue.add(unit);
    }

    private boolean isPlayerUnit(Unit unit) {
        return unit.getProgram().getClass().getSimpleName().startsWith("User");
    }
}