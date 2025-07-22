package cc.vergence.injections.mixins.render;

import cc.vergence.modules.misc.NameProtect;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.text.TextVisitFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextVisitFactory.class)
public class MixinTextVisitFactory implements Wrapper {
    @ModifyVariable(method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static String replaceText(String value) {
        if (NameProtect.INSTANCE != null && NameProtect.INSTANCE.getStatus()) {
            return value.replaceAll(mc.getSession().getUsername(), NameProtect.INSTANCE.nickname.getValue());
        }
        return value;
    }
}
