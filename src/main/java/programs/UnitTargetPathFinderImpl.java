package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        boolean[][] blocked = new boolean[WIDTH][HEIGHT];

        for (Unit unit : existingUnitList) {
            if (!unit.isAlive()) continue;

            if (unit == attackUnit) continue;
            if (unit == targetUnit) continue;

            blocked[unit.getxCoordinate()][unit.getyCoordinate()] = true;
        }

        int[][] distance = new int[WIDTH][HEIGHT];
        for (int[] row : distance) Arrays.fill(row, Integer.MAX_VALUE);

        Edge[][] parent = new Edge[WIDTH][HEIGHT];

        PriorityQueue<EdgeDistance> pq = new PriorityQueue<>(Comparator.comparingInt(EdgeDistance::getDistance));

        distance[startX][startY] = 0;
        pq.add(new EdgeDistance(startX, startY, 0));

        while (!pq.isEmpty()) {
            EdgeDistance current = pq.poll();
            int x = current.getX();
            int y = current.getY();

            if (x == targetX && y == targetY) break;

            for (int[] d : DIRECTIONS) {
                int nx = x + d[0];
                int ny = y + d[1];

                if (nx < 0 || ny < 0 || nx >= WIDTH || ny >= HEIGHT) continue;
                if (blocked[nx][ny]) continue;

                int newDist = distance[x][y] + 1;
                if (newDist < distance[nx][ny]) {
                    distance[nx][ny] = newDist;
                    parent[nx][ny] = new Edge(x, y);
                    pq.add(new EdgeDistance(nx, ny, newDist));
                }
            }
        }

        if (distance[targetX][targetY] == Integer.MAX_VALUE) return Collections.emptyList();
        List<Edge> path = new ArrayList<>();
        int cx = targetX;
        int cy = targetY;

        while (!(cx == startX && cy == startY)) {
            path.add(new Edge(cx, cy));
            Edge p = parent[cx][cy];
            cx = p.getX();
            cy = p.getY();
        }

        path.add(new Edge(startX, startY));
        Collections.reverse(path);

        return path;
    }
}