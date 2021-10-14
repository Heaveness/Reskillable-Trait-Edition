package codersafterdark.reskillable.common.commands;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerProfessionInfo;
import codersafterdark.reskillable.api.event.LevelUpProfessionEvent;
import codersafterdark.reskillable.api.profession.Profession;
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
import java.util.stream.IntStream;

public class CmdSetProfessionLevel extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "setprofessionlevel";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "reskillable.command.setprofessionlevel.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 0) {
            throw new CommandException("reskillable.command.invalid.missing.playerskilllevel");
        }
        if (args.length == 1) {
            throw new CommandException("reskillable.command.invalid.missing.skilllevel");
        }
        if (args.length == 2) {
            throw new CommandException("reskillable.command.invalid.missing.level");
        }
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        args[1] = args[1].replaceAll(":", ".");
        String[] parts = args[1].split("\\.");
        ResourceLocation professionName = parts.length > 1 ? new ResourceLocation(parts[0], args[1].substring(parts[0].length() + 1)) : new ResourceLocation(args[1]);
        if (!ReskillableRegistries.PROFESSIONS.containsKey(professionName)) {
            throw new CommandException("reskillable.command.invalid.skill", professionName);
        }
        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            throw new CommandException("reskillable.command.invalid.missing.level", args[2]);
        }
        if (level < 1) {
            throw new CommandException("reskillable.command.invalid.belowmin", level);
        }
        Profession profession = ReskillableRegistries.PROFESSIONS.getValue(professionName);
        if (level > profession.getCap()) {
            throw new CommandException("reskillable.command.invalid.pastcap", level, profession.getCap());
        }
        PlayerData data = PlayerDataHandler.get(player);
        PlayerProfessionInfo professionInfo = data.getProfessionInfo(profession);
        int oldLevel = professionInfo.getLevel();
        if (!MinecraftForge.EVENT_BUS.post(new LevelUpProfessionEvent.Pre(player, profession, level, oldLevel))) {
            professionInfo.setLevel(level);
            data.saveAndSync();
            MinecraftForge.EVENT_BUS.post(new LevelUpProfessionEvent.Post(player, profession, level, oldLevel));
            ToastHelper.sendProfessionToast(player, profession, level);
            sender.sendMessage(new TextComponentTranslation("reskillable.command.success.setskilllevel", professionName, level, player.getDisplayName()));
        } else {
            sender.sendMessage(new TextComponentTranslation("reskillable.command.fail.setskilllevel", professionName, level, player.getDisplayName()));
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
            return ReskillableRegistries.PROFESSIONS.getValuesCollection().stream().map(Profession::getKey).filter(professionName -> professionName.startsWith(partial)).collect(Collectors.toList());
        }
        if (args.length == 3) {
            args[1] = args[1].replaceAll(":", ".");
            String[] parts = args[1].split("\\.");
            ResourceLocation professionName = parts.length > 1 ? new ResourceLocation(parts[0], args[1].substring(parts[0].length() + 1)) : new ResourceLocation(args[1]);
            if (ReskillableRegistries.PROFESSIONS.containsKey(professionName)) {
                Profession profession = ReskillableRegistries.PROFESSIONS.getValue(professionName);
                String level = args[2];
                return IntStream.rangeClosed(1, profession.getCap()).mapToObj(Integer::toString).filter(iString -> iString.startsWith(level)).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
