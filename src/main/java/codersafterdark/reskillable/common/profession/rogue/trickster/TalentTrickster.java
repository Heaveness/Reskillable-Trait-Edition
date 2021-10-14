package codersafterdark.reskillable.common.profession.rogue.trickster;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentTrickster extends Talent {

    public TalentTrickster() {
        super(new ResourceLocation(MOD_ID, "trickster"), 1, 2, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "trickster"),
                1, "unobtainable");
    }

}
