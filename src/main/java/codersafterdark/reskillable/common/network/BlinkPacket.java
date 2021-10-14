package codersafterdark.reskillable.common.network;

import codersafterdark.reskillable.common.profession.rogue.assassin.TalentBlink;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BlinkPacket implements IMessage, IMessageHandler<BlinkPacket, IMessage> {

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public IMessage onMessage(BlinkPacket message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(ctx));
        return null;
    }

    public IMessage handleMessage(MessageContext context) {
        EntityPlayerMP player = context.getServerHandler().player;
        player.server.addScheduledTask(() -> {
            TalentBlink talentBlink = new TalentBlink();
            talentBlink.cast(player.getEntityWorld(), player);
        });
        return null;
    }

}
