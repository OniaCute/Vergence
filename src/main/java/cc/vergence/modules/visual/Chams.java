package cc.vergence.modules.visual;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.modules.Module;
import cc.vergence.modules.misc.MurdererCatcher;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.render.other.ModelRenderer;
import cc.vergence.util.render.utils.Render3DUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.EnumSet;
import java.util.Objects;

public class Chams extends Module implements Wrapper {
    public static Chams INSTANCE;
    private FastTimerUtil timer = new FastTimerUtil();

    public Chams() {
        super("Chams", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", RenderMode.Normal));
    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<>("Targets", EnumSet.of(TargetTypes.EnemyPlayers, TargetTypes.Invisible, TargetTypes.Crystal)));
    public Option<Double> syncTime = addOption(new DoubleOption("SyncTime", 20, 5000, 500, v -> mode.getValue().equals(RenderMode.Glow)).setUnit("ms"));
    public Option<Boolean> shine = addOption(new BooleanOption("Shine", true, v -> mode.getValue().equals(RenderMode.Normal)));
    public Option<Boolean> forEnemy = addOption(new BooleanOption("RenderEnemy", true, v -> mode.getValue().equals(RenderMode.Normal)));
    public Option<Boolean> forFriend = addOption(new BooleanOption("RenderFriend", true, v -> mode.getValue().equals(RenderMode.Normal)));
    public Option<Boolean> forMyself = addOption(new BooleanOption("RenderSelf", false, v -> mode.getValue().equals(RenderMode.Normal)));
    public Option<Boolean> forCrystal = addOption(new BooleanOption("RenderCrystal", true, v -> mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> crystalFillColor = addOption(new ColorOption("CrystalFill", new Color(236, 105, 255, 130), v -> forCrystal.getValue() && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> crystalOutlineColor = addOption(new ColorOption("CrystalOutline", new Color(165, 84, 255), v -> forCrystal.getValue() && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> enemyFillColor = addOption(new ColorOption("EnemyFill", new Color(255, 19, 19, 108), v -> forEnemy.getValue() && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> enemyOutlineColor = addOption(new ColorOption("EnemyOutline", new Color(197, 6, 6), v -> forEnemy.getValue() && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> friendFillColor = addOption(new ColorOption("FriendFill", new Color(38, 184, 255, 128), v -> forFriend.getValue() && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> friendOutlineColor = addOption(new ColorOption("FriendOutline", new Color(128, 220, 255), v -> forFriend.getValue() && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> selfFillColor = addOption(new ColorOption("SelfFill", new Color(173, 255, 231, 107), v -> forMyself.getValue() && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> selfOutlineColor = addOption(new ColorOption("SelfOutline", new Color(204, 255, 236), v -> forMyself.getValue() && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> mobFillColor = addOption(new ColorOption("MobFill", new Color(255, 140, 59, 130), v -> targets.getValue().contains(TargetTypes.Mobs) && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> mobOutlineColor = addOption(new ColorOption("MobOutline", new Color(255, 116, 27), v -> targets.getValue().contains(TargetTypes.Mobs) && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> animalFillColor = addOption(new ColorOption("AnimalFill", new Color(85, 255, 145, 130), v -> targets.getValue().contains(TargetTypes.Animals) && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> animalOutlineColor = addOption(new ColorOption("AnimalOutline", new Color(51, 197, 51), v -> targets.getValue().contains(TargetTypes.Animals) && mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> defaultFillColor = addOption(new ColorOption("DefaultFill", new Color(255, 255, 255, 56), v -> mode.getValue().equals(RenderMode.Normal)));
    public Option<Color> defaultOutlineColor = addOption(new ColorOption("DefaultOutline", new Color(220, 220, 220), v -> mode.getValue().equals(RenderMode.Normal)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (mc.world == null || mc.player == null || !mode.getValue().equals(RenderMode.Normal)) {
            return;
        }
        for (Entity entity : mc.world.getEntities()) {
            if (!Render3DUtil.isFrustumVisible(entity.getBoundingBox())) {
                continue;
            }

            if (entity instanceof LivingEntity living) {
                boolean isPlayer = entity instanceof PlayerEntity;
                boolean isSelf = entity == mc.player;
                boolean isFriend = isPlayer && Vergence.FRIEND.isFriend(entity.getName().getString());
                boolean isEnemy = isPlayer && Vergence.ENEMY.isEnemy(entity.getName().getString());

                if (isSelf && !forMyself.getValue()) {
                    continue;
                }
                if (isFriend && !forFriend.getValue()) {
                    continue;
                }
                if (!isPlayer && !isValidEntity(entity)) {
                    continue;
                }

                Color fill, outline;

                if (isSelf && forMyself.getValue()) {
                    fill = selfFillColor.getValue();
                    outline = selfOutlineColor.getValue();
                } else if (isFriend && forFriend.getValue()) {
                    fill = friendFillColor.getValue();
                    outline = friendOutlineColor.getValue();
                } else if (isEnemy && forEnemy.getValue()) {
                    fill = enemyFillColor.getValue();
                    outline = enemyOutlineColor.getValue();
                } else if (!isPlayer) {
                    if (entity.getType().getSpawnGroup() == SpawnGroup.MONSTER && targets.getValue().contains(TargetTypes.Mobs)) {
                        fill = mobFillColor.getValue();
                        outline = mobOutlineColor.getValue();
                    } else if ((entity.getType().getSpawnGroup() == SpawnGroup.CREATURE
                            || entity.getType().getSpawnGroup() == SpawnGroup.WATER_CREATURE
                            || entity.getType().getSpawnGroup() == SpawnGroup.WATER_AMBIENT
                            || entity.getType().getSpawnGroup() == SpawnGroup.UNDERGROUND_WATER_CREATURE
                            || entity.getType().getSpawnGroup() == SpawnGroup.AXOLOTLS)
                            && targets.getValue().contains(TargetTypes.Animals)) {
                        fill = animalFillColor.getValue();
                        outline = animalOutlineColor.getValue();
                    } else {
                        fill = defaultFillColor.getValue();
                        outline = defaultOutlineColor.getValue();
                    }
                } else {
                    fill = defaultFillColor.getValue();
                    outline = defaultOutlineColor.getValue();
                }

                if (MurdererCatcher.INSTANCE.getStatus() && (MurdererCatcher.INSTANCE.bowmen.contains(living.getName().getString()) || MurdererCatcher.INSTANCE.murderers.contains(living.getName().getString()))) {
                    return ;
                }

                ModelRenderer.renderModel(living, 1.0f, tickDelta, new ModelRenderer.Render(true, fill, true, outline, shine.getValue()));
            }

            else if (entity instanceof EndCrystalEntity crystalEntity) {
                ModelRenderer.renderModel(crystalEntity, 1.0f, tickDelta, new ModelRenderer.Render(true, crystalFillColor.getValue(), true, crystalOutlineColor.getValue(), shine.getValue()));
            }
        }
    }

    @Override
    public void onDisable() {
        timer.reset();
    }

    @Override
    public void onConfigChange() {
        timer.reset();
    }

    @Override
    public void onLogout() {
        timer.reset();
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (isNull() || !mode.getValue().equals(RenderMode.Glow)) {
            return ;
        }

        if (timer.passedMs(syncTime.getValue())) {
            Team friendTeam = Objects.requireNonNull(mc.getNetworkHandler()).getScoreboard().addTeam("Chams_Friend_Team");
            Team enemyTeam = Objects.requireNonNull(mc.getNetworkHandler()).getScoreboard().addTeam("Chams_Enemy_Team");
            Team animalsTeam = Objects.requireNonNull(mc.getNetworkHandler()).getScoreboard().addTeam("Chams_Animals_Team");
            Team mobsTeam = Objects.requireNonNull(mc.getNetworkHandler()).getScoreboard().addTeam("Chams_Mobs_Team");
            Team crystalTeam = Objects.requireNonNull(mc.getNetworkHandler()).getScoreboard().addTeam("Chams_Crystal_Team");
            Team selfTeam = Objects.requireNonNull(mc.getNetworkHandler()).getScoreboard().addTeam("Chams_Self_Team");
            friendTeam.setColor(Formatting.AQUA);
            enemyTeam.setColor(Formatting.RED);
            animalsTeam.setColor(Formatting.GREEN);
            mobsTeam.setColor(Formatting.GOLD);
            crystalTeam.setColor(Formatting.LIGHT_PURPLE);
            selfTeam.setColor(Formatting.DARK_PURPLE);

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof PlayerEntity player) {
                    try {
                        mc.getNetworkHandler().getScoreboard().removeScoreHolderFromTeam(player.getName().getString(), friendTeam);
                        mc.getNetworkHandler().getScoreboard().removeScoreHolderFromTeam(player.getName().getString(), enemyTeam);
                        mc.getNetworkHandler().getScoreboard().removeScoreHolderFromTeam(player.getName().getString(), selfTeam);
                    } catch (IllegalStateException ignored) {
                    }

                    if (player.equals(mc.player) && forMyself.getValue()) {
                        mc.getNetworkHandler().getScoreboard().addScoreHolderToTeam(player.getName().getString(), selfTeam);
                    } else if (Vergence.FRIEND.isFriend(player)) {
                        mc.getNetworkHandler().getScoreboard().addScoreHolderToTeam(player.getName().getString(), friendTeam);
                    } else if (Vergence.ENEMY.isEnemy(player)) {
                        mc.getNetworkHandler().getScoreboard().addScoreHolderToTeam(player.getName().getString(), enemyTeam);
                    }
                } else if (entity instanceof AnimalEntity || (entity.getType().getSpawnGroup() == SpawnGroup.CREATURE
                        || entity.getType().getSpawnGroup() == SpawnGroup.WATER_CREATURE
                        || entity.getType().getSpawnGroup() == SpawnGroup.WATER_AMBIENT
                        || entity.getType().getSpawnGroup() == SpawnGroup.UNDERGROUND_WATER_CREATURE
                        || entity.getType().getSpawnGroup() == SpawnGroup.AXOLOTLS
                        || entity instanceof VillagerEntity)) {
                    mc.getNetworkHandler().getScoreboard().addScoreHolderToTeam(entity.getUuidAsString(), animalsTeam);
                } else if (entity.getType().getSpawnGroup() == SpawnGroup.MONSTER) {
                    mc.getNetworkHandler().getScoreboard().addScoreHolderToTeam(entity.getUuidAsString(), mobsTeam);
                } else if (entity instanceof EndCrystalEntity) {
                    mc.getNetworkHandler().getScoreboard().addScoreHolderToTeam(entity.getUuidAsString(), crystalTeam);
                }
            }
        }
    }

    private boolean isValidEntity(Entity entity) {
        if (targets.getValue().contains(TargetTypes.EnemyPlayers) && entity.getType() == EntityType.PLAYER || (entity.isInvisible() && targets.getValue().contains(TargetTypes.Invisible))) {
            return true;
        }
        if (targets.getValue().contains(TargetTypes.Mobs) && entity.getType().getSpawnGroup() == SpawnGroup.MONSTER) {
            return true;
        }
        return targets.getValue().contains(TargetTypes.Animals)
                && (entity.getType().getSpawnGroup() == SpawnGroup.CREATURE
                || entity.getType().getSpawnGroup() == SpawnGroup.WATER_CREATURE
                || entity.getType().getSpawnGroup() == SpawnGroup.WATER_AMBIENT
                || entity.getType().getSpawnGroup() == SpawnGroup.UNDERGROUND_WATER_CREATURE
                || entity.getType().getSpawnGroup() == SpawnGroup.AXOLOTLS
                || entity instanceof VillagerEntity);
    }

    public enum RenderMode {
        Normal,
        Glow
    }
}
