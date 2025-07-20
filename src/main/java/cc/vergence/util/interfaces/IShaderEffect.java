package cc.vergence.util.interfaces;

import net.minecraft.client.gl.Framebuffer;

public interface IShaderEffect {
    void vergence$addFakeTargetHook(String name, Framebuffer buffer);
}