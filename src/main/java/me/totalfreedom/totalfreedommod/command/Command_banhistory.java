package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "See the ban history of a player", usage = "/<command> <player> [amount]", aliases = "bh")
public class Command_banhistory extends FreedomCommand
{
    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }
        int amount = 1;
        if (args.length > 1)
        {
            try
            {
                amount = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException ex)
            {
                msg("Invalid amount: " + args[1], ChatColor.RED);
                return true;
            }
        }
        plugin.pul.getPlayerPunishments(args[0], amount, sender);
        return true;
    }
}
