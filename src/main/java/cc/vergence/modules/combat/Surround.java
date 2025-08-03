package cc.vergence.modules.combat;

import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.features.event.events.PlayerJumpEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.blocks.BlockUtil;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.player.MovementUtil;
import cc.vergence.util.rotation.RotateUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class Surround extends Module {
    public static Surround INSTANCE;
    private FastTimerUtil timer = new FastTimerUtil();
    private boolean moved = false;

    public Surround() {
        super("Surround", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Double> placeRange = addOption(new DoubleOption("PlaceRange", 1, 10, 4));
    public Option<Double> placeDelay = addOption(new DoubleOption("PlaceDelay", 0, 1000, 50).setUnit("ms"));
    public Option<Double> multiPlace = addOption(new DoubleOption("MultiPlace", 1, 8, 1));
    public Option<Boolean> doRotate = addOption(new BooleanOption("Rotate", true));
    public Option<Enum<?>> rotateMode = addOption(new EnumOption("RotateMode", RotateModes.Client, v -> doRotate.getValue()));
    public Option<Boolean> whileEating = addOption(new BooleanOption("WhileEating", true));
    public Option<Boolean> antiCrystal = addOption(new BooleanOption("AntiCrystal", true));
    public Option<Boolean> center = addOption(new BooleanOption("ToCenter", true));
    public Option<Boolean> expansion = addOption(new BooleanOption("Expansion", true));
    public Option<Boolean> doSupport = addOption(new BooleanOption("Support", true));
    public Option<Boolean> autoDisable = addOption(new BooleanOption("AutoDisable", false));
    public Option<EnumSet<DisableItems>> disableItems = addOption(new MultipleOption<DisableItems>("DisableItems", EnumSet.of(DisableItems.Jump, DisableItems.Leave, DisableItems.Death), v -> autoDisable.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onEnable() {
        moved = false;
    }

    @Override
    public void onPlayerJump(PlayerJumpEvent event) {
        if (event.isCancelled()) {
            return ;
        }

        if (autoDisable.getValue() && disableItems.getValue().contains(DisableItems.Jump)) {
            this.disable();
        }
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (isNull()) {
            return ;
        }
        if (getSurroundPos().isEmpty() && autoDisable.getValue() && disableItems.getValue().contains(DisableItems.Place)) {
            this.disable();
        }

        for (Direction direction : Direction.values()) {
            BlockPos blockPos = mc.player.getBlockPos().offset(direction, 1);


        }
    }

    @Override
    public void onMoveEvent(MoveEvent event, double x, double y, double z) {
        if (isNull() || !center.getValue() || mc.player.isGliding()) {
            return;
        }

        BlockPos blockPos = EntityUtil.getPlayerPos(true);
        if (mc.player.getX() - blockPos.getX() - 0.5 <= 0.2 && mc.player.getX() - blockPos.getX() - 0.5 >= -0.2 && mc.player.getZ() - blockPos.getZ() - 0.5 <= 0.2 && mc.player.getZ() - 0.5 - blockPos.getZ() >= -0.2) {
            if (center.getValue() && (mc.player.isOnGround() || MovementUtil.isMoving())) {
                event.setX(0);
                event.setZ(0);
                moved = false;
            }
        } else {
            if (moved) {
                Vec3d centerPos = EntityUtil.getPlayerPos(true).toCenterPos();
                float rotation = RotateUtil.getRotationTo(mc.player.getPos(), centerPos).x;
                float yawRad = rotation / 180.0f * 3.1415927f;
                double dist = mc.player.getPos().distanceTo(new Vec3d(centerPos.x, mc.player.getY(), centerPos.z));
                double cappedSpeed = Math.min(0.2873, dist);
                double x1 = -(float) Math.sin(yawRad) * cappedSpeed;
                double z1 = (float) Math.cos(yawRad) * cappedSpeed;
                event.setX(x1);
                event.setZ(z1);
            }
        }
    }

    private ArrayList<BlockPos> getSurroundPos() {
        ArrayList<BlockPos> surroundPos = new ArrayList<>();
        BlockPos playerPos = mc.player.getBlockPos();

        for (Direction direction : Direction.values()) {
            if (direction.equals(Direction.UP)) {
                continue ;
            }
            if (!BlockUtil.hasEntity(playerPos.offset(direction, 1), antiCrystal.getValue()) && BlockUtil.isReplaceable(playerPos.offset(direction, 1))) {
                surroundPos.add(playerPos.offset(direction, 1));
            }
        }

        return surroundPos;
    }

    public enum DisableItems {
        Jump,
        Leave,
        Death,
        Place,
        Move
    }
}
