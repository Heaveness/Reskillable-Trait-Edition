package codersafterdark.reskillable.common.profession.warrior.berserker;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.profession.warrior.warden.TalentAura;
import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import com.fantasticsource.dynamicstealth.server.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentFrenzy extends Talent {
    private UUID modifierID = null;
    IAttribute damageResist = ReskillableAttributes.DAMAGE_RESIST;

    public TalentFrenzy() {
        super(new ResourceLocation(MOD_ID, "frenzy"), 1, 3, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "profession|reskillable:warrior|13");
        setCap(5);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAttack(LivingDamageEvent event) {
        if ((event.getSource().getTrueSource() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (!PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {return;}
            float health = player.getHealth();
            float maxHealth = player.getMaxHealth();
            float damage = event.getAmount();
            float healAmount = damage * 0.15f;
            if (health < maxHealth) {
                player.heal(Math.round(healAmount));
            }
        }
    }

    @SubscribeEvent
    public void onUnlock(UnlockTalentEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        if (event.getTalent() instanceof TalentFrenzy) {
            player.sendMessage(new TextComponentString("Unlocked " + event.getTalent().getName()));

            IAttributeInstance threatFromAttacks = player.getEntityAttribute(damageResist);
            AttributeModifier modifier = new AttributeModifier("reskillable.damageResistance", -15, 0);
            modifierID = modifier.getID();
            threatFromAttacks.applyModifier(modifier);
        }
    }

    @SubscribeEvent
    public void onLock(LockTalentEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        if (event.getTalent() instanceof TalentFrenzy) {
            player.sendMessage(new TextComponentString("Unlocked " + event.getTalent().getName()));
            IAttributeInstance threatFromAttacks = player.getEntityAttribute(damageResist);
            threatFromAttacks.removeModifier(modifierID);
        }
    }
}
