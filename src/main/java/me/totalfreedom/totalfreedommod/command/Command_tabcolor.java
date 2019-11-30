package me.totalfreedom.totalfreedommod.command;

import java.util.Iterator;
import java.util.Map;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SENIOR_ADMIN, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Change your tab color", usage = "/<command> <color>")
public class Command_tabcolor extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!FUtil.isExecutive(sender.getName()))
        {
            return noPerms();
        }

        if (args.length != 1)
        {
            return false;
        }

        if ("list".equalsIgnoreCase(args[0]))
        {
            msg("Colors: " + StringUtils.join(FUtil.CHAT_COLOR_NAMES.keySet(), ", "));
            return true;
        }

        final String needle = args[0].trim().toLowerCase();
        ChatColor color = null;
        final Iterator<Map.Entry<String, ChatColor>> it = FUtil.CHAT_COLOR_NAMES.entrySet().iterator();
        while (it.hasNext())
        {
            final Map.Entry<String, ChatColor> entry = it.next();
            if (entry.getKey().contains(needle))
            {
                color = entry.getValue();
                break;
            }
        }

        if (color == null)
        {
            msg("Invalid color: " + needle + " - Use \"/tabcolor list\" to list colors.");
            return true;
        }

        playerSender.setPlayerListName(StringUtils.substring(color + sender.getName(), 0, 16));

        msg("ok dad");
        return true;
    }
}