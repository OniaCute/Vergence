package cc.vergence.util.interfaces;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Random;

public interface Wrapper {
    MinecraftClient mc = MinecraftClient.getInstance();
    Random RANDOM = new Random();
}
