package cc.vergence.features.managers.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.eventbus.EventHandler;
import cc.vergence.features.event.events.DeathEvent;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.event.events.TotemEvent;
import cc.vergence.features.event.events.UpdateWalkingEvent;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.ArrayList;
import java.util.HashMap;

public class TotemManager implements Wrapper {
    public TotemManager() {
        Vergence.EVENTBUS.subscribe(this);
    }

    public final HashMap<String, Integer> popContainer = new HashMap<>();
    public final ArrayList<PlayerEntity> deadPlayer = new ArrayList<>();

    public Integer getPop(String s) {
        return popContainer.getOrDefault(s, 0);
    }

    public void update() {
        if (Module.isNull()) return;
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == null || player.isAlive()) {
                deadPlayer.remove(player);
                continue;
            }
            if (deadPlayer.contains(player)) {
                continue;
            }
            Vergence.EVENTBUS.post(new DeathEvent(player));
            onDeath(player);
            deadPlayer.add(player);
        }
    }

    @EventHandler
    public void updateWalking(UpdateWalkingEvent event) {
        update();
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        if (Module.isNull()) return;
        if (event.getPacket() instanceof EntityStatusS2CPacket packet) {
            if (packet.getStatus() == EntityStatuses.USE_TOTEM_OF_UNDYING) {
                Entity entity = packet.getEntity(mc.world);
                if(entity instanceof PlayerEntity player) {
                    onTotemPop(player);
                }
            }
        }
    }

    public void onDeath(PlayerEntity player) {
        popContainer.remove(player.getName().getString());
    }

    public void onTotemPop(PlayerEntity player) {
        int l_Count = 1;
        if (popContainer.containsKey(player.getName().getString())) {
            l_Count = popContainer.get(player.getName().getString());
            popContainer.put(player.getName().getString(), ++l_Count);
        } else {
            popContainer.put(player.getName().getString(), l_Count);
        }
        Vergence.EVENTBUS.post(new TotemEvent(player));
    }
}
