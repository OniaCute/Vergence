package cc.vergence.modules.movement;

import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.modules.Module;
import cc.vergence.util.player.EntityUtil;

public class QuickStop extends Module {
    public static QuickStop INSTANCE;

    public QuickStop() {
        super("QuickStop", Category.MOVEMENT);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onMoveEvent(MoveEvent event, double x, double y, double z) {
        if (isNull()) {
            return ;
        }

        if (!mc.options.forwardKey.isPressed() && !mc.options.backKey.isPressed() && !mc.options.leftKey.isPressed() && !mc.options.rightKey.isPressed() && !EntityUtil.isFalling() && mc.player.isOnGround()) {
//            event.set(0, 0, 0);
//            mc.player.setVelocity(0, 0, 0);
            event.cancel();
        }
    }
}
