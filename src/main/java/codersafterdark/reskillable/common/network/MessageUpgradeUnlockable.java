package codersafterdark.reskillable.common.network;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.*;
import codersafterdark.reskillable.api.event.UpgradeTalentEvent;
import codersafterdark.reskillable.api.event.UpgradeUnlockableEvent;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
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

public class MessageUpgradeUnlockable implements IMessage, IMessageHandler<MessageUpgradeUnlockable, IMessage> {
    private ResourceLocation skill;
    private ResourceLocation unlockable;

    public MessageUpgradeUnlockable() {
    }

    public MessageUpgradeUnlockable(ResourceLocation skill, ResourceLocation unlockable) {
        this.skill = skill;
        this.unlockable = unlockable;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        skill = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        unlockable = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, skill.toString());
        ByteBufUtils.writeUTF8String(buf, unlockable.toString());
    }

    @Override
    public IMessage onMessage(MessageUpgradeUnlockable message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));
        return null;
    }

    public IMessage handleMessage(MessageUpgradeUnlockable message, MessageContext context) {
        EntityPlayer player = context.getServerHandler().player;
        Skill skill = ReskillableRegistries.SKILLS.getValue(message.skill);
        Unlockable unlockable = Objects.requireNonNull(ReskillableRegistries.UNLOCKABLES.getValue(message.unlockable));
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo info = data.getSkillInfo(skill);
        PlayerUnlockableInfo unlockableInfo = data.getUnlockableInfo(unlockable);
        if (!unlockableInfo.isCapped() && info.getSkillPoints() >= unlockable.getCost()) {
            int oldRank = unlockableInfo.getLevel();
            if (!MinecraftForge.EVENT_BUS.post(new UpgradeUnlockableEvent.Pre(player, unlockable, oldRank + 1, oldRank))) {
                unlockableInfo.levelUp();
                info.spendSkillPoints(unlockableInfo.getLevelUpCost());
                data.saveAndSync();
                MinecraftForge.EVENT_BUS.post(new UpgradeUnlockableEvent.Post(player, unlockable, unlockableInfo.getLevel(), oldRank));
            }
        }
        return null;
    }

}
