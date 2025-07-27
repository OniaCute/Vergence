package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.InteractionUtil;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.sound.SoundEvents;

public class AutoRepairArmor extends Module {
    public static AutoRepairArmor INSTANCE;

    private final FastTimerUtil timer = new FastTimerUtil();
    private boolean repairing = false;
    private boolean lastRepairing = false;

    // Options
    public Option<Boolean> notify = addOption(new BooleanOption("Notify", true));
    public Option<Boolean> withSound = addOption(new BooleanOption("Sounds", true));
    public Option<Boolean> durabilityCheck = addOption(new BooleanOption("DurabilityCheck", true));
    public Option<Boolean> doRotate = addOption(new BooleanOption("Rotate", false));
    public Option<Boolean> doSwing = addOption(new BooleanOption("Swing", false));
    public Option<Double> delay = addOption(new DoubleOption("Delay", 1, 200, 10).setUnit("ms"));

    public AutoRepairArmor() {
        super("AutoRepairArmor", Category.COMBAT);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        repairing = false;
        lastRepairing = false;
        timer.reset();
    }

    @Override
    public void onDisable() {
        repairing = false;
        if (notify.getValue() && lastRepairing) {
            NotifyManager.newNotification(this, Vergence.TEXT.get("Module.Modules.AutoRepairArmor.Messages.Stopped"));
        }
        lastRepairing = false;
    }

    @Override
    public String getDetails() {
        return repairing ? "Repairing" : "Standing";
    }

    @Override
    public void onTick() {
        if (isNull() || !timer.passedMs(delay.getValue().intValue())) return;

        if (durabilityCheck.getValue() && areItemsFullDura(mc.player)) {
            repairing = false;
            disable();
            return;
        }

        int expSlot = findExpBottleSlot();
        if (expSlot == -1) {
            repairing = false;
            disable();
            return;
        }

        int prevSlot = mc.player.getInventory().selectedSlot;
        InventoryUtil.switchToSlot(expSlot);

        if (doRotate.getValue()) {
            mc.player.setPitch(90f);
        }

        mc.getNetworkHandler().sendPacket(new PlayerInteractItemC2SPacket(
                Hand.MAIN_HAND,
                InteractionUtil.getNextSequence(mc.interactionManager),
                mc.player.getYaw(),
                doRotate.getValue() ? 90.0f : mc.player.getPitch()
        ));

        if (doSwing.getValue()) {
            mc.player.swingHand(Hand.MAIN_HAND);
        }

        InventoryUtil.switchToSlot(prevSlot);
        repairing = true;
        timer.reset();

        handleNotificationState();
    }

    private void handleNotificationState() {
        if (repairing != lastRepairing) {
            if (repairing) {
                if (notify.getValue()) {
                    NotifyManager.newNotification(this, Vergence.TEXT.get("Module.Modules.AutoRepairArmor.Messages.Repairing"));
                }
                if (withSound.getValue()) {
                    mc.world.playSound(mc.player, mc.player.getBlockPos(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1f, 1f);
                }
            } else {
                if (notify.getValue()) {
                    NotifyManager.newNotification(this, Vergence.TEXT.get("Module.Modules.AutoRepairArmor.Messages.Stopped"));
                }
            }
            lastRepairing = repairing;
        }
    }

    private int findExpBottleSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof ExperienceBottleItem) {
                return i;
            }
        }
        return -1;
    }

    private boolean areItemsFullDura(PlayerEntity player) {
        if (!isItemFullDura(player.getMainHandStack()) || !isItemFullDura(player.getOffHandStack()))
            return false;

        for (ItemStack stack : player.getArmorItems()) {
            if (!isItemFullDura(stack)) return false;
        }
        return true;
    }

    private boolean isItemFullDura(ItemStack stack) {
        if (stack.isEmpty()) return true;
        return stack.getDamage() == 0 || stack.getMaxDamage() == 0;
    }
}
