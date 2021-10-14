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

public class TalentToastPacket implements IMessage, IMessageHandler<TalentToastPacket, IMessage> {
    private ResourceLocation talentName;

    public TalentToastPacket() {
    }

    public TalentToastPacket(ResourceLocation talentName) {
        this.talentName = talentName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.talentName = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.talentName.toString());
    }

    @Override
    public IMessage onMessage(TalentToastPacket message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handleMessage(message, ctx));
        return null;
    }

    public IMessage handleMessage(TalentToastPacket message, MessageContext ctx) {
        ToastHelper.sendTalentToast(ReskillableRegistries.TALENTS.getValue(message.talentName));
        return null;
    }

}
