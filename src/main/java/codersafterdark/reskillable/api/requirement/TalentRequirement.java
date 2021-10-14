package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.Objects;

public class TalentRequirement extends Requirement {
    private Talent talent;

    public TalentRequirement(Talent talent) {
        this.talent = talent;
        this.tooltip = TextFormatting.GRAY + " - " + TextFormatting.LIGHT_PURPLE + new TextComponentTranslation("reskillable.requirements.format.talent", "%s",
                this.talent.getName()).getUnformattedComponentText();
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayer) {
        return PlayerDataHandler.get(entityPlayer).getProfessionInfo(talent.getParentProfession()).isUnlocked(talent);
    }

    public Profession getProfession() {
        return talent.getParentProfession();
    }

    public Talent getTalent() {
        return talent;
    }

    @Override
    public RequirementComparison matches(Requirement other) {
        return other instanceof TalentRequirement ? talent.getKey().equals(((TalentRequirement) other).talent.getKey()) ?
                RequirementComparison.EQUAL_TO : RequirementComparison.NOT_EQUAL : RequirementComparison.NOT_EQUAL;
    }

    @Override
    public boolean isEnabled() {
        return talent != null && talent.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof TalentRequirement && talent.equals(((TalentRequirement) o).talent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(talent);
    }

}
