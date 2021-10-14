package codersafterdark.reskillable.common.potion;

import codersafterdark.reskillable.common.core.CommonProxy;
import codersafterdark.reskillable.common.lib.LibMisc;
import codersafterdark.reskillable.common.util.DamageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

public class PotionBleed extends Potion {
    public PotionBleed(String name, boolean isBadPotion, int color, int iconIndexX, int iconIndexY) {
        super(isBadPotion, color);
        setPotionName("effect." + name);
        setIconIndex(iconIndexX, iconIndexY);
        setRegistryName(new ResourceLocation(LibMisc.MOD_ID, name));
    }

    @Override
    public boolean hasStatusIcon() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(LibMisc.MOD_ID, "textures/gui/potion_effects.png"));
        return true;
    }

    @Override
    public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
        if (entity instanceof EntityPlayer && !entity.getEntityWorld().isRemote &&
        entity.getEntityWorld() instanceof WorldServer &&
        entity.getEntityWorld().getMinecraftServer().isPVPEnabled()) {
            return;
        }

        int preTime = entity.hurtResistantTime;
        DamageUtil.attackEntityFrom(entity, CommonProxy.dmgSourceBleed, 0.5F * (amplifier + 1));
        entity.hurtResistantTime = Math.max(preTime, entity.hurtResistantTime);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}
