package codersafterdark.reskillable.common.profession.warrior.berserker;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.potion.ReskillablePotion;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentShadow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import zabi.minecraft.extraalchemy.potion.PotionReference;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentEdge extends Talent {
    public TalentEdge() {
        super(new ResourceLocation(MOD_ID, "edge"), 2, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "profession|reskillable:warrior|6", "reskillable:attack|8");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void playerTickHandler(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            if (!PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {return;}
            if (event.phase != TickEvent.Phase.START) {return;}
            if (player.getHealth() > player.getMaxHealth() * 0.5 && player.isPotionActive(ReskillablePotion.POTION_EDGE_EFFECT)) {
                player.removeActivePotionEffect(ReskillablePotion.POTION_EDGE_EFFECT);
            }

            if (player.ticksExisted % 20 == 0 && player.getHealth() < player.getMaxHealth() * 0.5
            && !player.isPotionActive(ReskillablePotion.POTION_EDGE_EFFECT)) {
                player.addPotionEffect(new PotionEffect(ReskillablePotion.POTION_EDGE_EFFECT, 6000));
            }
        }
    }

}
