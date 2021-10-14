package codersafterdark.reskillable.common.profession;

import codersafterdark.reskillable.api.profession.Profession;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class ProfessionGatherer extends Profession {
    public ProfessionGatherer() {
        super(new ResourceLocation(MOD_ID, "gatherer"), new ResourceLocation("textures/blocks/stone.png"));
        this.setGuiIndex(3);
        setColor(6411029);
        addSubProfession("miner", 0);
        addSubProfession("seafarer", 1);
        addSubProfession("forager", 2);
        setOffense(false);
    }
}
