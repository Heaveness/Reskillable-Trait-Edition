package codersafterdark.reskillable.common.profession.rogue.archer;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.network.MessageClimb;
import codersafterdark.reskillable.common.network.PacketHandler;
import com.fantasticsource.dynamicstealth.common.ClientData;
import com.tmtravlr.potioncore.network.CToSMessage;
import com.tmtravlr.potioncore.potion.PotionClimb;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentVantage extends Talent {

    public TalentVantage() {
        super(new ResourceLocation(MOD_ID, "vantage"), 1, 3, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "archer"), 3, "profession|reskillable:rogue|13");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void playerTickHandler(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            if (!PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {return;}
            if (event.phase != TickEvent.Phase.START) {return;}

            if (player.collidedHorizontally) {
                if (!player.isSneaking()) {
                    if (player.moveForward > 0.0F && player.motionY < 0.2D) {
                        player.motionY = 0.2D;
                    }
                } else {
                    player.motionY = 0.0D;
                }

                player.fallDistance = 0.0F;
                if (player.getEntityWorld().isRemote && player.ticksExisted % 21 == 0) {
                    PacketBuffer out = new PacketBuffer(Unpooled.buffer());
                    out.writeInt(1);
                    out.writeLong(player.getUniqueID().getMostSignificantBits());
                    out.writeLong(player.getUniqueID().getLeastSignificantBits());
                    MessageClimb packet = new MessageClimb(out);
                    PacketHandler.INSTANCE.sendToServer(packet);
                }
            }
        }
    }

}
