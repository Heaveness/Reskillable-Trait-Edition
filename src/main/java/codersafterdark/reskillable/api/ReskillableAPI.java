package codersafterdark.reskillable.api;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.profession.ProfessionConfig;
import codersafterdark.reskillable.api.requirement.*;
import codersafterdark.reskillable.api.requirement.logic.LogicParser;
import codersafterdark.reskillable.api.skill.SkillConfig;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.api.talent.TalentConfig;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.api.unlockable.UnlockableConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.util.Objects;

public class ReskillableAPI {
    private static ReskillableAPI instance;
    private IModAccess modAccess;
    private RequirementRegistry requirementRegistry;

    public ReskillableAPI(IModAccess modAccess) {
        this.modAccess = modAccess;
        this.requirementRegistry = new RequirementRegistry();
        requirementRegistry.addRequirementHandler("adv", input -> new AdvancementRequirement(new ResourceLocation(input)));
        requirementRegistry.addRequirementHandler("trait", input -> {
            Unlockable unlockable = ReskillableRegistries.UNLOCKABLES.getValue(new ResourceLocation(input));
            if (unlockable == null) {
                throw new RequirementException("Unlockable '" + input + "' not found.");
            }
            return new TraitRequirement(unlockable);
        });
        requirementRegistry.addRequirementHandler("profession", input -> {
            String[] requirements = input.split("\\|");
            if (requirements.length == 2) {
                Profession profession = ReskillableRegistries.PROFESSIONS.getValue(new ResourceLocation(requirements[0]));
                String requirementInputs = requirements[1];

                if (profession == null) {
                    throw new RequirementException("Profession '" + input + "' not found.");
                }

                try {
                    int level = Integer.parseInt(requirementInputs);
                    if (level > 1) {
                        return new ProfessionRequirement(profession, level);
                    } else {
                        throw new RequirementException("Level must be greater than 1. Found: '" + level + "'.");
                    }
                } catch (NumberFormatException e) {
                    throw new RequirementException("Invalid level '" + requirementInputs + "' for profession '" + profession.getName() + "'.");
                }
            }
            return null;
        });
        requirementRegistry.addRequirementHandler("talent", input -> {
            Talent talent = ReskillableRegistries.TALENTS.getValue(new ResourceLocation(input));
            if (talent == null) {
                throw new RequirementException("Talent '" + input + "' not found.");
            }
            return new TalentRequirement(talent);
        });
        requirementRegistry.addRequirementHandler("unobtainable", input -> new UnobtainableRequirement());
        requirementRegistry.addRequirementHandler("none", input -> new NoneRequirement()); //Makes it so other requirements are ignored

        //Logic Requirements
        requirementRegistry.addRequirementHandler("not", LogicParser::parseNOT);
        requirementRegistry.addRequirementHandler("and", LogicParser::parseAND);
        requirementRegistry.addRequirementHandler("nand", LogicParser::parseNAND);
        requirementRegistry.addRequirementHandler("or", LogicParser::parseOR);
        requirementRegistry.addRequirementHandler("nor", LogicParser::parseNOR);
        requirementRegistry.addRequirementHandler("xor", LogicParser::parseXOR);
        requirementRegistry.addRequirementHandler("xnor", LogicParser::parseXNOR);
    }

    public static ReskillableAPI getInstance() {
        return Objects.requireNonNull(instance, "Calling Reskillable API Instance before it's creation");
    }

    public static void setInstance(ReskillableAPI reskillableAPI) {
        instance = reskillableAPI;
    }

    public ProfessionConfig getProfessionConfig(ResourceLocation name) {return modAccess.getProfessionConfig(name);}

    public SkillConfig getSkillConfig(ResourceLocation name) {
        return modAccess.getSkillConfig(name);
    }

    public UnlockableConfig getTraitConfig(ResourceLocation name, int x, int y, int cost, String[] defaultRequirements) {
        return modAccess.getUnlockableConfig(name, x, y, cost, defaultRequirements);
    }

    public TalentConfig getTalentConfig(ResourceLocation name, int x, int y, int cost, String[] defaultRequirements) {
        return modAccess.getTalentConfig(name, x, y, cost, defaultRequirements);
    }

    public void syncPlayerData(EntityPlayer entityPlayer, PlayerData playerData) {
        modAccess.syncPlayerData(entityPlayer, playerData);
    }

    public AdvancementProgress getAdvancementProgress(EntityPlayer entityPlayer, Advancement advancement) {
        return modAccess.getAdvancementProgress(entityPlayer, advancement);
    }

    public RequirementRegistry getRequirementRegistry() {
        return requirementRegistry;
    }

    public void log(Level warn, String s) {
        modAccess.log(warn, s);
    }
}