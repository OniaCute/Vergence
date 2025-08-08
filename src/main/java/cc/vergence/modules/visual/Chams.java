package cc.vergence.modules.visual;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.other.ModelRenderer;
import cc.vergence.util.render.utils.Render3DUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;
import java.util.EnumSet;

public class Chams extends Module implements Wrapper {
    public static Chams INSTANCE;

    public Chams() {
        super("Chams", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<>("Targets", EnumSet.of(TargetTypes.EnemyPlayers, TargetTypes.Invisible)));
    public Option<Boolean> forEnemy = addOption(new BooleanOption("RenderEnemy", true));
    public Option<Boolean> forFriend = addOption(new BooleanOption("RenderFriend", true));
    public Option<Boolean> forMyself = addOption(new BooleanOption("RenderSelf", false));
    public Option<Boolean> shine = addOption(new BooleanOption("Shine", true));
    public Option<Boolean> forCrystal = addOption(new BooleanOption("RenderCrystal", true));
    public Option<Color> crystalFillColor = addOption(new ColorOption("CrystalFill", new Color(236, 105, 255, 130), v -> forCrystal.getValue()));
    public Option<Color> crystalOutlineColor = addOption(new ColorOption("CrystalOutline", new Color(165, 84, 255), v -> forCrystal.getValue()));
    public Option<Color> enemyFillColor = addOption(new ColorOption("EnemyFill", new Color(255, 19, 19, 108), v -> forEnemy.getValue()));
    public Option<Color> enemyOutlineColor = addOption(new ColorOption("EnemyOutline", new Color(197, 6, 6), v -> forEnemy.getValue()));
    public Option<Color> friendFillColor = addOption(new ColorOption("FriendFill", new Color(38, 184, 255, 128), v -> forFriend.getValue()));
    public Option<Color> friendOutlineColor = addOption(new ColorOption("FriendOutline", new Color(128, 220, 255), v -> forFriend.getValue()));
    public Option<Color> selfFillColor = addOption(new ColorOption("SelfFill", new Color(173, 255, 231, 107), v -> forMyself.getValue()));
    public Option<Color> selfOutlineColor = addOption(new ColorOption("SelfOutline", new Color(204, 255, 236), v -> forMyself.getValue()));
    public Option<Color> mobFillColor = addOption(new ColorOption("MobFill", new Color(255, 140, 59, 130), v -> targets.getValue().contains(TargetTypes.Mobs)));
    public Option<Color> mobOutlineColor = addOption(new ColorOption("MobOutline", new Color(255, 116, 27), v -> targets.getValue().contains(TargetTypes.Mobs)));
    public Option<Color> animalFillColor = addOption(new ColorOption("AnimalFill", new Color(85, 255, 145, 130), v -> targets.getValue().contains(TargetTypes.Animals)));
    public Option<Color> animalOutlineColor = addOption(new ColorOption("AnimalOutline", new Color(51, 197, 51), v -> targets.getValue().contains(TargetTypes.Animals)));
    public Option<Color> defaultFillColor = addOption(new ColorOption("DefaultFill", new Color(255, 255, 255, 56)));
    public Option<Color> defaultOutlineColor = addOption(new ColorOption("DefaultOutline", new Color(220, 220, 220)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (mc.world == null || mc.player == null) {
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

                ModelRenderer.renderModel(living, 1.0f, tickDelta, new ModelRenderer.Render(true, fill, true, outline, shine.getValue()));
            }

            else if (entity instanceof EndCrystalEntity crystalEntity) {
                ModelRenderer.renderModel(crystalEntity, 1.0f, tickDelta, new ModelRenderer.Render(true, crystalFillColor.getValue(), true, crystalOutlineColor.getValue(), shine.getValue()));
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
                || entity.getType().getSpawnGroup() == SpawnGroup.AXOLOTLS);
    }
}
