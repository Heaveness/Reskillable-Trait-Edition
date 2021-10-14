package codersafterdark.reskillable.common.advancement.professionlevel;

import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.common.lib.LibMisc;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ProfessionLevelCriterionInstance extends AbstractCriterionInstance {
    private final Profession profession;
    private final int level;

    public ProfessionLevelCriterionInstance(@Nullable Profession profession, int level) {
        super (new ResourceLocation(LibMisc.MOD_ID, "profession_level"));

        this.profession = profession;
        this.level = level;
    }

    public boolean test(final Profession profession, final int level) {
        return (this.profession == null || this.profession == profession) && level >= this.level;
    }
}
