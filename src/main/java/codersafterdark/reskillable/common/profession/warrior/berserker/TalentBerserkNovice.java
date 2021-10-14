package codersafterdark.reskillable.common.profession.warrior.berserker;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentBerserkNovice extends Talent {
    public TalentBerserkNovice() {
        super(new ResourceLocation(MOD_ID, "berserk_novice"), 1, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "profession|reskillable:warrior|6", "reskillable:attack|8");
    }

}
