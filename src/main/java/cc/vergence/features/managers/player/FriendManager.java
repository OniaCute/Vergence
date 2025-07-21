package cc.vergence.features.managers.player;

import cc.vergence.Vergence;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class FriendManager implements Wrapper {
    public ArrayList<String> friendList = new ArrayList<>();

    public boolean isFriend(String name) {
        return friendList.contains(name);
    }

    public void removeFriend(String name) {
        friendList.remove(name);
    }

    public void addFriend(String name) {
        if (Vergence.ENEMY != null && Vergence.ENEMY.isEnemy(name)) {
            NotifyManager.newNotification("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.FRIEND.IS_ENEMY"));
            return;
        }
        if (!friendList.contains(name)) {
            friendList.add(name);
        }
    }

    public void resetFriend() {
        friendList.clear();
    }

    public void toggleFriend(String name) {
        if (friendList.contains(name)) {
            removeFriend(name);
        } else {
            addFriend(name);
        }
    }

    public boolean isFriend(PlayerEntity entity) {
        return isFriend(entity.getName().getString());
    }
}
