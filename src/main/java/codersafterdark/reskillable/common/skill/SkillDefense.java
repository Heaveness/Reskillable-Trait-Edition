package codersafterdark.reskillable.common.skill;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class SkillDefense extends Skill {
    public SkillDefense() {
        super(new ResourceLocation(MOD_ID, "defense"), new ResourceLocation("textures/blocks/quartz_block_side.png"));
        skillConfig.setLevelCap(100);
    }
}