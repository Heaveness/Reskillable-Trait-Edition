package codersafterdark.reskillable.common.profession;

import codersafterdark.reskillable.api.profession.Profession;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class ProfessionTinkerer extends Profession {
    public ProfessionTinkerer() {
        super(new ResourceLocation(MOD_ID, "tinkerer"), new ResourceLocation("textures/blocks/stonebrick.png"));
        this.setGuiIndex(5);
        setColor(14053905);
        addSubProfession("engineer", 0);
        addSubProfession("blacksmith", 2);
        setOffense(false);
    }
}
