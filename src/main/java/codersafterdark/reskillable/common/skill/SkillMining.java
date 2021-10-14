package codersafterdark.reskillable.common.skill;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class SkillMining extends Skill {
    public SkillMining() {
        super(new ResourceLocation(MOD_ID, "mining"), new ResourceLocation("textures/blocks/stone.png"));
        skillConfig.setLevelCap(100);
    }
}