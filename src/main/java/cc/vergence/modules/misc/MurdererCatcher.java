package cc.vergence.modules.misc;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.DeathEvent;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.player.InventoryUtil;
import cc.vergence.util.render.other.ModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

// For mini-games ~
public class MurdererCatcher extends Module implements Wrapper {
    public static MurdererCatcher INSTANCE;
    private final HashSet<String> notifiedMurderers = new HashSet<>();
    private final HashSet<String> notifiedBowmen = new HashSet<>();
    public final ArrayList<String> murderers = new ArrayList<>();
    public final ArrayList<String> bowmen = new ArrayList<>();


    public MurdererCatcher() {
        super("MurdererCatcher", Category.MISC);
        INSTANCE = this;
    }

    public Option<Boolean> markMurderer = addOption(new BooleanOption("MarkMurderer", true));
    public Option<Boolean> markBowmer = addOption(new BooleanOption("MarkBowmer", true));
    public Option<Boolean> markShine = addOption(new BooleanOption("MarkShine", true, v -> markMurderer.getValue() || markBowmer.getValue()));
    public Option<Color> bowmenFillColor = addOption(new ColorOption("BowmenFillColor", new Color(44, 116, 255, 0), v -> markBowmer.getValue()));
    public Option<Color> bowmenOutlineColor = addOption(new ColorOption("BowmenOutlineColor", new Color(0, 128, 255, 228), v -> markBowmer.getValue()));
    public Option<Color> murdererFillColor = addOption(new ColorOption("MurdererFillColor", new Color(255, 16, 16, 0), v -> markMurderer.getValue()));
    public Option<Color> murdererOutlineColor = addOption(new ColorOption("MurdererOutlineColor", new Color(255, 22, 22, 228), v -> markMurderer.getValue()));
    public Option<Boolean> withSound = addOption(new BooleanOption("Sound", true));


    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDeathEvent(DeathEvent event, PlayerEntity deadPlayer) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (mc.player == deadPlayer) {
            reset();
            return ;
        }

        PlayerEntity nearestPlayer = null;
        double minDistance = Double.MAX_VALUE;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == deadPlayer) {
                continue;
            }

            double dist = player.getPos().distanceTo(deadPlayer.getPos());
            if (dist < minDistance) {
                minDistance = dist;
                nearestPlayer = player;
            }
        }

        if (nearestPlayer != null) {
            String text = Vergence.TEXT.get("Module.Modules.MurdererCatcher.Messages.PlayerDiedSuppose")
                    .replace("{player}", deadPlayer.getName().getString()
                    .replace("{Murderer}", nearestPlayer.getName().getString()
                    .replace("{distance}", String.format("%.2f", minDistance))));
            NotifyManager.newNotification(this, text);
            MessageManager.newMessage(this, text);
            if (withSound.getValue()) {
                try {
                    mc.world.playSound(mc.player, mc.player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.BLOCKS, 1f, 1f);
                } catch (Exception ignored) {
                }
            }

            if (markMurderer.getValue()) {
                murderers.add(nearestPlayer.getName().getString());
            }
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;

            String playerName = player.getName().getString();

            boolean hasIronSword = !player.getMainHandStack().isEmpty() && InventoryUtil.isSword(player.getMainHandStack().getItem());
            if (!hasIronSword) {
                for (int i = 0; i < player.getInventory().size(); i++) {
                    if (!player.getInventory().getStack(i).isEmpty() && InventoryUtil.isSword(player.getInventory().getStack(i).getItem())) {
                        hasIronSword = true;
                        break;
                    }
                }
            }

            if (hasIronSword && !notifiedMurderers.contains(playerName)) {
                String text = Vergence.TEXT.get("Module.Modules.MurdererCatcher.Messages.ItemCheckSuppose")
                        .replace("{murderer}", playerName)
                        .replace("{distance}", String.format("%.2f", EntityUtil.getDistance(player)));

                NotifyManager.newNotification(this, text);
                MessageManager.newMessage(this, text);
                if (withSound.getValue()) {
                    try {
                        mc.world.playSound(mc.player, mc.player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                    } catch (Exception ignored) {}
                }

                if (markMurderer.getValue()) {
                    if (bowmen.contains(playerName)) {
                        bowmen.remove(playerName);
                    }
                    murderers.add(playerName);
                }

                notifiedMurderers.add(playerName);
            }

            boolean hasBowOrArrow = !player.getMainHandStack().isEmpty() && player.getMainHandStack().getItem().getTranslationKey().contains("bow") || player.getMainHandStack().getItem().getTranslationKey().contains("arrow");
            for (int i = 0; i < player.getInventory().size(); i++) {
                var stack = player.getInventory().getStack(i);
                if (stack.isEmpty()) continue;
                String name = stack.getItem().getTranslationKey();
                if (name.contains("bow") || name.contains("arrow")) {
                    hasBowOrArrow = true;
                    break;
                }
            }

            if (hasBowOrArrow && !notifiedBowmen.contains(playerName)) {
                String text = Vergence.TEXT.get("Module.Modules.MurdererCatcher.Messages.BowCheck")
                        .replace("{bowmen}", playerName)
                        .replace("{distance}", String.format("%.2f", EntityUtil.getDistance(player)));

                NotifyManager.newNotification(this, text);
                MessageManager.newMessage(this, text);
                if (withSound.getValue()) {
                    try {
                        mc.world.playSound(mc.player, mc.player.getBlockPos(), SoundEvents.ENTITY_VILLAGER_TRADE, SoundCategory.BLOCKS, 1f, 1f);
                    } catch (Exception ignored) {}
                }

                if (!murderers.contains(playerName) && markBowmer.getValue()) {
                    bowmen.add(playerName);
                }
                notifiedBowmen.add(playerName);
            }
        }
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (isNull()) {
            reset();
            return;
        }

        for (PlayerEntity player : mc.world.getPlayers()) {
            for (String s : murderers) {
                if (player.getName().getString().equals(s)) {
                    ModelRenderer.renderModel(player, 1.0f, tickDelta, new ModelRenderer.Render(true, murdererFillColor.getValue(), true, murdererOutlineColor.getValue(), markShine.getValue()));
                }
            }
            for (String s : bowmen) {
                if (player.getName().getString().equals(s)) {
                    ModelRenderer.renderModel(player, 1.0f, tickDelta, new ModelRenderer.Render(true, bowmenFillColor.getValue(), true, bowmenOutlineColor.getValue(), markShine.getValue()));
                }
            }
        }
    }

    private void reset() {
        murderers.clear();
        notifiedMurderers.clear();
        bowmen.clear();
        notifiedBowmen.clear();
    }


    @Override
    public void onEnable() {
        reset();
    }

    @Override
    public void onDisable() {
        reset();
    }

    @Override
    public void onLogout() {
        reset();
    }

    @Override
    public void onShutDown() {
        reset();
    }

    @Override
    public void onConfigChange() {
        reset();
    }
}
