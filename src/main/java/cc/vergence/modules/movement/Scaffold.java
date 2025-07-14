package cc.vergence.modules.movement;

import cc.vergence.features.enums.AntiCheats;
import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.features.managers.MessageManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;

public class Scaffold extends Module {
    public static Scaffold INSTANCE;

    public Scaffold() {
        super("Scaffold", Category.MOVEMENT);
        INSTANCE = this;
    }

    public Option<Enum<?>> antiCheat = addOption(new EnumOption("AntiCheat", AntiCheats.Legit));

    @Override
    public String getDetails() {
        return antiCheat.getValue().name();
    }

    @Override
    public void onBlock(Module module) {
        MessageManager.blockedMessage(this, module);
    }

    @Override
    public void onMoveEvent(MoveEvent event, double x, double y, double z) {
        MessageManager.newMessage(this, "Gotten moved position, x:" + x + " y:" + y + " z:" + z);
    }
}
