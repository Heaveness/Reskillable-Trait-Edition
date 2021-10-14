package codersafterdark.reskillable.common.network;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerProfessionInfo;
import codersafterdark.reskillable.api.data.PlayerTalentInfo;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.talent.Talent;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Objects;

public class MessageUnlockTalent implements IMessage, IMessageHandler<MessageUnlockTalent, IMessage> {
    private ResourceLocation profession;
    private ResourceLocation talent;

    public MessageUnlockTalent() {
    }

    public MessageUnlockTalent(ResourceLocation profession, ResourceLocation talent) {
        this.profession = profession;
        this.talent = talent;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        profession = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        talent = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, profession.toString());
        ByteBufUtils.writeUTF8String(buf, talent.toString());
    }

    @Override
    public IMessage onMessage(MessageUnlockTalent message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));
        return null;
    }

    public IMessage handleMessage(MessageUnlockTalent message, MessageContext context) {
        EntityPlayer player = context.getServerHandler().player;
        Profession profession = ReskillableRegistries.PROFESSIONS.getValue(message.profession);
        Talent talent = Objects.requireNonNull(ReskillableRegistries.TALENTS.getValue(message.talent));
        PlayerData data = PlayerDataHandler.get(player);
        PlayerProfessionInfo info = data.getProfessionInfo(profession);
        PlayerTalentInfo talentInfo = data.getTalentInfo(talent);

        if (!info.isUnlocked(talent) && info.getProfessionPoints() >= talent.getCost() && data.matchStats(talent.getRequirements())
                && !MinecraftForge.EVENT_BUS.post(new UnlockTalentEvent.Pre(player, talent))) {
            info.unlock(talent, player);
            talentInfo.levelUp();
            data.saveAndSync();
            MinecraftForge.EVENT_BUS.post(new UnlockTalentEvent.Post(player, talent));
        }
        return null;
    }

}
