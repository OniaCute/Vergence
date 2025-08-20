package cc.vergence.features.managers.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.eventbus.EventHandler;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.render.utils.FadeUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.concurrent.ConcurrentHashMap;

public class MineManager implements Wrapper {
    private boolean mining;

    public MineManager() {
        Vergence.EVENTBUS.subscribe(this);
    }

    public final ConcurrentHashMap<Integer, MineData> breakMap = new ConcurrentHashMap<>();

    @EventHandler
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof BlockBreakingProgressS2CPacket packet) {
            if (packet.getPos() == null) return;
            MineData mineData = new MineData(packet.getPos(), packet.getEntityId());
            if (breakMap.containsKey(packet.getEntityId()) && breakMap.get(packet.getEntityId()).pos.equals(packet.getPos())) {
                return;
            }
            if (mineData.getEntity() == null) {
                return;
            }
            if (MathHelper.sqrt((float) mineData.getEntity().getEyePos().squaredDistanceTo(packet.getPos().toCenterPos())) > 8) {
                return;
            }
            breakMap.put(packet.getEntityId(), mineData);
        }
    }

    public void setMining(boolean mining) {
        this.mining = mining;
    }

    public boolean isMining() {
        return mining;
    }

    public boolean isMining(BlockPos pos) {
        return isMining(pos, true) ;
    }

    public boolean isMining(BlockPos pos, boolean self) {
        if (Module.isNull()) {
            return false;
        }

//        if (self && PacketMine.getBreakPos() != null && PacketMine.getBreakPos().equals(pos)) {
//            return true;
//        }

        for (MineData mineData : breakMap.values()) {
            if (mineData.getEntity() == null) {
                continue;
            }
            if (mineData.getEntity().getEyePos().distanceTo(pos.toCenterPos()) > 7) {
                continue;
            }
            if (mineData.pos.equals(pos)) {
                return self && mc.player.getId() == mineData.entityId;
            }
        }

        return false;
    }
    public static class MineData {
        public final BlockPos pos;
        public final int entityId;
        public final FadeUtil fade;
        public final FastTimerUtil timer;
        public MineData(BlockPos pos, int entityId) {
            this.pos = pos;
            this.entityId = entityId;
            this.fade = new FadeUtil(0);
            this.timer = new FastTimerUtil();
        }

        public Entity getEntity() {
            if (mc.world == null) return null;
            Entity entity = mc.world.getEntityById(entityId);
            if (entity instanceof PlayerEntity) {
                return entity;
            }
            return null;
        }
    }
}
