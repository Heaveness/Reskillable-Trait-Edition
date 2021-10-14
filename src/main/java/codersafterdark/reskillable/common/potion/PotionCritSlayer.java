package codersafterdark.reskillable.common.potion;

import codersafterdark.reskillable.common.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionCritSlayer extends Potion {

    public PotionCritSlayer(String name, boolean isBadPotion, int color, int iconIndexX, int iconIndexY) {
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

}
