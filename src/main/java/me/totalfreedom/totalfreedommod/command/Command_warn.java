package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "Warns a player.", usage = "/<command> <player> <reason>")
public class Command_warn extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 2)
        {
            return false;
        }

        Player player = getPlayer(args[0]);

        if (player == null)
        {
            msg(PLAYER_NOT_FOUND);
            return true;
        }

        if (sender instanceof Player)
        {
            if (player.equals(playerSender))
            {
                msg(ChatColor.RED + "Please, don't try to warn yourself.");
                return true;
            }
        }

        if (plugin.al.isAdmin(player))
        {
            msg(ChatColor.RED + "You can not warn admins");
            return true;
        }

        String warnReason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        msg(player, ChatColor.RED + "[WARNING] You received a warning: " + warnReason);
        player.sendTitle(ChatColor.RED + "You've been warned.", ChatColor.YELLOW + "Reason: " + warnReason, 20, 100, 60);
        msg(ChatColor.GREEN + "You have successfully warned " + player.getName());
        final StringBuilder adminNotice = new StringBuilder()
                .append(ChatColor.RED)
                .append(sender.getName())
                .append(" - ")
                .append("Warning: ")
                .append(player.getName())
                .append(" - Reason: ")
                .append(ChatColor.YELLOW)
                .append(warnReason);
        plugin.al.messageAllAdmins(adminNotice.toString());
        plugin.pl.getPlayer(player).incrementWarnings();
        return true;
    }
}
