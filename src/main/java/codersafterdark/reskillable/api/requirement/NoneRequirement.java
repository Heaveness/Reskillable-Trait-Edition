package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

//This has the same body as a TrueRequirement, except that it should not simplify out
public final class NoneRequirement extends Requirement {
    public NoneRequirement() {
        this.tooltip = TextFormatting.GREEN + new TextComponentTranslation("reskillable.requirements.format.unobtainable").getUnformattedComponentText();
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayerMP) {
        return true;
    }

    @Override
    public String getToolTip(PlayerData data) {
        return tooltip;
    }

    @Override
    public RequirementComparison matches(Requirement other) {
        return other instanceof NoneRequirement ? RequirementComparison.EQUAL_TO : RequirementComparison.NOT_EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NoneRequirement;
    }

    @Override
    public int hashCode() {
        //Does not actually matter but might as well have it be the same for each none requirement
        return 2;
    }
}