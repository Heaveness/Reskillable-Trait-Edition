package codersafterdark.reskillable.common.profession.warrior.gladiator;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentGladiator extends Talent {

    public TalentGladiator() {
        super(new ResourceLocation(MOD_ID, "gladiator"), 1, 2, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "gladiator"),
                1, "unobtainable");
    }

}