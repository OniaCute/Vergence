package cc.vergence.modules.visual;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.render.utils.Render3DUtil;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class Tracers extends Module {
    public static Tracers INSTANCE;

    public Tracers() {
        super("Tracers", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Boolean> antiBot = addOption(new BooleanOption("AntiBot", false));
    public Option<Double> range = addOption(new DoubleOption("Range", 1, 120, 80));
    public Option<Boolean> customColor = addOption(new BooleanOption("CustomColor", true));
    public Option<Color> color = addOption(new ColorOption("LineColor", new Color(255, 255, 255, 228), v -> customColor.getValue()));
    public Option<Color> friendColor = addOption(new ColorOption("FriendColor", new Color(56, 122, 255, 200), v -> customColor.getValue()));
    public Option<Color> enemyColor = addOption(new ColorOption("EnemyColor", new Color(255, 56, 56, 200), v -> customColor.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (isNull()) {
            return;
        }

        boolean prevBobView = mc.options.getBobView().getValue();
        mc.options.getBobView().setValue(false);

        Vec3d pos = EntityUtil.getRenderPos(mc.player, tickDelta);
        Camera camera = mc.gameRenderer.getCamera();
        Vec3d cameraPos = new Vec3d(0.0, 0.0, 1.0).rotateX(-(float) Math.toRadians(camera.getPitch())).rotateY(-(float) Math.toRadians(camera.getYaw())).add(pos.x, mc.player.getEyeHeight(mc.player.getPose()) + pos.y, pos.z);

        for(PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) {
                continue;
            }
            if (EntityUtil.isBot(player) && antiBot.getValue()) {
                continue;
            }
            Vec3d playerPos = EntityUtil.getRenderPos(player, tickDelta);
            Render3DUtil.renderLine(matrixStack, cameraPos, playerPos, getColor(player));
        }

        mc.options.getBobView().setValue(prevBobView);
    }

    private Color getColor(PlayerEntity player) {
        if (customColor.getValue()) {
            if (Vergence.FRIEND.isFriend(player.getName().getString())) {
                return friendColor.getValue();
            } else if (Vergence.ENEMY.isEnemy(player.getName().getString())) {
                return enemyColor.getValue();
            } else {
                return color.getValue();
            }
        }

        double maxDistance = range.getValue();
        double distance = MathHelper.clamp(mc.player.distanceTo(player), 0, maxDistance);
        return new Color((int) ((maxDistance - distance) / maxDistance), (int) (1.0f - (maxDistance - distance) / (float) maxDistance), 0, color.getValue().getAlpha()/255f);
    }
}
