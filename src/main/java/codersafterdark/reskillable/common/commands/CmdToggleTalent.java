package codersafterdark.reskillable.common.commands;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerProfessionInfo;
import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.api.toast.ToastHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdToggleTalent extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "toggletalent";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "reskillable.command.toggletalent.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 0) {
            throw new CommandException("reskillable.command.invalid.missing.playertalent");
        }
        if (args.length == 1) {
            throw new CommandException("reskillable.command.invalid.missing.talent");
        }
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        args[1] = args[1].replaceAll(":", ".");
        String[] parts = args[1].split("\\.");
        ResourceLocation talentName = parts.length > 1 ? new ResourceLocation(parts[0], args[1].substring(parts[0].length() + 1)) : new ResourceLocation(args[1]);
        if (!ReskillableRegistries.TALENTS.containsKey(talentName)) {
            throw new CommandException("reskillable.command.invalid.talent", talentName);
        }
        Talent talent = ReskillableRegistries.TALENTS.getValue(talentName);
        PlayerData data = PlayerDataHandler.get(player);
        PlayerProfessionInfo professionInfo = data.getProfessionInfo(talent.getParentProfession());
        if (professionInfo.isUnlocked(talent)) {
            if (!MinecraftForge.EVENT_BUS.post(new LockTalentEvent.Pre(player, talent))) {
                professionInfo.lock(talent, player);
                data.saveAndSync();
                MinecraftForge.EVENT_BUS.post(new LockTalentEvent.Post(player, talent));
                sender.sendMessage(new TextComponentTranslation("reskillable.command.success.locktalent", talentName, player.getDisplayName()));
            } else {
                sender.sendMessage(new TextComponentTranslation("reskillable.command.fail.locktalent", talentName, player.getDisplayName()));
            }
        } else if (!MinecraftForge.EVENT_BUS.post(new UnlockTalentEvent.Pre(player, talent))) {
            professionInfo.unlock(talent, player);
            data.saveAndSync();
            MinecraftForge.EVENT_BUS.post(new UnlockTalentEvent.Post(player, talent));
            ToastHelper.sendTalentToast(player, talent);
            sender.sendMessage(new TextComponentTranslation("reskillable.command.success.unlocktalent", talentName, player.getDisplayName()));
        } else {
            sender.sendMessage(new TextComponentTranslation("reskillable.command.fail.unlocktalent", talentName, player.getDisplayName()));
        }
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 0) {
            return Arrays.asList(server.getPlayerList().getOnlinePlayerNames());
        }
        if (args.length == 1) {
            String partialName = args[0];
            return Arrays.stream(server.getPlayerList().getOnlinePlayerNames()).filter(name -> name.startsWith(partialName)).collect(Collectors.toList());
        }
        if (args.length == 2) {
            String partial = args[1].replaceAll(":", ".");
            return ReskillableRegistries.TALENTS.getValuesCollection().stream().map(Talent::getKey).filter(talentName -> talentName.startsWith(partial)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

}
