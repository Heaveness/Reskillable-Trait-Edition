package codersafterdark.reskillable.common.profession.rogue.assassin;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.potion.ReskillablePotion;
import com.fantasticsource.dynamicstealth.common.ClientData;
import com.fantasticsource.dynamicstealth.server.event.attacks.StealthAttackEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentDarkness extends Talent {
    public TalentDarkness() {
        super(new ResourceLocation(MOD_ID, "darkness"), 0, 1, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "assassin"), 3, "profession|reskillable:rogue|13");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void playerTickHandler(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            if (!PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {return;}
            if (event.phase != TickEvent.Phase.START) {return;}
            if (ClientData.lightLevel > 8) {
                if (player.isPotionActive(ReskillablePotion.POTION_DARKNESS_EFFECT)) {
                    player.removePotionEffect(ReskillablePotion.POTION_DARKNESS_EFFECT);
                }
                return;
            }

            if (player.ticksExisted % 100 == 0) {
                player.addPotionEffect(new PotionEffect(ReskillablePotion.POTION_DARKNESS_EFFECT, 1000000, 0, true, false));
            }
        }
    }

    @SubscribeEvent
    public void stealthAttack(StealthAttackEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)
            && player.isPotionActive(ReskillablePotion.POTION_DARKNESS_EFFECT)
            && !(event.getSource().getImmediateSource() instanceof EntityArrow)) {
                event.setAmount(event.getAmount() * 1.25F);
            }
        }
    }
}


