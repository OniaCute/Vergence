package cc.vergence.modules.visual;

import cc.vergence.features.enums.AntiCheats;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.injections.accessors.EntityAccessor;
import cc.vergence.modules.Module;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.render.Render3DUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;

public class ESP extends Module {
    public ESP() {
        super("ESP", Category.VISUAL);
    }

    ArrayList<PlayerEntity> players = new ArrayList<>();

    @Override
    public String getDetails() {
        return String.valueOf(players.size());
    }
}
