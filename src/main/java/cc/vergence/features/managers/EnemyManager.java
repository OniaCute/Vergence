package cc.vergence.features.managers;

import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class EnemyManager implements Wrapper {
    public ArrayList<String> enemyList = new ArrayList<>();

    public boolean isEnemy(String name) {
        return enemyList.contains(name);
    }

    public void removeEnemy(String name) {
        enemyList.remove(name);
    }

    public void addEnemy(String name) {
        if (!enemyList.contains(name)) {
            enemyList.add(name);
        }
    }

    public void resetEnemy() {
        enemyList.clear();
    }

    public void toggleEnemy(String name) {
        if (enemyList.contains(name)) {
            removeEnemy(name);
        } else {
            addEnemy(name);
        }
    }

    public boolean isEnemy(PlayerEntity entity) {
        return isEnemy(entity.getName().getString());
    }
}
