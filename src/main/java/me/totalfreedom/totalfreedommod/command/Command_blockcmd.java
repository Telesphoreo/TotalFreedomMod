package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "Block all commands for a specific player.", usage = "/<command> <-a | purge | <player>>", aliases = "blockcommands,blockcommand,bc,bcmd")
public class Command_blockcmd extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (args[0].equals("purge"))
        {
            FUtil.adminAction(sender.getName(), plugin.i18n.getMessage("unblockingCommandsForAllPlayers"), true);
            int counter = 0;
            for (Player player : server.getOnlinePlayers())
            {
                FPlayer playerdata = plugin.pl.getPlayer(player);
                if (playerdata.allCommandsBlocked())
                {
                    counter += 1;
                    playerdata.setCommandsBlocked(false);
                }
            }
            msg(plugin.i18n.getMessage("unblockedCommandsForAllPlayers", counter));
            return true;
        }

        if (args[0].equals("-a"))
        {
            FUtil.adminAction(sender.getName(), plugin.i18n.getMessage("blockingCommandsForAllPlayers"), true);
            int counter = 0;
            for (Player player : server.getOnlinePlayers())
            {
                if (isAdmin(player))
                {
                    continue;
                }

                counter += 1;
                plugin.pl.getPlayer(player).setCommandsBlocked(true);
                msg(player, plugin.i18n.getMessage("commandsBlockedByAnAdmin"), ChatColor.RED);
            }

            msg(plugin.i18n.getMessage("blockedCommandsForAllPlayers", counter));
            return true;
        }

        final Player player = getPlayer(args[0]);

        if (player == null)
        {
            msg(FreedomCommand.PLAYER_NOT_FOUND);
            return true;
        }

        if (isAdmin(player))
        {
            msg(plugin.i18n.getMessage("commandsCannotBeBlocked", player));
            return true;
        }

        FPlayer playerdata = plugin.pl.getPlayer(player);

        playerdata.setCommandsBlocked(!playerdata.allCommandsBlocked());

        // TODO
        FUtil.adminAction(sender.getName(), (playerdata.allCommandsBlocked() ? "B" : "Unb") + "locking all commands for " + player.getName(), true);
        msg((playerdata.allCommandsBlocked() ? "B" : "Unb") + "locked all commands.");

        return true;
    }
}
