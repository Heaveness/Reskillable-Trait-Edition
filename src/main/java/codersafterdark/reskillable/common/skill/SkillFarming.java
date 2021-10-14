package codersafterdark.reskillable.common.skill;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class SkillFarming extends Skill {
    public SkillFarming() {
        super(new ResourceLocation(MOD_ID, "farming"), new ResourceLocation("textures/blocks/dirt.png"));
        skillConfig.setLevelCap(100);
    }
}