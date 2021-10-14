package codersafterdark.reskillable.common.skill;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class SkillAttack extends Skill {
    public SkillAttack() {
        super(new ResourceLocation(MOD_ID, "attack"), new ResourceLocation("textures/blocks/stonebrick.png"));
        skillConfig.setLevelCap(100);
    }
}