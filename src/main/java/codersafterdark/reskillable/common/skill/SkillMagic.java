package codersafterdark.reskillable.common.skill;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class SkillMagic extends Skill {
    public SkillMagic() {
        super(new ResourceLocation(MOD_ID, "magic"), new ResourceLocation("textures/blocks/end_stone.png"));
        skillConfig.setLevelCap(100);
    }
}