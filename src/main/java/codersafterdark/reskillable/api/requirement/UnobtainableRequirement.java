package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

//Needs to be a separate object from FalseRequirement, so that it doesn't always get trimmed out of logic requirements
public class UnobtainableRequirement extends Requirement {
    public UnobtainableRequirement() {
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
        return other instanceof UnobtainableRequirement ? RequirementComparison.EQUAL_TO : RequirementComparison.NOT_EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UnobtainableRequirement;
    }

    @Override
    public int hashCode() {
        return -1;
    }
}