package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.blocks.BlockUtil;
import cc.vergence.util.blocks.FixedBlockPos;
import cc.vergence.util.combat.CombatUtil;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class Defender extends Module {
    public static Defender INSTANCE;
    private final FastTimerUtil timer = new FastTimerUtil();
    private final List<BlockPos> blocks = new ArrayList<>();
    private final List<BlockPos> placePos = new ArrayList<>();
    private int progress = 0;
    private BlockPos playerBlockPos;

    public Defender() {
        super("Defender", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Double> placeDelay = addOption(new DoubleOption("PlaceDelay", 0, 500, 50));
    public Option<Double> multiPlace = addOption(new DoubleOption("MultiPlace", 1, 8, 1));
    public Option<Boolean> doRotate = addOption(new BooleanOption("Rotate", true));
    public Option<Enum<?>> rotateMode = addOption(new EnumOption("RotateMode", RotateModes.Server, v -> doRotate.getValue()));
    public Option<Boolean> inventorySwap = addOption(new BooleanOption("InventorySwap", true));
    public Option<Boolean> antiCrystal = addOption(new BooleanOption("AntiCrystal", true));
    public Option<Boolean> whileEating = addOption(new BooleanOption("WhileEating", true));
    public Option<Boolean> whileMining = addOption(new BooleanOption("WhileMining", true));
    public Option<Boolean> forFace = addOption(new BooleanOption("Face", false));
    public Option<Boolean> forBottom = addOption(new BooleanOption("Bottom", false));
    public Option<Boolean> burrow = addOption(new BooleanOption("Burrow", false));
    public Option<Boolean> inAir = addOption(new BooleanOption("InAir", false));
    public Option<Boolean> onlySurround = addOption(new BooleanOption("OnlySurround", false));
    public Option<Boolean> bevelCev = addOption(new BooleanOption("BevelCev", false));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        blocks.clear();
        if (!timer.passedMs(placeDelay.getValue())) {
            return;
        }
        if (!whileEating.getValue() && mc.player.isUsingItem()) {
            return;
        }
        progress = 0;
        if (playerBlockPos != null && !playerBlockPos.equals(EntityUtil.getPlayerPos(true))) {
            placePos.clear();
        }
        playerBlockPos = EntityUtil.getPlayerPos(true);
        double[] offset = new double[]{AntiCheat.getHitboxOffset(), -AntiCheat.getHitboxOffset(), 0};
        if (bevelCev.getValue()) {
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN) continue;
                if (isBedrock(playerBlockPos.offset(i).up())) continue;

                BlockPos blockerPos = playerBlockPos.offset(i).up(2);
                if (crystalHere(blockerPos) && !placePos.contains(blockerPos)) {
                    placePos.add(blockerPos);
                }
            }
        }
        if (forFace.getValue()) {
            for (double x : offset) {
                for (double z : offset) {
                    for (Direction i : Direction.values()) {
                        BlockPos blockerPos = new FixedBlockPos(mc.player.getX() + x, mc.player.getY() + 0.5, mc.player.getZ() + z).offset(i).up();
                        if (crystalHere(blockerPos) && !placePos.contains(blockerPos)) {
                            placePos.add(blockerPos);
                        }
                    }
                }
            }
            for (Direction i : Direction.values()) {
                if (i == Direction.DOWN) continue;
                if (isBedrock(playerBlockPos.offset(i).up())) continue;

                BlockPos blockerPos = playerBlockPos.offset(i).up(2);
                if (crystalHere(blockerPos) && !placePos.contains(blockerPos)) {
                    placePos.add(blockerPos);
                }
            }
        }
        if (getObsidian() == -1) {
            return;
        }

        if (!inAir.getValue() && !mc.player.isOnGround()) return;
        placePos.removeIf((pos) -> !BlockUtil.clientCanPlace(pos, true));
        if (burrow.getValue()) {
            for (double x : offset) {
                for (double z : offset) {
                    BlockPos surroundPos = new FixedBlockPos(mc.player.getX() + x, mc.player.getY(), mc.player.getZ() + z);
                    if (isBedrock(surroundPos)) continue;
                    if (Vergence.MINE.isMining(surroundPos)) {
                        for (Direction direction : Direction.values()) {
                            if (direction == Direction.DOWN || direction == Direction.UP) continue;
                            BlockPos defensePos = surroundPos.offset(direction);
                            if (!whileMining.getValue() && Vergence.MINE.isMining(defensePos)) {
                                continue;
                            }
                            if (antiCrystal.getValue()) {
                                CombatUtil.attackCrystal(defensePos, doRotate.getValue(), getPriority(), ((RotateModes) rotateMode.getValue()), false);
                            }
                            if (BlockUtil.canPlace(defensePos, 6, antiCrystal.getValue())) {
                                tryPlaceObsidian(defensePos);
                            }
                        }
                    }
                }
            }
        }
        if (forBottom.getValue() && (!onlySurround.getValue() || Surround.INSTANCE.getStatus())) {
            for (double x : offset) {
                for (double z : offset) {
                    for (Direction i : Direction.values()) {
                        BlockPos surroundPos = new FixedBlockPos(mc.player.getX() + x, mc.player.getY() + 0.5, mc.player.getZ() + z).offset(i);
                        if (isBedrock(surroundPos)) continue;
                        if (Vergence.MINE.isMining(surroundPos)) {
                            for (Direction direction : Direction.values()) {
                                if (direction == Direction.DOWN || direction == Direction.UP) continue;
                                BlockPos defensePos = surroundPos.offset(direction);
                                if (!whileMining.getValue() && Vergence.MINE.isMining(defensePos)) {
                                    continue;
                                }
                                if (antiCrystal.getValue()) {
                                    CombatUtil.attackCrystal(defensePos, doRotate.getValue(), getPriority(), ((RotateModes) rotateMode.getValue()), false);
                                }
                                if (BlockUtil.canPlace(defensePos, 6, antiCrystal.getValue())) {
                                    tryPlaceObsidian(defensePos);
                                }
                            }
                            BlockPos defensePos = surroundPos.up();
                            if (!whileMining.getValue() && Vergence.MINE.isMining(defensePos)) {
                                continue;
                            }
                            if (antiCrystal.getValue()) {
                                CombatUtil.attackCrystal(defensePos, doRotate.getValue(), getPriority(), ((RotateModes) rotateMode.getValue()), false);
                            }
                            if (BlockUtil.canPlace(defensePos, 6, antiCrystal.getValue())) {
                                tryPlaceObsidian(defensePos);
                            }
                        }
                    }
                }
            }
        }

        for (BlockPos defensePos : placePos) {
            if (antiCrystal.getValue() && crystalHere(defensePos)) {
                CombatUtil.attackCrystal(defensePos, doRotate.getValue(), getPriority(), ((RotateModes) rotateMode.getValue()), false);
            }
            if (BlockUtil.canPlace(defensePos, 6, antiCrystal.getValue())) {
                tryPlaceObsidian(defensePos);
            }
        }
    }

    private boolean crystalHere(BlockPos pos) {
        return BlockUtil.getEndCrystals(new Box(pos)).stream().anyMatch(entity -> entity.getBlockPos().equals(pos));
    }

    private boolean isBedrock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;
    }

    private void tryPlaceObsidian(BlockPos pos) {
        if (blocks.contains(pos)) {
            return;
        }
        blocks.add(pos);
        if (!(progress < multiPlace.getValue())) return;
        if (!whileMining.getValue() && Vergence.MINE.isMining(pos)) {
            return;
        }
        int oldSlot = mc.player.getInventory().selectedSlot;
        int block;
        if ((block = getObsidian()) == -1) {
            return;
        }
        doSwap(block);
        EntityUtil.placeBlock(pos, doRotate.getValue(), getPriority(), ((RotateModes) rotateMode.getValue()));
        if (inventorySwap.getValue()) {
            doSwap(block);
            InventoryUtil.syncInventory();
        } else {
            doSwap(oldSlot);
        }
        progress++;
        timer.reset();
    }

    private void doSwap(int slot) {
        if (inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getObsidian() {
        if (inventorySwap.getValue()) {
            return InventoryUtil.findBlockInventorySlot(Blocks.OBSIDIAN);
        } else {
            return InventoryUtil.findBlock(Blocks.OBSIDIAN);
        }
    }
}
