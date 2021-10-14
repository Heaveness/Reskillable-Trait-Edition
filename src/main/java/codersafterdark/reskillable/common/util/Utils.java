package codersafterdark.reskillable.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.vecmath.Vector3d;

public class Utils {

    public static @Nonnull Vector3d getEyePosition(@Nonnull EntityPlayer player) {
        Vector3d res = new Vector3d(player.posX, player.posY, player.posZ);
        res.y += player.getEyeHeight();
        return res;
    }

    public static @Nonnull Vector3d getLookVec(@Nonnull EntityPlayer player) {
        Vec3d lv = player.getLookVec();
        return new Vector3d(lv.x, lv.y, lv.z);
    }
}
