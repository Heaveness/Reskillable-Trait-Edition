package codersafterdark.reskillable.common.profession.rogue.archer;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentResourceful extends Talent {

    public TalentResourceful() {
        super(new ResourceLocation(MOD_ID, "resourceful"), 2, 4, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "archer"), 3, "profession|reskillable:rogue|13");
        MinecraftForge.EVENT_BUS.register(this);
    }
}
