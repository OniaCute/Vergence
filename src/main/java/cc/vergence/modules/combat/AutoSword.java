package cc.vergence.modules.combat;

import cc.vergence.features.enums.TargetTypes;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.combat.CombatUtil;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class AutoSword extends Module {
    public static AutoSword INSTANCE;
    private boolean switched = false;
    private int originalSlot = -1;

    public AutoSword() {
        super("AutoSword", Category.COMBAT);
    }

    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<TargetTypes>("Targets", EnumSet.of(TargetTypes.EnemyPlayers)));
    public Option<Boolean> slotBack = addOption(new BooleanOption("SlotBack", false));
    public Option<Double> distance = addOption(new DoubleOption("Distance", 1, 7, 5));

    @Override
    public String getDetails() {
        return (CombatUtil.getClosestAnyTarget(distance.getValue(), targets.getValue()) == null ? "NoTargets" : "Holding") + " | " + (switched ? "Switched" : "SlotBacked");
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (mc.player == null) {
            switched = false;
            return;
        }
        if (switched && CombatUtil.getClosestAnyTarget(distance.getValue(), targets.getValue()) == null && slotBack.getValue() && originalSlot != -1) {
            switched = false;
            InventoryUtil.switchToSlot(originalSlot);
            originalSlot = -1;
            return ;
        }
        if (!switched && CombatUtil.getClosestAnyTarget(distance.getValue(), targets.getValue()) != null) {
            if (!InventoryUtil.isSword(mc.player.getStackInHand(Hand.MAIN_HAND).getItem())) {
                if (originalSlot == -1 && slotBack.getValue()) {
                    originalSlot = mc.player.getInventory().selectedSlot;
                }
                int swordSlot = InventoryUtil.findHotbarSlot(stack -> InventoryUtil.isSword(stack.getItem()));
                if (swordSlot != -1) {
                    InventoryUtil.switchToSlot(swordSlot);
                    switched = true;
                }
            } else {
                originalSlot = -1;
            }
        }
    }

    @Override
    public void onEnable() {
        originalSlot = -1;
        switched = false;
    }

    @Override
    public void onDisable() {
        originalSlot = -1;
        switched = false;
    }

    @Override
    public void onBlock(Module module) {
        originalSlot = -1;
        switched = false;
    }
}
