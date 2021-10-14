package codersafterdark.reskillable.api.talent;

import net.minecraft.util.ResourceLocation;

public class TalentActive extends Talent {
    private int cooldown;
    private final int defaultCooldown;

    public TalentActive(ResourceLocation name, int x, int y, ResourceLocation professionName, ResourceLocation subProfessionName, int cost, int defaultCooldown, String... defaultRequirements) {
        super(name, x, y, professionName, subProfessionName, cost, defaultRequirements);
        this.defaultCooldown = defaultCooldown;
    }

    public void setCooldown(int i) {
        this.cooldown = i;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getDefaultCooldown() {
        return defaultCooldown;
    }

}
