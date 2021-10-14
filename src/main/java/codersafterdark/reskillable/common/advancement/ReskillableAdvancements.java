package codersafterdark.reskillable.common.advancement;

import codersafterdark.reskillable.common.advancement.professionlevel.ProfessionLevelTrigger;
import codersafterdark.reskillable.common.advancement.skilllevel.SkillLevelTrigger;
import codersafterdark.reskillable.common.advancement.trait.UnlockUnlockableTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class ReskillableAdvancements {
    public static final ProfessionLevelTrigger PROFESSION_LEVEL = new ProfessionLevelTrigger();
    public static final SkillLevelTrigger SKILL_LEVEL = new SkillLevelTrigger();
    public static final UnlockUnlockableTrigger UNLOCK_UNLOCKABLE = new UnlockUnlockableTrigger();

    public static void preInit() {
        CriteriaTriggers.register(PROFESSION_LEVEL);
        CriteriaTriggers.register(SKILL_LEVEL);
        CriteriaTriggers.register(UNLOCK_UNLOCKABLE);
    }
}
