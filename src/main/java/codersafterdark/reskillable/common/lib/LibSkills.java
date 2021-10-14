package codersafterdark.reskillable.common.lib;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

public class LibSkills {

    // SKILLS
    @ObjectHolder("reskillable:farming")
    public static Skill farming = null;


    // TRAITS
    @ObjectHolder("reskillable:seasonal_greetings")
    public static Trait traitSeasons = null;
}
