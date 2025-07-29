package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.event.events.EntitySpawnEvent;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.animations.SimpleAnimation;
import cc.vergence.util.combat.CrystalUtil;
import cc.vergence.util.data.EvictingQueue;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.player.InventoryUtil;
import cc.vergence.util.rotation.RotateUtil;
import cc.vergence.util.rotation.Rotation;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static cc.vergence.util.combat.CombatUtil.isValidTarget;

public class CrystalAura extends Module {
    public static CrystalAura INSTANCE;
    private DamageData<EndCrystalEntity> attackCrystal;
    private DamageData<BlockPos> placeCrystal;
    private BlockPos renderPos;

    private Vec3d crystalRotation;
    private boolean attackRotate;
    private boolean rotated;

    private static final Box FULL_CRYSTAL_BB = new Box(0.0, 0.0, 0.0, 1.0, 2.0, 1.0);
    private static final Box HALF_CRYSTAL_BB = new Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    private final FastTimerUtil lastAttackTimer = new FastTimerUtil();
    private final FastTimerUtil lastPlaceTimer = new FastTimerUtil();
    private final FastTimerUtil lastSwapTimer = new FastTimerUtil();
    private final FastTimerUtil autoSwapTimer = new FastTimerUtil();

    private final ArrayDeque<Long> attackLatency = new EvictingQueue<>(50);
    private final List<BlockPos> manualCrystals = new ArrayList<>();
    private final Map<Integer, Long> attackPackets = Collections.synchronizedMap(new ConcurrentHashMap<>());
    private final Map<BlockPos, Long> placePackets = Collections.synchronizedMap(new ConcurrentHashMap<>());

    private final Map<BlockPos, SimpleAnimation> fadeList = new HashMap<>();

    private int serverSlot = -1;

    public CrystalAura() {
        super("CrystalAura", Category.COMBAT);
        INSTANCE = this;
    }

    // Page Option
    public Option<Enum<?>> page = addOption(new EnumOption("Page", Pages.General));

    // Page - General
    // Page - General | Normally
    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<TargetTypes>("Targets", EnumSet.of(TargetTypes.EnemyPlayers, TargetTypes.Invisible), v -> page.getValue().equals(Pages.General)));
    public Option<Double> range = addOption(new DoubleOption("Range", 1, 14, 10, v -> page.getValue().equals(Pages.General)));
    public Option<Boolean> autoDisable = addOption(new BooleanOption("AutoDisable", false, v -> page.getValue().equals(Pages.General)));
    public Option<Double> maxYAxisOffset = addOption(new DoubleOption("MaxYAxisOffset", 1, 10, 5, v -> page.getValue().equals(Pages.General)));
    public Option<Double> maxPlayerDamage = addOption(new DoubleOption("MaxPlayerDamage", 1, 14, 10, v -> page.getValue().equals(Pages.General)));
    public Option<Double> minDamage = addOption(new DoubleOption("MinDamage", 1, 10, 4, v -> page.getValue().equals(Pages.General)));
    public Option<Boolean> multiTask = addOption(new BooleanOption("MultiTask", false, v -> page.getValue().equals(Pages.General)));
    public Option<Boolean> whileMining = addOption(new BooleanOption("WhileMining", false, v -> page.getValue().equals(Pages.General)));
    public Option<Double> swapDelay = addOption(new DoubleOption("SwapDelay", 1, 10, 0, v -> page.getValue().equals(Pages.General)));
    public Option<Boolean> blockDestruction = addOption(new BooleanOption("BlockDestruction", false, v -> page.getValue().equals(Pages.General)));
    // Page - General | Armor Destroyer
    public Option<Boolean> armorDestroyer = addOption(new BooleanOption("ArmorDestroyer", true, v -> page.getValue().equals(Pages.General)));
    public Option<Double> armorScale = addOption(new DoubleOption("ArmorScale", 1, 20, 5, v -> page.getValue().equals(Pages.General) && armorDestroyer.getValue()));
    // Page - General | Lethal Options
    public Option<Double> lethalMultiplier = addOption(new DoubleOption("LethalMultiplier", 1, 4, 1.5, v -> page.getValue().equals(Pages.General)));
    public Option<Boolean> lethalTick = addOption(new BooleanOption("LethalTick", false, v -> page.getValue().equals(Pages.General)));
    // Page - General | Safety
    public Option<Boolean> safeCheck = addOption(new BooleanOption("SafeCheck", true, v -> page.getValue().equals(Pages.General)));
    public Option<Boolean> safeOverride = addOption(new BooleanOption("SafeOverride", false, v -> page.getValue().equals(Pages.General)));

    // Page - Rotate
    public Option<Boolean> doRotate = addOption(new BooleanOption("Rotate", true, v -> page.getValue().equals(Pages.Rotate)));
    public Option<Enum<?>> rotateMode = addOption(new EnumOption("RotateMode", RotateModes.Server, v -> page.getValue().equals(Pages.Rotate) && doRotate.getValue()));
    public Option<Enum<?>> rotateYawStep = addOption(new EnumOption("YawStep", Rotations.None, v -> page.getValue().equals(Pages.Rotate) && doRotate.getValue()));
    public Option<Double> rotateYawStepLimit = addOption(new DoubleOption("YawStepLimit", 1, 180, 180, v -> page.getValue().equals(Pages.Rotate) && doRotate.getValue()));

    // Page - Place
    public Option<Boolean> doPlace = addOption(new BooleanOption("Place", true, v -> page.getValue().equals(Pages.Place)));
    public Option<Enum<?>> placementMode = addOption(new EnumOption("Placement", Placements.Native, v -> page.getValue().equals(Pages.Place)));
    public Option<Double> placeRange = addOption(new DoubleOption("PlaceRange", 1, 10, 4, v -> page.getValue().equals(Pages.Place)));
    public Option<Double> placeWallRange = addOption(new DoubleOption("PlaceWallRange", 0.6, 6, 4, v -> page.getValue().equals(Pages.Place)));
    public Option<Boolean> placeSwing = addOption(new BooleanOption("PlaceSwing", true, v -> page.getValue().equals(Pages.Place)));
    public Option<Double> placeSpeed = addOption(new DoubleOption("PlaceSpeed", 1, 20, 4, v -> page.getValue().equals(Pages.Place)));
    public Option<Boolean> placeCalcByEye = addOption(new BooleanOption("PlaceCalcByEye", false, v -> page.getValue().equals(Pages.Place)));
    public Option<Boolean> placeCalcCenter = addOption(new BooleanOption("PlaceCalcCenter", false, v -> page.getValue().equals(Pages.Place)));
    public Option<Enum<?>> placeSwapMode = addOption(new EnumOption("PlaceSwapMode", SwapModes.None, v -> page.getValue().equals(Pages.Place)));
    public Option<Boolean> antiSurround = addOption(new BooleanOption("AntiSurround", false, v -> page.getValue().equals(Pages.Place)));
    public Option<Boolean> placeStrict = addOption(new BooleanOption("PlaceStrict", false, v -> page.getValue().equals(Pages.Place)));
    public Option<Boolean> placeOnlyVisible = addOption(new BooleanOption("PlaceOnlyVisible", false, v -> page.getValue().equals(Pages.Place)));
    public Option<Boolean> placeOnlyExposed = addOption(new BooleanOption("PlaceOnlyExposed", false, v -> page.getValue().equals(Pages.Place)));

    // Page - Attack
    // Page - Attack | Normally
    public Option<Boolean> doAttack = addOption(new BooleanOption("Attack", true, v -> page.getValue().equals(Pages.Attack)));
    public Option<Double> attackRange = addOption(new DoubleOption("AttackRange", 1, 10, 5, v -> page.getValue().equals(Pages.Attack)));
    public Option<Double> attackWallRange = addOption(new DoubleOption("AttackWallRange", 0.1, 6, 4, v -> page.getValue().equals(Pages.Attack)));
    public Option<Boolean> attackSwing = addOption(new BooleanOption("AttackSwing", true, v -> page.getValue().equals(Pages.Attack)));
    public Option<Double> attackSpeed = addOption(new DoubleOption("AttackSpeed", 1, 20, 18, v -> page.getValue().equals(Pages.Attack)));
    public Option<Double> attackDelay = addOption(new DoubleOption("AttackDelay", 0, 5, 0, v -> page.getValue().equals(Pages.Attack)).addSpecialValue(0, "NoDelay"));
    public Option<Double> attackFactor = addOption(new DoubleOption("AttackFactor", 0, 3, 0, v -> page.getValue().equals(Pages.Attack) && attackDelay.getValue() != 0));
    public Option<Boolean> attackRaytrace = addOption(new BooleanOption("AttackRayTrace", true, v -> page.getValue().equals(Pages.Attack)));
    public Option<Double> attackTicksExisted = addOption(new DoubleOption("AttackTickExisted", 0, 10, 0, v -> page.getValue().equals(Pages.Attack)));
    public Option<Enum<?>> attackAntiWeakness = addOption(new EnumOption("AntiWeakness", SwapModes.None, v -> page.getValue().equals(Pages.Attack)));
    public Option<Boolean> attackInhibit = addOption(new BooleanOption("AttackInhibit", true, v -> page.getValue().equals(Pages.Attack)));
    public Option<Boolean> attackAnyCrystal = addOption(new BooleanOption("AttackAnyCrystal", false, v -> page.getValue().equals(Pages.Attack)));
    // Page - Attack | DelayCalc (Break Delay)
    public Option<Boolean> attackDelayCalc = addOption(new BooleanOption("AttackDelayCalc", false, v -> page.getValue().equals(Pages.Attack)));
    public Option<Double> attackTimeout = addOption(new DoubleOption("AttackTimeout", 0, 10, 3, v -> page.getValue().equals(Pages.Attack) && attackDelayCalc.getValue()));
    public Option<Double> attackMinTimeout = addOption(new DoubleOption("AttackMinTimeout", 0, 20, 5, v -> page.getValue().equals(Pages.Attack) && attackDelayCalc.getValue()));
    // Page - Attack | Force Attack (Instant Break)
    public Option<Boolean> forceAttack = addOption(new BooleanOption("ForceAttack", false, v -> page.getValue().equals(Pages.Attack)));
    public Option<Boolean> forceAttackCalc = addOption(new BooleanOption("ForceAttackCalc", false, v -> page.getValue().equals(Pages.Attack) && forceAttack.getValue()));
    public Option<Boolean> forceAttackMax = addOption(new BooleanOption("ForceAttackMax", true, v -> page.getValue().equals(Pages.Attack) && forceAttack.getValue()));
    public Option<Double> forceAttackDamage = addOption(new DoubleOption("ForceAttackDamage", 1, 10, 6, v -> page.getValue().equals(Pages.Attack) && forceAttack.getValue()));

    // Page - Render
    public Option<Boolean> doRender = addOption(new BooleanOption("Render", true, v -> page.getValue().equals(Pages.Render)));
    public Option<Boolean> doFade = addOption(new BooleanOption("Fade", true, v -> page.getValue().equals(Pages.Render)));
    public Option<Double> fadeTime = addOption(new DoubleOption("FadeTime", 0, 1000, 300, v -> page.getValue().equals(Pages.Render)));
    public Option<Boolean> renderDamage = addOption(new BooleanOption("RenderDamage", false, v -> page.getValue().equals(Pages.Render)));

    public Option<Boolean> debugInfo = addOption(new BooleanOption("DebugInfo", false, v -> page.getValue().equals(Pages.Other)));

    @Override
    public String getDetails() {
        return debugInfo.getValue() ? getBreakMs() + "ms" : "";
    }

    @Override
    public void onDisable() {
        renderPos = null;
        attackCrystal = null;
        placeCrystal = null;
        crystalRotation = null;
        attackPackets.clear();
        placePackets.clear();
        fadeList.clear();
    }

    @Override
    public void onLogout() {
        if (autoDisable.getValue()) {
            this.disable();
        }
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (mc.player.isUsingItem() && mc.player.getActiveHand() == Hand.MAIN_HAND
                || mc.options.attackKey.isPressed() || EntityUtil.isHotbarKeysPressed()) {
            autoSwapTimer.reset();
        }
        renderPos = null;
        ArrayList<Entity> entities = Lists.newArrayList(mc.world.getEntities());
        List<BlockPos> blocks = getSphere(mc.player.getPos());
        attackCrystal = calculateAttackCrystal(entities);
        if (doPlace.getValue()) {
            placeCrystal = calculatePlaceCrystal(blocks, entities);
        }
        double breakDelay = 1000.0f - attackSpeed.getValue() * 50.0f;
        if (attackDelayCalc.getValue()) {
            breakDelay = (float) Math.max(attackMinTimeout.getValue() * 50.0f, getBreakMs() + attackTimeout.getValue() * 50.0f);
        }
        attackRotate = attackCrystal != null && attackDelay.getValue() <= 0.0 && lastAttackTimer.passedMs(breakDelay);
        if (attackCrystal != null) {
            crystalRotation = attackCrystal.damageData.getPos();
        } else if (placeCrystal != null) {
            crystalRotation = placeCrystal.damageData.toCenterPos().add(0.0, 0.5, 0.0);
        }
        if (doRotate.getValue() && crystalRotation != null && (placeCrystal == null || canHoldCrystal())) {
            float[] rotations = RotateUtil.getRotationsTo(mc.player.getEyePos(), crystalRotation);
            if (rotateYawStep.getValue().equals(Rotations.Full) || rotateYawStep.getValue().equals(Rotations.Semirage) && attackRotate) {
                float yaw;
                float serverYaw = Vergence.ROTATE.getWrappedYaw();
                float diff = serverYaw - rotations[0];
                float diff1 = Math.abs(diff);
                if (diff1 > 180.0f) {
                    diff += diff > 0.0f ? -360.0f : 360.0f;
                }
                int dir = diff > 0.0f ? -1 : 1;
                float deltaYaw = (float) (dir * rotateYawStepLimit.getValue());
                if (diff1 > rotateYawStepLimit.getValue()) {
                    yaw = serverYaw + deltaYaw;
                    rotated = false;
                } else {
                    yaw = rotations[0];
                    rotated = true;
                    crystalRotation = null;
                }
                rotations[0] = yaw;
            } else {
                rotated = true;
                crystalRotation = null;
            }
            Vergence.ROTATE.rotate(new Rotation(rotations[1], rotations[0], 180, (RotateModes) rotateMode.getValue(), this.getPriority()));
        }
        if (!rotated && doRotate.getValue()) {
            return;
        }
        final Hand hand = getCrystalHand();
        if (attackCrystal != null) {
            if (attackRotate) {
                attackCrystal(attackCrystal.getDamageData(), hand);
                lastAttackTimer.reset();
            }
        }
        if (placeCrystal != null) {
            renderPos = placeCrystal.getDamageData();
            if (lastPlaceTimer.passedMs(1000.0f - placeSpeed.getValue() * 50.0f)) {
                placeCrystal(placeCrystal.getDamageData(), hand);
                lastPlaceTimer.reset();
            }
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null || attackDelay.getValue() <= 0.0) {
            return;
        }
        float attackFactorValue = (float) (50.0f / Math.max(1.0f, attackFactor.getValue()));
        if (attackCrystal != null && lastAttackTimer.passedMs(attackDelay.getValue() * attackFactorValue)) {
            attackCrystal(attackCrystal.getDamageData(), getCrystalHand());
            lastAttackTimer.reset();
        }
    }

    @Override
    public void onReceivePacket(PacketEvent.Receive event, Packet<?> packet) {
        if (isNull()) {
            return ;
        }

        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket packet1) {
            serverSlot = packet1.getSelectedSlot();
        }

        if (event.getPacket() instanceof PlaySoundS2CPacket packet1 && packet1.getCategory() == SoundCategory.BLOCKS && packet1.getSound().value() == SoundEvents.ENTITY_GENERIC_EXPLODE.value()) {
            for (Entity entity : Lists.newArrayList(mc.world.getEntities())) {
                if (entity instanceof EndCrystalEntity && entity.squaredDistanceTo(packet1.getX(), packet1.getY(), packet1.getZ()) < 144.0) {
                    mc.executeSync(() -> {
                        mc.world.removeEntity(entity.getId(), Entity.RemovalReason.KILLED);
                    });
                }
            }
        }
    }

    @Override
    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
        if (isNull()) {
            return ;
        }

        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket packet1) {
            serverSlot = packet1.getSelectedSlot();
        }

        boolean isClient = Vergence.NETWORK.isCached(packet);

        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket) {
            lastSwapTimer.reset();
        } else if (event.getPacket() instanceof PlayerInteractBlockC2SPacket packet1 && !isClient && mc.player.getStackInHand(packet1.getHand()).getItem() instanceof EndCrystalItem && attackAnyCrystal.getValue()) {
            BlockHitResult result = packet1.getBlockHitResult();
            manualCrystals.add(result.getBlockPos());
        }
    }

    @Override
    public void onEntitySpawn(EntitySpawnEvent event, Entity entity) {
        if (!(entity instanceof EndCrystalEntity crystalEntity)) {
            return;
        }
        Vec3d crystalPos = crystalEntity.getPos();
        BlockPos blockPos = BlockPos.ofFloored(crystalPos.add(0.0, -1.0, 0.0));
        boolean manualPos = manualCrystals.contains(blockPos);
        if (!forceAttack.getValue() && !(manualPos && attackAnyCrystal.getValue())) {
            return;
        }
        Long time = placePackets.remove(blockPos);
        attackRotate = time != null;
        if (attackRotate || manualPos) {
            attackInternal(crystalEntity, getCrystalHand());
            lastAttackTimer.reset();
        } else if (forceAttackCalc.getValue()) {
            if (attackRangeCheck(crystalPos)) {
                return;
            }
            double selfDamage = CrystalUtil.getDamageTo(mc.player, crystalPos, blockDestruction.getValue());
            if (playerDamageCheck(selfDamage)) {
                return;
            }
            for (Entity entity1 : mc.world.getEntities()) {
                if (entity1 == null || !entity1.isAlive() || entity1 == mc.player
                        || !isValidTarget(entity1, targets.getValue(), range.getValue())
                        || entity1.getDisplayName() != null && Vergence.FRIEND.isFriend(entity1.getDisplayName().getString())) {
                    continue;
                }
                double crystalDist = crystalPos.squaredDistanceTo(entity1.getPos());
                if (crystalDist > 144.0f) {
                    continue;
                }
                double dist = mc.player.squaredDistanceTo(entity1);
                if (dist > range.getValue() * range.getValue()) {
                    continue;
                }
                double damage = CrystalUtil.getDamageTo(entity1, crystalPos, blockDestruction.getValue());
                DamageData<EndCrystalEntity> data = new DamageData<>(crystalEntity, entity, damage, selfDamage, crystalEntity.getBlockPos().down());
                attackRotate = damage > forceAttackDamage.getValue() || attackCrystal != null
                        && damage >= attackCrystal.getDamage() && forceAttackMax.getValue()
                        || entity1 instanceof LivingEntity entity2 && isCrystalLethalTo(data, entity2);
                if (attackRotate) {
                    attackInternal(crystalEntity, getCrystalHand());
                    lastAttackTimer.reset();
                    break;
                }
            }
        }
    }

    private DamageData<EndCrystalEntity> calculateAttackCrystal(List<Entity> entities) {
        if (entities.isEmpty()) {
            return null;
        }
        DamageData<EndCrystalEntity> data = null;
        for (Entity crystal : entities) {
            if (!(crystal instanceof EndCrystalEntity crystal1) || !crystal.isAlive()) {
                continue;
            }
            Long time = attackPackets.get(crystal.getId());
            boolean attacked = time != null && time < getBreakMs();
            if ((crystal.age < attackTicksExisted.getValue() || attacked) && attackInhibit.getValue()) {
                continue;
            }
            if (attackRangeCheck(crystal1)) {
                continue;
            }
            double selfDamage = CrystalUtil.getDamageTo(mc.player,
                    crystal.getPos(), blockDestruction.getValue());
            boolean unsafeToPlayer = playerDamageCheck(selfDamage);
            if (unsafeToPlayer && !safeOverride.getValue()) {
                continue;
            }
            for (Entity entity : entities) {
                if (entity == null || !entity.isAlive() || entity == mc.player
                        || !isValidTarget(entity, targets.getValue(), range.getValue())
                        || entity.getDisplayName() != null && Vergence.FRIEND.isFriend(entity.getDisplayName().getString())) {
                    continue;
                }
                double crystalDist = crystal.squaredDistanceTo(entity);
                if (crystalDist > 144.0f) {
                    continue;
                }
                double dist = mc.player.squaredDistanceTo(entity);
                if (dist > range.getValue() * range.getValue()) {
                    continue;
                }
                double damage = CrystalUtil.getDamageTo(entity,
                        crystal.getPos(), blockDestruction.getValue());
                if (checkOverrideSafety(unsafeToPlayer, damage, entity)) {
                    continue;
                }
                if (data == null || damage > data.getDamage()) {
                    data = new DamageData<>(crystal1, entity,
                            damage, selfDamage, crystal1.getBlockPos().down());
                }
            }
        }
        if (data == null || targetDamageCheck(data)) {
            return null;
        }
        return data;
    }

    private void placeCrystal(BlockPos blockPos, Hand hand) {
        if (checkMultitask()) {
            return;
        }
        Direction sidePlace = getPlaceDirection(blockPos);
        BlockHitResult result = new BlockHitResult(blockPos.toCenterPos(), sidePlace, blockPos, false);
        if (!placeSwapMode.getValue().equals(SwapModes.Normal) && hand != Hand.OFF_HAND && getCrystalHand() == null) {
            if (isSilentSwap((SwapModes) placeSwapMode.getValue()) && InventoryUtil.getItemCount(Items.END_CRYSTAL) == 0) {
                return;
            }
            int crystalSlot = getCrystalSlot();
            if (crystalSlot != -1) {
                boolean canSwap = !placeSwapMode.getValue().equals(SwapModes.Normal) || autoSwapTimer.passedMs(500);
                if (canSwap) {
                    if (placeSwapMode.getValue().equals(SwapModes.Silent)) {
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId,
                                crystalSlot + 36, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
                    } else if (placeSwapMode.getValue().equals(SwapModes.Silent)) {
                        InventoryUtil.sendServerSlot(crystalSlot);
                    } else {
                        InventoryUtil.setClientSlot(crystalSlot);
                    }
                }
                placeInternal(result, Hand.MAIN_HAND);
                placePackets.put(blockPos, System.currentTimeMillis());
                if (canSwap) {
                    if (placeSwapMode.getValue().equals(SwapModes.SilentAlt)) {
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId,
                                crystalSlot + 36, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
                    } else if (placeSwapMode.getValue().equals(SwapModes.Silent)) {
                        if (mc.player.getInventory().selectedSlot != serverSlot) {
                            InventoryUtil.sendServerSlot(mc.player.getInventory().selectedSlot);
                        }
                    }
                }
            }
        } else if (isHoldingCrystal()) {
            placeInternal(result, hand);
            placePackets.put(blockPos, System.currentTimeMillis());
        }
    }

    public boolean canUseCrystalOnBlock(BlockPos p) {
        BlockState state = mc.world.getBlockState(p);
        if (!state.isOf(Blocks.OBSIDIAN) && !state.isOf(Blocks.BEDROCK)) {
            return false;
        }
        BlockPos p2 = p.up();
        BlockState state2 = mc.world.getBlockState(p2);
        // Low version (be l1ke 1.12)
        if (placementMode.getValue().equals(Placements.Protocol) && !mc.world.isAir(p2.up())) {
            return false;
        }
        if (!mc.world.isAir(p2) && !state2.isOf(Blocks.FIRE)) {
            return false;
        } else {
            final Box bb = FULL_CRYSTAL_BB;
            double d = p2.getX();
            double e = p2.getY();
            double f = p2.getZ();
            List<Entity> list = getEntitiesBlockingCrystal(new Box(d, e, f,
                    d + bb.maxX, e + bb.maxY, f + bb.maxZ));
            return list.isEmpty();
        }
    }

    private List<Entity> getEntitiesBlockingCrystal(Box box) {
        List<Entity> entities = new CopyOnWriteArrayList<>(mc.world.getOtherEntities(null, box));
        for (Entity entity : entities) {
            if (entity == null || !entity.isAlive()
                    || entity instanceof ExperienceOrbEntity) {
                entities.remove(entity);
            } else if (entity instanceof EndCrystalEntity entity1
                    && entity1.getBoundingBox().intersects(box) || attackPackets.containsKey(entity.getId()) && entity.age < attackTicksExisted.getValue()) {
                entities.remove(entity);
            }
        }
        return entities;
    }

    private DamageData<BlockPos> calculatePlaceCrystal(List<BlockPos> placeBlocks, List<Entity> entities) {
        if (placeBlocks.isEmpty() || entities.isEmpty()) {
            return null;
        }
        DamageData<BlockPos> data = null;
        for (BlockPos pos : placeBlocks) {
            if (!canUseCrystalOnBlock(pos) || placeRangeCheck(pos)) {
                continue;
            }
            double selfDamage = CrystalUtil.getDamageTo(mc.player, crystalDamageVec(pos), blockDestruction.getValue());
            boolean unsafeToPlayer = playerDamageCheck(selfDamage);
            if (unsafeToPlayer && !safeOverride.getValue()) {
                continue;
            }
            for (Entity entity : entities) {
                if (entity == null || !entity.isAlive() || entity == mc.player
                        || !isValidTarget(entity, targets.getValue(), range.getValue())
                        || entity.getDisplayName() != null && Vergence.FRIEND.isFriend(entity.getDisplayName().getString())) {
                    continue;
                }
                double blockDist = pos.getSquaredDistance(entity.getPos());
                if (blockDist > 144.0f) {
                    continue;
                }
                double dist = mc.player.squaredDistanceTo(entity);
                if (dist > range.getValue() * range.getValue()) {
                    continue;
                }
                double damage = CrystalUtil.getDamageTo(entity, crystalDamageVec(pos), blockDestruction.getValue());
                if (checkOverrideSafety(unsafeToPlayer, damage, entity)) {
                    continue;
                }
                if (data == null || damage > data.getDamage()) {
                    data = new DamageData<>(pos, entity, damage, selfDamage);
                }
            }
        }
        if (data == null || targetDamageCheck(data)) {
            return null;
        }
        return data;
    }

    private boolean placeRangeCheck(BlockPos pos) {
        Vec3d player = placeCalcByEye.getValue() ? mc.player.getEyePos() : mc.player.getPos();
        double dist = placeCalcCenter.getValue() ?
                player.squaredDistanceTo(pos.toCenterPos()) : pos.getSquaredDistance(player.x, player.y, player.z);
        if (dist > placeRange.getValue() * placeRange.getValue()) {
            return true;
        }
        Vec3d raytrace = Vec3d.of(pos).add(0.0, attackRaytrace.getValue() ? 2.700000047683716 : 1.0, 0.0);
        BlockHitResult result = mc.world.raycast(new RaycastContext(
                mc.player.getEyePos(), raytrace,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE, mc.player));
        double maxDist = attackRange.getValue() * attackRange.getValue();
        if (result != null && result.getType() == HitResult.Type.BLOCK && result.getBlockPos() != pos) {
            maxDist = attackWallRange.getValue() * attackWallRange.getValue();
            if (dist > placeWallRange.getValue() * placeWallRange.getValue()) {
                return true;
            }
        }
        return placeStrict.getValue() && dist > maxDist;
    }

    private boolean checkOverrideSafety(boolean unsafeToPlayer, double damage, Entity entity) {
        return safeOverride.getValue() && unsafeToPlayer && damage < EntityUtil.getHealth(entity) + 0.5;
    }

    private boolean targetDamageCheck(DamageData<?> crystal) {
        double minDmg = minDamage.getValue();
        if (crystal.getAttackTarget() instanceof LivingEntity entity && isCrystalLethalTo(crystal, entity)) {
            minDmg = 2.0f;
        }
        return crystal.getDamage() < minDmg;
    }

    private boolean playerDamageCheck(double playerDamage) {
        if (!mc.player.isCreative()) {
            float health = mc.player.getHealth() + mc.player.getAbsorptionAmount();
            if (safeCheck.getValue() && playerDamage >= health + 0.5f) {
                return true;
            }
            return playerDamage > maxPlayerDamage.getValue();
        }
        return false;
    }

    private boolean isFeetSurrounded(LivingEntity entity) {
        BlockPos pos1 = entity.getBlockPos();
        if (!mc.world.getBlockState(pos1).isReplaceable()) {
            return true;
        }
        for (Direction direction : Direction.values()) {
            if (!direction.getAxis().isHorizontal()) {
                continue;
            }
            BlockPos pos2 = pos1.offset(direction);
            if (mc.world.getBlockState(pos2).isReplaceable()) {
                return false;
            }
        }
        return true;
    }

    private boolean isCrystalLethalTo(DamageData<?> crystal, LivingEntity entity) {
        if (!isFeetSurrounded(entity)) {
            return false;
        }
        if (lethalTick.getValue()) {
            return lastAttackTimer.passedMs(500);
        }
        float health = entity.getHealth() + entity.getAbsorptionAmount();
        if (crystal.getDamage() * (1.0f + lethalMultiplier.getValue()) >= health + 0.5f) {
            return true;
        }
        if (armorDestroyer.getValue()) {
            for (ItemStack armorStack : entity.getArmorItems()) {
                int n = armorStack.getDamage();
                int n1 = armorStack.getMaxDamage();
                float durability = ((n1 - n) / (float) n1) * 100.0f;
                if (durability < armorScale.getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canHoldCrystal() {
        return isHoldingCrystal() || !placeSwapMode.getValue().equals(SwapModes.None) && getCrystalSlot() != -1;
    }

    private Hand getCrystalHand() {
        final ItemStack offhand = mc.player.getOffHandStack();
        final ItemStack mainhand = mc.player.getMainHandStack();
        if (offhand.getItem() instanceof EndCrystalItem) {
            return Hand.OFF_HAND;
        } else if (mainhand.getItem() instanceof EndCrystalItem) {
            return Hand.MAIN_HAND;
        }
        return null;
    }

    private boolean checkMultitask() {
        return !multiTask.getValue() && mc.player.isUsingItem()
                || !whileMining.getValue() && mc.interactionManager.isBreakingBlock();
    }

    private boolean isHoldingCrystal() {
        if (!checkMultitask() && (placeSwapMode.getValue().equals(SwapModes.Silent) || placeSwapMode.getValue().equals(SwapModes.Silent))) {
            return true;
        }
        return getCrystalHand() != null;
    }

    private Vec3d crystalDamageVec(BlockPos pos) {
        return Vec3d.of(pos).add(0.5, 1.0, 0.5);
    }

    private void placeInternal(BlockHitResult result, Hand hand) {
        if (hand == null) {
            return;
        }
        Vergence.NETWORK.sendSequencedPacket(id -> new PlayerInteractBlockC2SPacket(hand, result, id));
        if (placeSwing.getValue()) {
            mc.player.swingHand(hand);
        } else {
            Vergence.NETWORK.sendPacketWithChach(new HandSwingC2SPacket(hand));
        }
    }

    private boolean isSilentSwap(SwapModes swap) {
        return swap.equals(SwapModes.Silent) || swap.equals(SwapModes.SilentAlt);
    }

    private int getCrystalSlot() {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof EndCrystalItem) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private Direction getPlaceDirection(BlockPos blockPos) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        if (placeOnlyVisible.getValue()) {
            if (mc.player.getY() >= blockPos.getY()) {
                return Direction.UP;
            }
            BlockHitResult result = mc.world.raycast(new RaycastContext(
                    mc.player.getEyePos(), new Vec3d(x + 0.5, y + 0.5, z + 0.5),
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE, mc.player));
            if (result != null && result.getType() == HitResult.Type.BLOCK) {
                Direction direction = result.getSide();
                if (!placeOnlyExposed.getValue() || mc.world.isAir(blockPos.offset(direction))) {
                    return direction;
                }
            }
        } else {
            if (mc.world.isInBuildLimit(blockPos)) {
                return Direction.DOWN;
            }
            BlockHitResult result = mc.world.raycast(new RaycastContext(
                    mc.player.getEyePos(), new Vec3d(x + 0.5, y + 0.5, z + 0.5),
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE, mc.player));
            if (result != null && result.getType() == HitResult.Type.BLOCK) {
                return result.getSide();
            }
        }
        return Direction.UP;
    }

    public void attackCrystal(EndCrystalEntity entity, Hand hand) {
        if (attackCheckPre(hand) || !doAttack.getValue()) {
            return;
        }
        StatusEffectInstance weakness = mc.player.getStatusEffect(StatusEffects.WEAKNESS);
        StatusEffectInstance strength = mc.player.getStatusEffect(StatusEffects.STRENGTH);
        if (weakness != null && (strength == null || weakness.getAmplifier() > strength.getAmplifier())) {
            int slot = -1;
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (!stack.isEmpty() && (stack.getItem() instanceof SwordItem
                        || stack.getItem() instanceof AxeItem
                        || stack.getItem() instanceof PickaxeItem)) {
                    slot = i;
                    break;
                }
            }
            if (slot != -1) {
                boolean canSwap = !attackAntiWeakness.getValue().equals(SwapModes.Normal)|| autoSwapTimer.passedMs(500);
                if (!attackAntiWeakness.getValue().equals(SwapModes.None) && canSwap) {
                    if (attackAntiWeakness.getValue().equals(SwapModes.SilentAlt)) {
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId,
                                slot + 36, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
                    } else if (attackAntiWeakness.getValue().equals(SwapModes.SilentAlt)) {
                        InventoryUtil.sendServerSlot(slot);
                    } else {
                        InventoryUtil.setClientSlot(slot);
                    }
                }
                attackInternal(entity, Hand.MAIN_HAND);
                if (canSwap) {
                    if (attackAntiWeakness.getValue().equals(SwapModes.SilentAlt)) {
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, slot + 36, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
                    } else if (attackAntiWeakness.getValue().equals(SwapModes.Silent)) {
                        if (mc.player.getInventory().selectedSlot != serverSlot) {
                            InventoryUtil.sendServerSlot(mc.player.getInventory().selectedSlot);
                        }
                    }
                }
            }
        } else {
            attackInternal(entity, hand);
        }
    }

    private boolean attackCheckPre(Hand hand) {
        if (!lastSwapTimer.passedMs(swapDelay.getValue() * 25.0f)) {
            return true;
        }
        if (hand == Hand.MAIN_HAND) {
            return checkMultitask();
        }
        return false;
    }

    private void attackInternal(EndCrystalEntity crystalEntity, Hand hand) {
        hand = hand != null ? hand : Hand.MAIN_HAND;
        Vergence.NETWORK.sendPacketWithChach(PlayerInteractEntityC2SPacket.attack(crystalEntity, mc.player.isSneaking()));
        attackPackets.put(crystalEntity.getId(), System.currentTimeMillis());
        if (attackSwing.getValue()) {
            mc.player.swingHand(hand);
        } else {
            Vergence.NETWORK.sendPacketWithChach(new HandSwingC2SPacket(hand));
        }
    }

    private boolean attackRangeCheck(EndCrystalEntity entity) {
        return attackRangeCheck(entity.getPos());
    }

    private boolean attackRangeCheck(Vec3d entityPos) {
        Vec3d playerPos = mc.player.getEyePos();
        double dist = playerPos.squaredDistanceTo(entityPos);
        if (dist > attackRange.getValue() * attackRange.getValue()) {
            return true;
        }
        double yOff = Math.abs(entityPos.getY() - mc.player.getY());
        if (yOff > maxYAxisOffset.getValue()) {
            return true;
        }
        BlockHitResult result = mc.world.raycast(new RaycastContext(
                playerPos, entityPos, RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE, mc.player));
        return result.getType() != HitResult.Type.MISS
                && dist > attackWallRange.getValue() * attackWallRange.getValue();
    }

    private List<BlockPos> getSphere(Vec3d origin) {
        List<BlockPos> sphere = new ArrayList<>();
        double rad = Math.ceil(placeRange.getValue());
        for (double x = -rad; x <= rad; ++x) {
            for (double y = -rad; y <= rad; ++y) {
                for (double z = -rad; z <= rad; ++z) {
                    Vec3i pos = new Vec3i((int) (origin.getX() + x),
                            (int) (origin.getY() + y), (int) (origin.getZ() + z));
                    final BlockPos p = new BlockPos(pos);
                    sphere.add(p);
                }
            }
        }
        return sphere;
    }

    public int getBreakMs() {
        float avg = 0.0f;
        ArrayList<Long> latencyCopy = Lists.newArrayList(attackLatency);
        if (!latencyCopy.isEmpty()) {
            for (float t : latencyCopy) {
                avg += t;
            }
            avg /= latencyCopy.size();
        }
        return (int) avg;
    }

    private static class DamageData<T> {
        private final List<String> tags = new ArrayList<>();
        private T damageData;
        private Entity attackTarget;
        private BlockPos blockPos;
        private double damage, selfDamage;
        public DamageData() {

        }

        public DamageData(BlockPos damageData, Entity attackTarget, double damage, double selfDamage) {
            this.damageData = (T) damageData;
            this.attackTarget = attackTarget;
            this.damage = damage;
            this.selfDamage = selfDamage;
            this.blockPos = damageData;
        }

        public DamageData(T damageData, Entity attackTarget, double damage, double selfDamage, BlockPos blockPos) {
            this.damageData = damageData;
            this.attackTarget = attackTarget;
            this.damage = damage;
            this.selfDamage = selfDamage;
            this.blockPos = blockPos;
        }

        public void addTag(String tag) {
            tags.add(tag);
        }

        public void setDamageData(T damageData, Entity attackTarget, double damage, double selfDamage) {
            this.damageData = damageData;
            this.attackTarget = attackTarget;
            this.damage = damage;
            this.selfDamage = selfDamage;
        }

        public T getDamageData() {
            return damageData;
        }

        public Entity getAttackTarget() {
            return attackTarget;
        }

        public double getDamage() {
            return damage;
        }

        public double getSelfDamage() {
            return selfDamage;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }
    }

    public enum Pages {
        General,
        Rotate,
        Place,
        Attack,
        Render,
        Other
    }

    public enum SwapModes {
        Normal,
        Silent,
        SilentAlt,
        None
    }

    public enum Sequential {
        Normal,
        Strict,
        None
    }

    public enum Placements {
        Native,
        Protocol
    }

    public enum Rotations {
        Full,
        Semirage,
        None
    }
}
