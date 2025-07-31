package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.event.events.DeathEvent;
import cc.vergence.features.event.events.EntitySpawnEvent;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.blocks.BlockUtil;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.player.InventoryUtil;
import cc.vergence.util.rotation.RotateUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Surround extends Module {
    public static Surround INSTANCE;
    private List<BlockPos> surround = new ArrayList<>();
    private List<BlockPos> placements = new ArrayList<>();
    private int blocksPlaced;
    private FastTimerUtil timer = new FastTimerUtil();
    private double prevY;

    public Surround() {
        super("Surround", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Double> placeRange = addOption(new DoubleOption("PlaceRange", 1, 10, 4));
    public Option<Double> placeDelay = addOption(new DoubleOption("PlaceDelay", 0, 1000, 50).setUnit("ms"));
    public Option<Double> multiPlace = addOption(new DoubleOption("MultiPlace", 1, 8, 1));
    public Option<Boolean> strictDirection = addOption(new BooleanOption("StrictDirection", true));
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
        if (isNull()) {
            return ;
        }
        if (center.getValue()) {
            double x = Math.floor(mc.player.getX()) + 0.5;
            double z = Math.floor(mc.player.getZ()) + 0.5;
            Vec3d motion = mc.player.getVelocity();
            mc.player.setVelocity((x - mc.player.getX()) / 2.0, motion.y, (z - mc.player.getZ()) / 2.0);
        }
        prevY = mc.player.getY();
        timer.reset();
    }

    @Override
    public void onDisable() {
        timer.reset();
    }

    @Override
    public void onLogout() {
        if (autoDisable.getValue() && disableItems.getValue().contains(DisableItems.Death)) {
            this.disable();
        }
    }

    @Override
    public void onDeathEvent(DeathEvent event, PlayerEntity player) {
        if (player == mc.player && autoDisable.getValue() && disableItems.getValue().contains(DisableItems.Death)) {
            this.disable();
        }
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (isNull() || mc.player.isUsingItem() && !whileEating.getValue()) {
            return ;
        }

        blocksPlaced = 0; // reset

        if (autoDisable.getValue() && disableItems.getValue().contains(DisableItems.Jump) && Math.abs(mc.player.getY() - prevY) > 0.5) {
            disable();
            return;
        }
        BlockPos pos = EntityUtil.getRoundedBlockPos(mc.player.getX(), mc.player.getY(), mc.player.getZ());
        if (!timer.passedMs(placeDelay.getValue())) {
            return;
        }
        final int slot = BlockUtil.getDefensiveBlockItem();
        if (slot == -1) {
            return;
        }

        surround = getSurroundPositions(pos);
        placements = surround.stream().filter(blockPos -> mc.world.getBlockState(blockPos).isReplaceable()).toList();
        if (placements.isEmpty()) {
            return;
        }
        final int shiftTicks = multiPlace.getValue().intValue();
        while (blocksPlaced < shiftTicks && !placements.isEmpty()) {
            if (blocksPlaced >= placements.size()) {
                break;
            }
            BlockPos targetPos = placements.get(blocksPlaced);
            blocksPlaced++;
            timer.reset();
            attackPlace(targetPos, slot);
        }
    }

    private void attack(Entity entity) {
        Vergence.NETWORK.sendPacket(PlayerInteractEntityC2SPacket.attack(entity, mc.player.isSneaking()));
        Vergence.NETWORK.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
    }

    private void attackPlace(BlockPos targetPos) {
        final int slot = BlockUtil.getDefensiveBlockItem();
        if (slot == -1)
        {
            return;
        }
        attackPlace(targetPos, slot);
    }

    private void attackPlace(BlockPos targetPos, int slot) {
        if (antiCrystal.getValue()) {
            List<Entity> entities = mc.world.getOtherEntities(null, new Box(targetPos)).stream().filter(e -> e instanceof EndCrystalEntity).toList();
            for (Entity entity : entities) {
                attack(entity);
            }
        }

        EntityUtil.placeBlock(targetPos, slot, strictDirection.getValue(), false, (state, angles) -> {
            if (doRotate.getValue()) {
                if (state) {
                    Vergence.ROTATE.setRotationSilent(angles[0], angles[1], ((RotateModes) rotateMode.getValue()));
                } else {
                    Vergence.ROTATE.setRotationSilentSync(((RotateModes) rotateMode.getValue()));
                }
            }
        });
    }

    public List<BlockPos> getSurroundPositions(BlockPos pos) {
        List<BlockPos> entities = getSurroundEntities(pos);
        List<BlockPos> blocks = new CopyOnWriteArrayList<>();
        for (BlockPos epos : entities) {
            for (Direction dir2 : Direction.values()) {
                if (!dir2.getAxis().isHorizontal()) {
                    continue;
                }
                BlockPos pos2 = epos.add(dir2.getVector());
                if (entities.contains(pos2) || blocks.contains(pos2)) {
                    continue;
                }
                double dist = mc.player.squaredDistanceTo(pos2.toCenterPos());
                if (dist > placeRange.getValue() * placeRange.getValue()) {
                    continue;
                }
                blocks.add(pos2);
            }
        }
        if (doSupport.getValue()) {
            for (BlockPos block : blocks) {
                Direction direction = RotateUtil.getInteractDirection(block, strictDirection.getValue());
                if (direction == null) {
                    blocks.add(block.down());
                }
            }
        }
        for (BlockPos entityPos : entities) {
            if (entityPos == pos) {
                continue;
            }
            blocks.add(entityPos.down());
        }
        Collections.reverse(blocks);
        return blocks;
    }

    public List<BlockPos> getSurroundEntities(Entity entity) {
        List<BlockPos> entities = new LinkedList<>();
        entities.add(entity.getBlockPos());
        if (expansion.getValue()) {
            for (Direction dir : Direction.values()) {
                if (!dir.getAxis().isHorizontal()) {
                    continue;
                }
                entities.addAll(EntityUtil.getAllInBox(entity.getBoundingBox(), entity.getBlockPos()));
            }
        }
        return entities;
    }

    public List<BlockPos> getSurroundEntities(BlockPos pos) {
        List<BlockPos> entities = new LinkedList<>();
        entities.add(pos);
        if (expansion.getValue()) {
            for (Direction dir : Direction.values()) {
                if (!dir.getAxis().isHorizontal()) {
                    continue;
                }
                BlockPos pos1 = pos.add(dir.getVector());
                List<Entity> box = mc.world.getOtherEntities(null, new Box(pos1))
                        .stream().filter(e -> !isEntityBlockingSurround(e)).toList();
                if (box.isEmpty()) {
                    continue;
                }
                for (Entity entity : box) {
                    entities.addAll(EntityUtil.getAllInBox(entity.getBoundingBox(), pos));
                }
            }
        }
        return entities;
    }

    public List<BlockPos> getEntitySurroundNoSupport(Entity entity) {
        List<BlockPos> entities = getSurroundEntities(entity);
        List<BlockPos> blocks = new CopyOnWriteArrayList<>();
        for (BlockPos epos : entities) {
            for (Direction dir2 : Direction.values()) {
                if (!dir2.getAxis().isHorizontal()) {
                    continue;
                }
                BlockPos pos2 = epos.add(dir2.getVector());
                if (entities.contains(pos2) || blocks.contains(pos2)) {
                    continue;
                }
                double dist = mc.player.squaredDistanceTo(pos2.toCenterPos());
                if (dist > placeRange.getValue() * placeRange.getValue()) {
                    continue;
                }
                blocks.add(pos2);
            }
        }
        return blocks;
    }

    private boolean isEntityBlockingSurround(Entity entity) {
        return entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity
                || (entity instanceof EndCrystalEntity && antiCrystal.getValue());
    }

    @Override
    public void onEntitySpawn(EntitySpawnEvent event, Entity entity) {
        if (!(event.getEntity() instanceof EndCrystalEntity crystalEntity) || !antiCrystal.getValue()) {
            return;
        }
        for (BlockPos blockPos : surround) {
            if (crystalEntity.getBlockPos() == blockPos) {
                Vergence.NETWORK.sendPacket(PlayerInteractEntityC2SPacket.attack(crystalEntity, mc.player.isSneaking()));
                Vergence.NETWORK.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                break;
            }
        }
    }

    @Override
    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
        if (mc.player == null) {
            return;
        }
        if (packet instanceof BlockUpdateS2CPacket packet1) {
            final BlockState state = packet1.getState();
            final BlockPos targetPos = packet1.getPos();
            if (surround.contains(targetPos) && state.isReplaceable()) {
                blocksPlaced++;
                RenderSystem.recordRenderCall(() -> attackPlace(targetPos));
            }
        } else if (packet instanceof PlaySoundS2CPacket packet1
                && packet1.getCategory() == SoundCategory.BLOCKS
                && packet1.getSound().value() == SoundEvents.ENTITY_GENERIC_EXPLODE.value()) {
            BlockPos targetPos = BlockPos.ofFloored(packet1.getX(), packet1.getY(), packet1.getZ());
            if (surround.contains(targetPos)) {
                blocksPlaced++;
                RenderSystem.recordRenderCall(() -> attackPlace(targetPos));
            }
        }
    }

    public enum DisableItems {
        Jump,
        Leave,
        Death,
        Move
    }
}
