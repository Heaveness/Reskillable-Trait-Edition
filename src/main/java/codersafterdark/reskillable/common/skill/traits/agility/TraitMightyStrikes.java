package codersafterdark.reskillable.common.skill.traits.agility;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.event.LockUnlockableEvent;
import codersafterdark.reskillable.api.event.UnlockUnlockableEvent;
import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.common.core.handler.MathHelper;
import codersafterdark.reskillable.common.lib.LibMisc;
import codersafterdark.reskillable.common.skill.SkillAgility;
import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class TraitMightyStrikes extends Trait {
    IAttribute critDamageMod = ReskillableAttributes.CRIT_DAMAGE;
    private UUID modifierID = null;

    public TraitMightyStrikes() {
        super(new ResourceLocation(LibMisc.MOD_ID, "mighty_strikes"), 3, 3, new ResourceLocation("reskillable", "agility"), 1, "reskillable:agility|10", "reskillable:attack|12");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLevelUp(LevelUpEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        if (event.getSkill() instanceof SkillAgility && !player.world.isRemote) {
            PlayerSkillInfo info = PlayerDataHandler.get(player).getSkillInfo(getParentSkill());
            if (!info.isUnlocked(this)) {return;}
            IAttributeInstance AttributeCritDamage = player.getEntityAttribute(critDamageMod);
            AttributeCritDamage.removeModifier(modifierID);
            AttributeModifier newModifier = new AttributeModifier("reskillable.critDamage", calcCritDamage(player), 0);
            modifierID = newModifier.getID();
            AttributeCritDamage.applyModifier(newModifier);
            AttributeCritDamage.getModifiers().forEach(modifiers -> player.sendMessage(new TextComponentString(modifiers.toString())));
        }
    }

    @SubscribeEvent
    public void onUnlock(UnlockUnlockableEvent.Post event) {
        if (event.getUnlockable() instanceof TraitMightyStrikes) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.world.isRemote) {
                IAttributeInstance AttributeCritDamage = player.getEntityAttribute(critDamageMod);
                float amount = calcCritDamage(player);
                AttributeModifier modifier = new AttributeModifier("reskillable.critDamage", amount, 0);
                AttributeCritDamage.applyModifier(modifier);
                modifierID = modifier.getID();
            }
        }
    }

    @SubscribeEvent
    public void onLock(LockUnlockableEvent event) {
        if (event.getUnlockable() instanceof TraitMightyStrikes) {
            EntityPlayer player = event.getEntityPlayer();
            IAttributeInstance AttributeCrit = player.getEntityAttribute(critDamageMod);
            AttributeCrit.removeModifier(modifierID);
        }
    }

    public float calcCritDamage(EntityPlayer player) {
        int agility = PlayerDataHandler.get(player).getSkillInfo(getParentSkill()).getLevel();
        if (agility < 1) throw new IllegalArgumentException();
        float scalar = 10.0F;
        float input = agility / scalar;
        float critDamageMod = (float) (((Math.sqrt(11.0 * (input + 1)) - 1) / 2) * scalar);
        return (float) MathHelper.round(critDamageMod, 2);
    }
}
