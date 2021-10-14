package codersafterdark.reskillable.common.profession.mage.monk;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentMonk extends Talent {

    public TalentMonk() {
        super(new ResourceLocation(MOD_ID, "monk"), 1, 2, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "monk"),
                1, "unobtainable");
    }
}
