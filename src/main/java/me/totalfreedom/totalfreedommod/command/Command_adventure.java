package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Quickly change your own gamemode to adventure, or define someone's username to change theirs.", usage = "/<command> <[partialname] | -a>", aliases = "gma")
public class Command_adventure extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            if (isConsole())
            {
                sender.sendMessage(plugin.i18n.getMessage("mustBePlayer"));
                return true;
            }

            playerSender.setGameMode(GameMode.ADVENTURE);
            msg(plugin.i18n.getMessage("gamemodeAdventure"));
            return true;
        }

        checkRank(Rank.SUPER_ADMIN);

        if (args[0].equals("-a"))
        {
            for (Player targetPlayer : server.getOnlinePlayers())
            {
                targetPlayer.setGameMode(GameMode.ADVENTURE);
            }

            FUtil.adminAction(sender.getName(), plugin.i18n.getMessage("gamemodeAdventureEveryone"), false);
            return true;
        }

        Player player = getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(FreedomCommand.PLAYER_NOT_FOUND);
            return true;
        }

        msg(plugin.i18n.getMessage("settingtoGamemodeAdventure", player));
        msg(player, sender.getName() + plugin.i18n.getMessage("gamemodeSetToAdventureByOtherPlayer"));
        player.setGameMode(GameMode.ADVENTURE);
        return true;
    }
}
