package cc.vergence.modules.visual;

import cc.vergence.features.enums.TargetTypes;
import cc.vergence.features.event.events.RenderEntityEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.other.ModelRenderer;
import cc.vergence.util.render.utils.Render3DUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;
import java.util.EnumSet;

public class Chams extends Module implements Wrapper {
    public static Chams INSTANCE;

    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<TargetTypes>("Targets", EnumSet.of(TargetTypes.EnemyPlayers, TargetTypes.invisible)));
    public Option<Boolean> texture = addOption(new BooleanOption("Texture", true));
    public Option<Boolean> shine = addOption(new BooleanOption("Shine", true));
    public Option<Color> fillColor = addOption(new ColorOption("FillColor", new Color(255, 132, 255)));
    public Option<Color> outlineColor = addOption(new ColorOption("OutlineColor", new Color(255, 181, 236)));

    public Chams() {
        super("Chams", Category.VISUAL);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (mc.world == null) {
            return;
        }
        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player && mc.options.getPerspective().isFirstPerson()) {
                continue;
            }
            if (!Render3DUtil.isFrustumVisible(entity.getBoundingBox())) {
                continue;
            }
            if (entity instanceof LivingEntity livingEntity) {
                if (!isValidEntity(livingEntity)) {
                    continue;
                }

//                boolean flag = damageModify.getValue() && livingEntity.hurtTime > 0;
//                Color friendFill = flag ? ColorUtils.getColor(damageColor.getColor(), friendFillColor.getColor().getAlpha()) : (friendMode.getValue().equals("Default") ? Sydney.FRIEND_MANAGER.getDefaultFriendColor(friendFillColor.getColor().getAlpha()) : friendFillColor.getColor());
//                Color friendOutline = flag ? ColorUtils.getColor(damageColor.getColor(), friendOutlineColor.getColor().getAlpha()) : (friendMode.getValue().equals("Default") ? Sydney.FRIEND_MANAGER.getDefaultFriendColor(friendOutlineColor.getColor().getAlpha()) : friendOutlineColor.getColor());
//                Color fillColor = (livingEntity instanceof PlayerEntity player && Sydney.FRIEND_MANAGER.contains(player.getName().getString()) && !friendMode.getValue().equals("Sync")) ? friendFill : flag ? ColorUtils.getColor(damageColor.getColor(), entityFillColor.getColor().getAlpha()) : entityFillColor.getColor();
//                Color outlineColor = (livingEntity instanceof PlayerEntity player && Sydney.FRIEND_MANAGER.contains(player.getName().getString()) && !friendMode.getValue().equals("Sync")) ? friendOutline : flag ? ColorUtils.getColor(damageColor.getColor(), entityOutlineColor.getColor().getAlpha()) : entityOutlineColor.getColor();

                ModelRenderer.renderModel(livingEntity, 1.0f, tickDelta, new ModelRenderer.Render(true, fillColor.getValue(), true, outlineColor.getValue(), shine.getValue()));
            }

//            if (crystals.getValue() && entity instanceof EndCrystalEntity crystal) {
//                ModelRenderer.renderModel(crystal, 1.0f, event.getTickDelta(), new ModelRenderer.Render(crystalMode.getValue().equals("Fill") || crystalMode.getValue().equals("Both"), crystalPulse.getValue() ? ColorUtils.getPulse(crystalFillColor.getColor()) : crystalFillColor.getColor(), crystalMode.getValue().equals("Outline") || crystalMode.getValue().equals("Both"), crystalPulse.getValue() ? ColorUtils.getPulse(crystalOutlineColor.getColor()) : crystalOutlineColor.getColor(), crystalShine.getValue()));
//            }
        }
    }

    private boolean isValidEntity(Entity entity) {
        if (targets.getValue().contains(TargetTypes.EnemyPlayers) && entity.getType() == EntityType.PLAYER) return true;
        if (targets.getValue().contains(TargetTypes.Mobs) && entity.getType().getSpawnGroup() == SpawnGroup.MONSTER) return true;
        return targets.getValue().contains(TargetTypes.Animals) && (entity.getType().getSpawnGroup() == SpawnGroup.CREATURE || entity.getType().getSpawnGroup() == SpawnGroup.WATER_CREATURE || entity.getType().getSpawnGroup() == SpawnGroup.WATER_AMBIENT || entity.getType().getSpawnGroup() == SpawnGroup.UNDERGROUND_WATER_CREATURE || entity.getType().getSpawnGroup() == SpawnGroup.AXOLOTLS);
    }
}
