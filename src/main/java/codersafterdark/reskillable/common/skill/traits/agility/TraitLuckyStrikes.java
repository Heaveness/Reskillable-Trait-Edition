package codersafterdark.reskillable.common.skill.traits.agility;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.data.PlayerUnlockableInfo;
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
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class TraitLuckyStrikes extends Trait {
    IAttribute critChance = ReskillableAttributes.CRIT_CHANCE;

    public TraitLuckyStrikes() {
        super(new ResourceLocation(LibMisc.MOD_ID, "lucky_strikes"), 1, 3, new ResourceLocation("reskillable", "agility"), 1, "reskillable:agility|10");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLevelUp(LevelUpEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        if (event.getSkill() instanceof SkillAgility && !player.world.isRemote) {
            PlayerSkillInfo info = PlayerDataHandler.get(player).getSkillInfo(getParentSkill());
            PlayerUnlockableInfo unlockableInfo = PlayerDataHandler.get(player).getUnlockableInfo(this);
            if (!info.isUnlocked(this)) {return;}
            IAttributeInstance AttributeCrit = player.getEntityAttribute(critChance);
            unlockableInfo.removeUnlockableAttribute(AttributeCrit);
            float amount = calcCritChance(player);
            AttributeModifier newModifier = new AttributeModifier("reskillable.critChance", amount, 0);
            unlockableInfo.addAttributeModifier(AttributeCrit, newModifier);
            PlayerDataHandler.get(player).saveAndSync();
        }
    }

    @SubscribeEvent
    public void onUnlock (UnlockUnlockableEvent.Post event){
        if (event.getUnlockable() instanceof TraitLuckyStrikes) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.world.isRemote) {
                PlayerUnlockableInfo info = PlayerDataHandler.get(player).getUnlockableInfo(this);
                IAttributeInstance AttributeCrit = player.getEntityAttribute(critChance);
                float amount = calcCritChance(player);
                AttributeModifier modifier = new AttributeModifier("reskillable.critChance", amount, 0);
                info.addAttributeModifier(AttributeCrit, modifier);
                PlayerDataHandler.get(player).saveAndSync();
            }
        }
    }

    @SubscribeEvent
    public void onLock (LockUnlockableEvent event){
        if (event.getUnlockable() instanceof TraitLuckyStrikes) {
            EntityPlayer player = event.getEntityPlayer();
            IAttributeInstance AttributeCrit = player.getEntityAttribute(critChance);
            PlayerUnlockableInfo info = PlayerDataHandler.get(player).getUnlockableInfo(this);
            info.removeUnlockableAttribute(AttributeCrit);
            PlayerDataHandler.get(player).saveAndSync();
        }
    }

    public float calcCritChance (EntityPlayer player){
        int agility = PlayerDataHandler.get(player).getSkillInfo(getParentSkill()).getLevel();
        if (agility < 1) throw new IllegalArgumentException();
        float scalar = 5.3933F;
        float input = agility / scalar;
        float critChance = (float) (((Math.sqrt(10 * (input + 1)) - 1) / 2) * scalar);
        return (float) MathHelper.round(critChance, 2);
    }
}


