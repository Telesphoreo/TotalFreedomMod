package me.totalfreedom.totalfreedommod.command;

import java.io.File;
import java.util.Objects;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.punishments.Punishment;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.reporting.ReportList;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SENIOR_ADMIN, source = SourceType.ONLY_CONSOLE, blockHostConsole = true)
@CommandParameters(description = "Wipes all logged reports or reports for a specific user.", usage = "/<command> <username | -a>")
public class Command_wipereports extends FreedomCommand
{
    @Override
    public boolean run(final CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }

        if (args[0].equalsIgnoreCase("-a"))
        {
            FUtil.adminAction(sender.getName(), "Wiping the reports history", true);
            FUtil.deleteFile(new File(Objects.requireNonNull(TotalFreedomMod.plugin()).getDataFolder(), ReportList.CONFIG_FILENAME));

            msg("Wiped " + plugin.pul.clear() + " reports.");
        }
        else
        {
            // TODO: make it actually work...
            String username = args[0];

            FUtil.adminAction(sender.getName(), "Wiping the report history for " + username, true);

            msg("Wiped " + plugin.pul.clear(username) + " reports for " + username + ".");
        }

        return true;
    }
}