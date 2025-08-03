package cc.vergence.modules.combat;

import cc.vergence.features.enums.player.SwapModes;
import cc.vergence.features.event.events.DeathEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;

public class SmartOffhand extends Module {
    public static SmartOffhand INSTANCE;
    private int ticks = 0;

    public SmartOffhand() {
        super("SmartOffhand", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Enum<?>> item = addOption(new EnumOption("Items", ItemModes.Totem));
    public Option<Boolean> tickAbort = addOption(new BooleanOption("TickAbort", true));
    public Option<Boolean> healthCheck = addOption(new BooleanOption("Health", true));
    public Option<Double> minHealth = addOption(new DoubleOption("MinHealth", 0, 36, 16, v -> healthCheck.getValue()));
    public Option<Boolean> fallCheck = addOption(new BooleanOption("Fall", true));
    public Option<Double> fallDistance = addOption(new DoubleOption("FallDistance", 20, 80, 30, v -> fallCheck.getValue()));
    public Option<Boolean> elytraCheck = addOption(new BooleanOption("Elytra", true));
    public Option<Boolean> lethalOverride = addOption(new BooleanOption("LethalOverride", true));
    public Option<Boolean> swordCheck = addOption(new BooleanOption("SmartSword", true));
    public Option<Boolean> mineCheck = addOption(new BooleanOption("SmartMine", true));
    public Option<Boolean> displayTotemCount = addOption(new BooleanOption("DisplayTotemCount", true));

    @Override
    public String getDetails() {
        return displayTotemCount.getValue() ? String.valueOf(InventoryUtil.countItems(Items.TOTEM_OF_UNDYING)) : "";
    }

    @Override
    public void onDeathEvent(DeathEvent event, PlayerEntity player) {
        if (player == mc.player) {
            ticks = 0;
        }
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (ticks > 0 && tickAbort.getValue()) {
            ticks--;
            return;
        }

        if (!(mc.currentScreen instanceof InventoryScreen) && mc.currentScreen instanceof HandledScreen<?>) {
            return;
        }

        Item item = getItem();
        if (item == null) return;

        int slot;

        if (item == Items.TOTEM_OF_UNDYING && needsTotem()) {
            if (mc.player.getOffHandStack().getItem() == item) return;

            slot = InventoryUtil.findItemInventorySlot(item);
            if (slot == -1) {
                slot = InventoryUtil.findItem(item);
            }
            if (slot == -1) {
                slot = InventoryUtil.findEmptySlot();
            }
        } else {
            if (mc.player.getOffHandStack().getItem() == item) return;

            slot = InventoryUtil.findItemInventorySlot(item);
            if (slot == -1) slot = InventoryUtil.findItem(item);

            if (slot == -1) {
                return;
            }
        }

        if (slot == -1) return;

        InventoryUtil.doSwap(SwapModes.Pickup, slot, 45);
        ticks = 2 + mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()).getLatency() / 50;
    }

    private Item getItem() {
        if (mc.player.getMainHandStack().getItem() instanceof SwordItem || mc.player.getMainHandStack().getItem() instanceof AxeItem) {
            if (swordCheck.getValue() && mc.options.useKey.isPressed() && (lethalOverride.getValue() || !needsTotem())) {
                return Items.ENCHANTED_GOLDEN_APPLE;
            }
        }

        if (mineCheck.getValue() && mc.player.isUsingItem()) {
            return Items.END_CRYSTAL;
        }

        return switch ((ItemModes) item.getValue()) {
            case Totem -> Items.TOTEM_OF_UNDYING;
            case GoldenApple -> Items.ENCHANTED_GOLDEN_APPLE;
            case Crystal -> Items.END_CRYSTAL;
            default -> Items.TOTEM_OF_UNDYING;
        };
    }

    private boolean needsTotem() {
        if (healthCheck.getValue() && (mc.player.getHealth() + mc.player.getAbsorptionAmount() <= minHealth.getValue())) {
            return true;
        }

        if (fallCheck.getValue() && mc.player.fallDistance > fallDistance.getValue()) {
            return true;
        }

        if (elytraCheck.getValue() && mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
            return true;
        }

        return false;
    }


    public enum ItemModes {
        Totem,
        GoldenApple,
        Crystal
    }
}
