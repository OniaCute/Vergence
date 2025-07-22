package cc.vergence.modules.movement;

import cc.vergence.modules.Module;

public class Parkour extends Module {
    public static Parkour INSTANCE;
    private boolean jumping = false;

    public Parkour() {
        super("Parkour", Category.MOVEMENT);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }

        if (mc.player.isOnGround() && !mc.player.isSneaking() && mc.world.isSpaceEmpty(mc.player.getBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001))) {
            mc.options.jumpKey.setPressed(true);
            jumping = true;
        } else if (jumping) {
            jumping = false;
            mc.options.jumpKey.setPressed(false);
        }
    }

    @Override
    public void onDisable() {
        if (isNull()) {
            return ;
        }

        if (jumping) {
            mc.options.jumpKey.setPressed(false);
            jumping = false;
        }
    }
}
