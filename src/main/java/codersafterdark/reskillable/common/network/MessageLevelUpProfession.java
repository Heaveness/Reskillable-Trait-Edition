package codersafterdark.reskillable.common.network;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerProfessionInfo;
import codersafterdark.reskillable.api.event.LevelUpProfessionEvent;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.common.core.ExperienceHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageLevelUpProfession implements IMessage, IMessageHandler<MessageLevelUpProfession, IMessage> {
    public ResourceLocation professionName;

    public MessageLevelUpProfession() {
    }

    public MessageLevelUpProfession(ResourceLocation professionName) {
        this.professionName = professionName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        professionName = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, professionName.toString());
    }

    @Override
    public IMessage onMessage(MessageLevelUpProfession message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));
        return null;
    }

    public IMessage handleMessage(MessageLevelUpProfession message, MessageContext context) {
        EntityPlayer player = context.getServerHandler().player;
        Profession profession = ReskillableRegistries.PROFESSIONS.getValue(message.professionName);
        PlayerData data = PlayerDataHandler.get(player);
        if (!data.getUnlockedProfessions().contains(profession)) {data.unlockProfession(profession);}
        PlayerProfessionInfo info = data.getProfessionInfo(profession);
        if (!info.isCapped()) {
            int cost = info.getLevelUpCost();
            if (player.experienceLevel >= cost || player.isCreative()) {
                int oldLevel = info.getLevel();
                if (!MinecraftForge.EVENT_BUS.post(new LevelUpProfessionEvent.Pre(player, profession, oldLevel + 1, oldLevel))) {
                    if (!player.isCreative()) {
                        ExperienceHelper.drainPlayerXP(player, ExperienceHelper.getExperienceForLevel(cost));
                    }
                    info.levelUp();
                    data.saveAndSync();
                    MinecraftForge.EVENT_BUS.post(new LevelUpProfessionEvent.Post(player, profession, info.getLevel(), oldLevel));
                }
            }
        }
        return null;
    }
}