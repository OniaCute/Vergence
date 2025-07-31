package cc.vergence.util.rotation;

import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;

import java.util.HashSet;
import java.util.Set;

public class RotateUtil implements Wrapper {
    public static float getPlayerPitch() {
        return  mc.player == null ? 0 : mc.player.getPitch();
    }

    public static double getPlayerYaw() {
        return mc.player == null ? 0 : mc.player.getY();
    }

    public static float getPlayerPitch(PlayerEntity player) {
        return  player == null ? 0 : player.getPitch();
    }

    public static double getPlayerYaw(PlayerEntity player) {
        return  player == null ? 0 : player.getYaw();
    }

    public static float[] getRotationsTo(Vec3d src, Vec3d dest) {
        float yaw = (float) (Math.toDegrees(Math.atan2(dest.subtract(src).z, dest.subtract(src).x)) - 90);
        float pitch = (float) Math.toDegrees(-Math.atan2(dest.subtract(src).y, Math.hypot(dest.subtract(src).x, dest.subtract(src).z)));
        return new float[] {MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }

    public static Vec2f getRotationVec2f(Vec3d posFrom, Vec3d posTo) {
        Vec3d vec3d = posTo.subtract(posFrom);
        return getRotationFromVec(vec3d);
    }

    private static Vec2f getRotationFromVec(Vec3d vec) {
        double d = vec.x;
        double d2 = vec.z;
        double xz = Math.hypot(d, d2);
        d2 = vec.z;
        double d3 = vec.x;
        double yaw = humanizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - 90.0);
        double pitch = humanizeAngle(Math.toDegrees(-Math.atan2(vec.y, xz)));
        return new Vec2f((float) yaw, (float) pitch);
    }

    private static double humanizeAngle(double angleIn) {
        double angle = angleIn;
        if ((angle %= 360.0) >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    public static Direction getInteractDirection(final BlockPos blockPos, final boolean strictDirection) {
        Set<Direction> ncpDirections = getPlaceDirectionsNCP(mc.player.getEyePos(), blockPos.toCenterPos());
        Direction interactDirection = null;
        for (final Direction direction : Direction.values()) {
            final BlockState state = mc.world.getBlockState(blockPos.offset(direction));
            if (state.isAir() || !state.getFluidState().isEmpty()) {
                continue;
            }
            if (strictDirection && !ncpDirections.contains(direction.getOpposite())) {
                continue;
            }
            interactDirection = direction;
            break;
        }
        if (interactDirection == null) {
            return null;
        }
        return interactDirection.getOpposite();
    }

    public Direction getPlaceDirectionNCP(BlockPos blockPos, boolean visible) {
        Vec3d eyePos = new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getStandingEyeHeight(), mc.player.getZ());
        if (blockPos.getX() == eyePos.getX() && blockPos.getY() == eyePos.getY() && blockPos.getZ() == eyePos.getZ()) {
            return Direction.DOWN;
        } else {
            Set<Direction> ncpDirections = getPlaceDirectionsNCP(eyePos, blockPos.toCenterPos());
            for (Direction dir : ncpDirections) {
                if (visible && !mc.world.isAir(blockPos.offset(dir))) {
                    continue;
                }
                return dir;
            }
        }
        return Direction.UP;
    }

    public static Set<Direction> getPlaceDirectionsNCP(Vec3d eyePos, Vec3d blockPos) {
        return getPlaceDirectionsNCP(eyePos.x, eyePos.y, eyePos.z, blockPos.x, blockPos.y, blockPos.z);
    }

    public Direction getPlaceDirectionGrim(BlockPos blockPos) {
        Set<Direction> directions = getPlaceDirectionsGrim(mc.player.getPos(), blockPos);
        return directions.stream().findAny().orElse(Direction.UP);
    }

    public Set<Direction> getPlaceDirectionsGrim(Vec3d eyePos, BlockPos blockPos) {
        return getPlaceDirectionsGrim(eyePos.x, eyePos.y, eyePos.z, blockPos);
    }

    public Set<Direction> getPlaceDirectionsGrim(final double x, final double y, final double z, BlockPos pos) {
        final Set<Direction> dirs = new HashSet<>(6);
        Box combined = getCombinedBox(pos);
        Box eyePositions = new Box(x, y + 0.4, z, x, y + 1.62, z).expand(0.0002);
        if (eyePositions.minZ <= combined.minZ) {
            dirs.add(Direction.NORTH);
        }
        if (eyePositions.maxZ >= combined.maxZ) {
            dirs.add(Direction.SOUTH);
        }
        if (eyePositions.maxX >= combined.maxX) {
            dirs.add(Direction.EAST);
        }
        if (eyePositions.minX <= combined.minX) {
            dirs.add(Direction.WEST);
        }
        if (eyePositions.maxY >= combined.maxY) {
            dirs.add(Direction.UP);
        }
        if (eyePositions.minY <= combined.minY) {
            dirs.add(Direction.DOWN);
        }
        return dirs;
    }

    public static Set<Direction> getPlaceDirectionsNCP(final double x, final double y, final double z,
                                                       final double dx, final double dy, final double dz) {
        // directly from NCP src
        final double xdiff = x - dx;
        final double ydiff = y - dy;
        final double zdiff = z - dz;
        final Set<Direction> dirs = new HashSet<>(6);
        if (ydiff > 0.5) {
            dirs.add(Direction.UP);
        } else if (ydiff < -0.5) {
            dirs.add(Direction.DOWN);
        } else {
            dirs.add(Direction.UP);
            dirs.add(Direction.DOWN);
        }
        if (xdiff > 0.5) {
            dirs.add(Direction.EAST);
        } else if (xdiff < -0.5) {
            dirs.add(Direction.WEST);
        } else {
            dirs.add(Direction.EAST);
            dirs.add(Direction.WEST);
        }
        if (zdiff > 0.5) {
            dirs.add(Direction.SOUTH);
        } else if (zdiff < -0.5) {
            dirs.add(Direction.NORTH);
        } else {
            dirs.add(Direction.SOUTH);
            dirs.add(Direction.NORTH);
        }
        return dirs;
    }

    public boolean isInEyeRange(final BlockPos pos) {
        return pos.getY() > mc.player.getY() + mc.player.getStandingEyeHeight();
    }

    private Box getCombinedBox(BlockPos pos) {
        VoxelShape shape = mc.world.getBlockState(pos).getCollisionShape(mc.world, pos).offset(pos.getX(), pos.getY(), pos.getZ());
        Box combined = new Box(pos);
        for (Box box : shape.getBoundingBoxes()) {
            double minX = Math.max(box.minX, combined.minX);
            double minY = Math.max(box.minY, combined.minY);
            double minZ = Math.max(box.minZ, combined.minZ);
            double maxX = Math.min(box.maxX, combined.maxX);
            double maxY = Math.min(box.maxY, combined.maxY);
            double maxZ = Math.min(box.maxZ, combined.maxZ);
            combined = new Box(minX, minY, minZ, maxX, maxY, maxZ);
        }
        return combined;
    }
}
