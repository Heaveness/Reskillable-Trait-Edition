package codersafterdark.reskillable.api.requirement.logic;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparison;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class FalseRequirement extends Requirement {
    public FalseRequirement() {
        this.tooltip = TextFormatting.RED + new TextComponentTranslation("reskillable.requirements.format.unobtainable").getUnformattedComponentText();
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayerMP) {
        return false;
    }

    @Override
    public String getToolTip(PlayerData data) {
        return tooltip;
    }

    @Override
    public RequirementComparison matches(Requirement other) {
        return other instanceof FalseRequirement ? RequirementComparison.EQUAL_TO : RequirementComparison.NOT_EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FalseRequirement;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}