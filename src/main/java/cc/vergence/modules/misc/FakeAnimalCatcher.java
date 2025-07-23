package cc.vergence.modules.misc;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.modules.Module;
import cc.vergence.util.render.other.ModelRenderer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FakeAnimalCatcher extends Module {
    public static FakeAnimalCatcher INSTANCE;
    private final List<LivingEntity> likelyFakeAnimals = new ArrayList<>();

    public FakeAnimalCatcher() {
        super("FakeAnimalCatcher", Category.MISC);
        INSTANCE = this;
    }

    public Option<Boolean> shine = addOption(new BooleanOption("Shine", true));
    public Option<Color> fillColor = addOption(new ColorOption("FillColor", new Color(255, 255, 255, 56)));
    public Option<Color> outlineColor = addOption(new ColorOption("OutlineColor", new Color(220, 220, 220)));

    @Override
    public String getDetails() {
        return String.valueOf(likelyFakeAnimals.size());
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        likelyFakeAnimals.clear();
        Vec3d playerPos = mc.player.getPos();

        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (entity == mc.player) continue;
            if (!isAnimalType(living)) continue;

            if (isLikelyFakeAnimal(living)) {
                likelyFakeAnimals.add(living);
            }
        }

        likelyFakeAnimals.sort(Comparator.comparingDouble(a -> a.squaredDistanceTo(playerPos)));
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        for (LivingEntity entity : getLikelyFakeAnimals()) {
            ModelRenderer.renderModel(entity, 1.0f, tickDelta, new ModelRenderer.Render(true, fillColor.getValue(), true, outlineColor.getValue(), shine.getValue()));
        }
    }

    public List<LivingEntity> getLikelyFakeAnimals() {
        return likelyFakeAnimals;
    }

    public LivingEntity getNearestFakeAnimal() {
        return likelyFakeAnimals.isEmpty() ? null : likelyFakeAnimals.get(0);
    }

    private boolean isAnimalType(Entity e) {
        return e instanceof ChickenEntity
                || e instanceof CowEntity
                || e instanceof PigEntity
                || e instanceof SheepEntity
                || e instanceof RabbitEntity
                || e instanceof CatEntity
                || e instanceof HorseEntity;
    }

    private boolean isLikelyFakeAnimal(LivingEntity entity) {
        if (entity.hasCustomName()) {
            return true;
        }
        PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(entity.getUuid());
        return entry != null;
    }
}
