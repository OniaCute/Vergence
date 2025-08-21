package cc.vergence.modules.player;

import cc.vergence.Vergence;
import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class MiddleClickExpansion extends Module {
    public static MiddleClickExpansion INSTANCE;

    public MiddleClickExpansion() {
        super("MiddleClickExpansion", Category.PLAYER);
    }

    public Option<Enum<?>> action = addOption(new EnumOption("Action", Actions.Friend));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onMouseActive(int button, int act) {
        if (button == -102 && act == 1) {
            if (action.getValue().equals(Actions.Friend)) {
                friendAction();
            }
            else if (action.getValue().equals(Actions.Enemy)) {
                enemyAction();
            }
        }
    }

    private void friendAction() {
        HitResult target = mc.crosshairTarget;
        if (target != null) {
            if (target.getType() == HitResult.Type.ENTITY) {
                EntityHitResult newTarget = (EntityHitResult) target;
                if (((EntityHitResult) target).getEntity() instanceof PlayerEntity) {
                    if (!Vergence.FRIEND.isFriend(((EntityHitResult) newTarget).getEntity().getName().getString())) {
                        Vergence.FRIEND.addFriend(((EntityHitResult) newTarget).getEntity().getName().getString());
                        NotifyManager.newNotification(this, Vergence.TEXT.get("COMMANDS.MESSAGE.FRIEND.ADD"));
                    } else {
                        Vergence.FRIEND.removeFriend(((EntityHitResult) newTarget).getEntity().getName().getString());
                        NotifyManager.newNotification(this, Vergence.TEXT.get("COMMANDS.MESSAGE.FRIEND.REMOVE"));
                    }
                }
            }
        }
    }

    private void enemyAction() { // It sounds stupid. You need to get close to your enemy and then mark him as "Enemy".
        HitResult target = mc.crosshairTarget;
        if (target != null) {
            if (target.getType() == HitResult.Type.ENTITY) {
                EntityHitResult newTarget = (EntityHitResult) target;
                if (((EntityHitResult) target).getEntity() instanceof PlayerEntity) {
                    if (!Vergence.ENEMY.isEnemy(((EntityHitResult) newTarget).getEntity().getName().getString())) {
                        Vergence.ENEMY.addEnemy(((EntityHitResult) newTarget).getEntity().getName().getString());
                        NotifyManager.newNotification(this, Vergence.TEXT.get("COMMANDS.MESSAGE.ENEMY.ADD"));
                    } else {
                        Vergence.ENEMY.removeEnemy(((EntityHitResult) newTarget).getEntity().getName().getString());
                        NotifyManager.newNotification(this, Vergence.TEXT.get("COMMANDS.MESSAGE.ENEMY.REMOVE"));
                    }
                }
            }
        }
    }

    public enum Actions {
        Friend,
        Enemy,
        EndPearl
    }
}
