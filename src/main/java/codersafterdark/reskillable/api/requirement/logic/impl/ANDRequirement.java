package codersafterdark.reskillable.api.requirement.logic.impl;

import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparison;
import codersafterdark.reskillable.api.requirement.logic.DoubleRequirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;

public class ANDRequirement extends DoubleRequirement {
    public ANDRequirement(Requirement left, Requirement right) {
        super(left, right);
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer player) {
        return leftAchieved(player) && rightAchieved(player);
    }

    @Override
    protected String getFormat() {
        return new TextComponentTranslation("reskillable.requirements.format.and").getUnformattedComponentText();
    }

    //TODO: Figure out how to implement this in the other logic requirements for if the elements are not just the same
    @Override
    public RequirementComparison matches(Requirement o) {
        if (o instanceof ANDRequirement) {
            ANDRequirement other = (ANDRequirement) o;
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

            //AND specific check
            //Check to see if one is greater/less than for both sub requirements
            if ((same && left.equals(RequirementComparison.GREATER_THAN)) || (altSame && leftAlt.equals(RequirementComparison.GREATER_THAN))) {
                return RequirementComparison.GREATER_THAN;
            } else if ((same && left.equals(RequirementComparison.LESS_THAN)) || (altSame && leftAlt.equals(RequirementComparison.LESS_THAN))) {
                return RequirementComparison.LESS_THAN;
            }
        }
        return RequirementComparison.NOT_EQUAL;
    }
}