package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "Restricts/unrestricts block modification abilities", usage = "/<command> [[-s] <player> [reason] | list | purge | all]")
public class Command_blockedit extends FreedomCommand
{
    @Override
    public boolean run(final CommandSender sender, final Player playerSender, final Command cmd, final String commandLabel, String[] args, final boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            return false;
        }

        if (args[0].equals("list"))
        {
            msg(plugin.i18n.getMessage("editsBlockedFor"));
            int count = 0;
            for (Player player : server.getOnlinePlayers())
            {
                final FPlayer info = plugin.pl.getPlayer(player);
                if (info.isEditBlocked())
                {
                    msg("- " + player.getName());
                    ++count;
                }
            }

            if (count == 0)
            {
                msg("- " + plugin.i18n.getMessage("none"));
            }
            return true;
        }

        if (args[0].equals("purge"))
        {
            FUtil.adminAction(sender.getName(), plugin.i18n.getMessage("editsUnblockedForAllPlayers"), true);
            int count = 0;
            for (final Player player : this.server.getOnlinePlayers())
            {
                final FPlayer info = plugin.pl.getPlayer(player);
                if (info.isEditBlocked())
                {
                    info.setEditBlocked(false);
                    ++count;
                }
            }
            msg(plugin.i18n.getMessage("editsUnblockedForAllPlayersCount", count));
            return true;
        }

        if (args[0].equals("all"))
        {
            FUtil.adminAction(sender.getName(), plugin.i18n.getMessage("editsBlockedForAllPlayers"), true);
            int counter = 0;
            for (final Player player : this.server.getOnlinePlayers())
            {
                if (!plugin.al.isAdmin(player))
                {
                    final FPlayer playerdata = plugin.pl.getPlayer(player);
                    playerdata.setEditBlocked(true);
                    ++counter;
                }
            }

            msg(plugin.i18n.getMessage("editsBlockedForAllPlayersCount", counter));
            return true;
        }

        final boolean smite = args[0].equals("-s");
        if (smite)
        {
            args = (String[])ArrayUtils.subarray((Object[])args, 1, args.length);
            if (args.length < 1)
            {
                return false;
            }
        }

        final Player player = getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(FreedomCommand.PLAYER_NOT_FOUND);
            return true;
        }

        String reason = null;
        if (args.length > 1)
        {
            reason = StringUtils.join(args, " ", 1, args.length);
        }

        final FPlayer pd = plugin.pl.getPlayer(player);
        if (pd.isEditBlocked())
        {
            FUtil.adminAction(sender.getName(), plugin.i18n.getMessage("editsUnblockedForPlayer", player), true);
            pd.setEditBlocked(false);
            msg(plugin.i18n.getMessage("editsUnblockedForPlayer", player));
            msg(player, plugin.i18n.getMessage("blockPlaceAbilityRestored"), ChatColor.RED);
        }
        else
        {
            if (plugin.al.isAdmin(player))
            {
                msg(plugin.i18n.getMessage("editsCannotBeBlocked", player));
                return true;
            }

            FUtil.adminAction(sender.getName(), plugin.i18n.getMessage("editsBlockedForPlayer", player), true);
            pd.setEditBlocked(true);

            if (smite)
            {
                Command_smite.smite(sender, player, reason);
            }

            msg(player, plugin.i18n.getMessage("editsBlocked"));
            msg(plugin.i18n.getMessage("blockedEditsFor", player));
        }
        return true;
    }
}
