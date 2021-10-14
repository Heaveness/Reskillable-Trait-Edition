package codersafterdark.reskillable.api.requirement.logic.impl;

import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparison;
import codersafterdark.reskillable.api.requirement.logic.DoubleRequirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;

public class XNORRequirement extends DoubleRequirement {
    public XNORRequirement(Requirement left, Requirement right) {
        super(left, right);
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer player) {
        return leftAchieved(player) == rightAchieved(player);
    }

    @Override
    protected String getFormat() {
        return new TextComponentTranslation("reskillable.requirements.format.xnor").getUnformattedComponentText();
    }

    @Override
    public RequirementComparison matches(Requirement o) {
        if (o instanceof ORRequirement) {
            ORRequirement other = (ORRequirement) o;
            RequirementComparison left = getLeft().matches(other.getLeft());
            RequirementComparison right = getRight().matches(other.getRight());
            boolean same = left.equals(right);
            if (same && left.equals(RequirementComparison.EQUAL_TO)) {
                return RequirementComparison.EQUAL_TO;
            }

            //Check to see if they were just written in the opposite order
            RequirementComparison leftAlt = getLeft().matches(other.getRight());
            RequirementComparison rightAlt = getRight().matches(other.getLeft());
            boolean altSame = leftAlt.equals(rightAlt);
            if (altSame && leftAlt.equals(RequirementComparison.EQUAL_TO)) {
                return RequirementComparison.EQUAL_TO;
            }

            //XNOR specific check

        }
        return RequirementComparison.NOT_EQUAL;
    }
}