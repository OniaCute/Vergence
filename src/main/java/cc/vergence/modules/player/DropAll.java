package cc.vergence.modules.player;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BindOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.player.EntityUtil;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class DropAll extends Module {
    public static DropAll INSTANCE;
    private boolean flag = false;

    public DropAll() {
        super("DropAll", Category.PLAYER);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onClickSlot(SlotActionType slotActionType, int slot, int button, int id) {
        if (slotActionType.equals(SlotActionType.THROW) && !flag) {
            Item copy = mc.player.currentScreenHandler.slots.get(slot).getStack().getItem();
            flag = true;
            for (int j = 0; j < mc.player.currentScreenHandler.slots.size(); ++j) {
                if (mc.player.currentScreenHandler.slots.get(j).getStack().getItem() == copy) {
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, j, 1, SlotActionType.THROW, mc.player);
                }
            }
            flag = false;
        }
    }
}
