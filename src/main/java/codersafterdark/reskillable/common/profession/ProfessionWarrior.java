package codersafterdark.reskillable.common.profession;

import codersafterdark.reskillable.api.profession.Profession;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class ProfessionWarrior extends Profession {
    public ProfessionWarrior() {
        super(new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "textures/gui/profession_bg/onyx_block.png"));
        this.setGuiIndex(0);
        setColor(14033937);
        addSubProfession("warden", 0);
        addSubProfession("gladiator", 1);
        addSubProfession("berserker", 2);
    }
}
