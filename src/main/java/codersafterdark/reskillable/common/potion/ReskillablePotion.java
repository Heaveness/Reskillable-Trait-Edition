package codersafterdark.reskillable.common.potion;

import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ReskillablePotion {
    public static final Potion POTION_RESIST_EFFECT = new PotionResist("damage_resist", false, 5925256, 0, 0)
            .registerPotionAttributeModifier(ReskillableAttributes.DAMAGE_RESIST, MathHelper.getRandomUUID().toString(), 10.0D, 0);
    public static final Potion POTION_DARKNESS_EFFECT = new PotionDarkness("darkness", false, 738644, 1, 0)
            .registerPotionAttributeModifier(ReskillableAttributes.CRIT_DAMAGE, MathHelper.getRandomUUID().toString(), 0.5D, 2);
    public static final Potion POTION_EDGE_EFFECT = new PotionEdge("edge", false, 738644, 2, 0)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, MathHelper.getRandomUUID().toString(), 0.2D, 2)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, MathHelper.getRandomUUID().toString(), 0.5D, 2)
            .registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, MathHelper.getRandomUUID().toString(), 0.1D, 2);
    public static final Potion POTION_BLEED_EFFECT = new PotionBleed("bleed", true, 16711680, 3, 0);
    public static final Potion POTION_CRIT_SLAYER_EFFECT = new PotionCritSlayer("crit_slayer", false, 16711680, 4, 0)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, MathHelper.getRandomUUID().toString(), 0.20D, 2);
    public static final Potion POTION_SPRING_HEEL_EFFECT = new PotionSpringHeel("spring_heel", false, 16711680, 5, 0)
            .registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, MathHelper.getRandomUUID().toString(), 0.1D, 2);

    public static void registerPotions() {
        registerPotion(POTION_DARKNESS_EFFECT);
        registerPotion(POTION_BLEED_EFFECT);
        registerPotion(POTION_CRIT_SLAYER_EFFECT);
    }

    public static void registerPotion(Potion effect) {
        ForgeRegistries.POTIONS.register(effect);
    }

}
