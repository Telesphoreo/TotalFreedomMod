package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Check the stats of the server", usage = "/<command>", aliases = "ss")
public class Command_serverstats extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        msg("-==" + ConfigEntry.SERVER_NAME.getString() + " server stats==-", ChatColor.GOLD);
        msg("Total opped players: " + server.getOperators().size(), ChatColor.RED);
        msg("Total staff: " + plugin.al.getAllAdmins().size() + " (" + plugin.al.getActiveAdmins().size() + " active)", ChatColor.BLUE);
        int tpbips = plugin.pm.getPermbannedIps().size();
        int tpbns = plugin.pm.getPermbannedNames().size();
        int tpbs = tpbips + tpbns;
        msg("Total perm bans: " + tpbs + " (" + tpbips + " IPs, " + tpbns + " names)", ChatColor.GREEN);
        return true;
    }
}