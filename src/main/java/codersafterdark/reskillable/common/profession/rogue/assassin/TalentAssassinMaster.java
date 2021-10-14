package codersafterdark.reskillable.common.profession.rogue.assassin;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import com.fantasticsource.dynamicstealth.server.event.attacks.StealthAttackEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentAssassinMaster extends Talent {
    public TalentAssassinMaster() {
        super(new ResourceLocation(MOD_ID, "assassin_master"), 1, 0, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "assassin"),
                3, "profession|reskillable:rogue|32", "talent|reskillable:assassin_advanced");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onStealthAttack(StealthAttackEvent event) {
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();

        if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
            event.setAmount(event.getAmount()*1.25F);
        }
    }
}
