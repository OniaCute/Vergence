package cc.vergence.injections.mixins;

import cc.vergence.util.interfaces.IChatHudLine;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChatHudLine.Visible.class)
public class MixinChatHudLineVisible implements IChatHudLine {
    @Unique
    private int id = 0;
    @Override
    public int emotion$getId() {
        return id;
    }

    @Override
    public void emotion$setId(int id) {
        this.id = id;
    }
}
