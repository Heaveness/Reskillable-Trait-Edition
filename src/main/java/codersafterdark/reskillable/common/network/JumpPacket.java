package codersafterdark.reskillable.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class JumpPacket implements IMessage, IMessageHandler<JumpPacket, IMessage> {

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public IMessage onMessage(JumpPacket message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(ctx));
        return null;
    }

    public IMessage handleMessage(MessageContext context) {
        EntityPlayerMP player = context.getServerHandler().player;
        player.server.addScheduledTask(() -> {
            player.fallDistance = 0;
            player.jump();
            player.addExhaustion(0.3F);
        });

        return null;
    }

}
