package codersafterdark.reskillable.common.network;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.toast.ToastHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ProfessionToastPacket implements IMessage, IMessageHandler<ProfessionToastPacket, IMessage> {
    private ResourceLocation professionName;
    private int level;

    public ProfessionToastPacket() {
    }

    public ProfessionToastPacket(ResourceLocation professionName, int level) {
        this.professionName = professionName;
        this.level = level;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.professionName = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        this.level = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.professionName.toString());
        buf.writeInt(this.level);
    }

    @Override
    public IMessage onMessage(ProfessionToastPacket message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handleMessage(message, ctx));
        return null;
    }

    public IMessage handleMessage(ProfessionToastPacket message, MessageContext ctx) {
        ToastHelper.sendProfessionToast(ReskillableRegistries.PROFESSIONS.getValue(message.professionName), message.level);
        return null;
    }

}
