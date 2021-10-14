package codersafterdark.reskillable.common.advancement.professionlevel;

import codersafterdark.reskillable.common.advancement.CriterionListeners;
import codersafterdark.reskillable.api.profession.Profession;
import net.minecraft.advancements.PlayerAdvancements;

public class ProfessionLevelListeners extends CriterionListeners<ProfessionLevelCriterionInstance> {
    public ProfessionLevelListeners(PlayerAdvancements playerAdvancements) {super(playerAdvancements);}

    public void trigger(final Profession profession, final int level) {trigger(instance -> instance.test(profession, level));}
}
