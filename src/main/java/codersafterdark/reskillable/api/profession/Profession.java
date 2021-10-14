package codersafterdark.reskillable.api.profession;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.lib.LibMisc;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class Profession extends IForgeRegistryEntry.Impl<Profession> implements Comparable<Profession> {
    private final Map<Integer, ResourceLocation> customSprites = new HashMap<>();
    private final List<Talent> talents = new ArrayList<>();
    private final ResourceLocation spriteLocation;
    private final String name;
    protected ResourceLocation background;
    protected ProfessionConfig professionConfig;
    private List<SubProfession> subProfessions = new ArrayList<>();
    private boolean offense;
    private boolean hidden;
    private int guiIndex;
    private int color;

    public Profession(ResourceLocation name, ResourceLocation background) {
        this.name = name.toString().replace(":", ".");
        this.background = background;
        this.spriteLocation = new ResourceLocation(name.getNamespace(), "textures/professions/" + name.getPath() + ".png");
        this.setRegistryName(name);
        this.professionConfig = ReskillableAPI.getInstance().getProfessionConfig(name);
        this.offense = true;
    }

    public void addSubProfession(String subProfession, int guiIndex) {
        this.subProfessions.add(new SubProfession(this, new ResourceLocation(LibMisc.MOD_ID, subProfession), guiIndex));
    }

    public List<SubProfession> getAllSubProfessions() {
        return subProfessions;
    }

    public void addTalent(Talent talent) {talents.add(talent);}

    public List<Talent> getTalents() {return talents;}

    public String getKey() {return name;}

    public String getName() {
        return new TextComponentTranslation("reskillable.profession." + getKey()).getUnformattedComponentText();
    }

    public ResourceLocation getBackground() {
        return background;
    }

    public void setBackground(ResourceLocation resourceLocation) {
        this.background = resourceLocation;
    }

    public int getCap() {
        return professionConfig.getLevelCap();
    }

    public boolean isEnabled() {
        return professionConfig.isEnabled();
    }

    public boolean hasLevelButton() {
        return professionConfig.hasLevelButton();
    }

    public String getDescription() {
        return new TextComponentTranslation("reskillable.profession." + getKey() + ".desc").getUnformattedComponentText();
    }

    public int getColor() { return this.color;}

    public void setColor(int color) {this.color = color;}

    public ResourceLocation getSpriteLocation() {
        return spriteLocation;
    }

    public void setOffense(boolean bool) {
        this.offense = bool;
    }

    public boolean isOffense() {
        return this.offense;
    }

    public Pair<Integer, Integer> getSpriteFromRank(int rank) {
        //TODO: If we ever end up having more images than 4 when the Math.min is changed make sure to also change the value rank is divided by
        return new MutablePair<>(Math.min(rank / 2, 3) * 16, 0);
    }

    public void setCustomSprite(int rank, ResourceLocation location) {
        customSprites.put(rank, location);
    }

    public void removeCustomSprite(int rank) {
        customSprites.remove(rank);
    }

    public ResourceLocation getSpriteLocation(int rank) {
        if (customSprites.containsKey(rank)) {
            return customSprites.get(rank);
        }
        for (int i = rank - 1; i >= 0; i--) {
            if (customSprites.containsKey(i)) {
                return customSprites.get(i);
            }
        }
        return null;
    }

    public boolean hasCustomSprites() {
        return !customSprites.isEmpty();
    }

    @Override
    public int compareTo(@Nonnull Profession o) {
        return o.getName().compareTo(this.getName());
    }

    public int getSkillPointInterval() {
        return professionConfig.getSkillPointInterval();
    }

    public int getLevelUpCost(int level) {
        int cost = this.professionConfig.getLevelStaggering()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() < level + 1)
                .mapToInt(Map.Entry::getValue)
                .sum() + this.professionConfig.getBaseLevelCost();
        return cost < 0 ? 0 : cost;
    }

    public final ProfessionConfig getProfessionConfig() {
        return professionConfig;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getRank(int level) {
        return 8 * level / getCap();
    }

    public int getGuiIndex() {return guiIndex;}

    public void setGuiIndex(int index) {this.guiIndex = index;}

    public static class SubProfession {
        private final String name;
        private final String resourcePath;
        private int guiIndex;
        private Profession profession;

        public SubProfession(Profession profession, ResourceLocation name, int guiIndex) {
            this.name = name.toString().replace(":", ".");
            this.resourcePath = name.getPath();
            this.guiIndex = guiIndex;
            this.profession = profession;
        }

        public String getName() {
            return name;
        }

        public String getUnformattedName() {
            return resourcePath;
        }

        public String getRegistryName() {
            return "reskillable.subprofession.reskillable." + this.resourcePath;
        }

        public Profession getProfession() {
            return profession;
        }

        public void setProfession(Profession profession) {
            this.profession = profession;
        }

        public int getGuiIndex() {
            return guiIndex;
        }

        public void setGuiIndex(int guiIndex) {
            this.guiIndex = guiIndex;
        }

    }
}
