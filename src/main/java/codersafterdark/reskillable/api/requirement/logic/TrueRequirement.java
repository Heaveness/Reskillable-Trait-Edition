package codersafterdark.reskillable.api.requirement.logic;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparison;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class TrueRequirement extends Requirement {
    public TrueRequirement() {
        this.tooltip = TextFormatting.GREEN + new TextComponentTranslation("reskillable.requirements.format.unobtainable").getUnformattedComponentText();
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayerMP) {
        return true;
    }

    @Override
    public String getToolTip(PlayerData data) {
        //Should never be needed but probably should be set anyways
        return tooltip;
    }

    @Override
    public RequirementComparison matches(Requirement other) {
        return other instanceof TrueRequirement ? RequirementComparison.EQUAL_TO : RequirementComparison.NOT_EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TrueRequirement;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}