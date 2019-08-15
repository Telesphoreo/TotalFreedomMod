package me.totalfreedom.totalfreedommod.command;

import java.util.ArrayList;
import me.totalfreedom.totalfreedommod.admin.Admin;
import me.totalfreedom.totalfreedommod.rank.Displayable;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import static me.totalfreedom.totalfreedommod.util.FUtil.playerMsg;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Vanish/unvanish yourself.", usage = "/<command> [-s[ilent]]", aliases = "v")
public class Command_vanish extends FreedomCommand
{
    public static ArrayList<Player> VANISHED = new ArrayList<>();

    public boolean run(final CommandSender sender, final Player playerSender, final Command cmd, final String commandLabel, final String[] args, final boolean senderIsConsole)
    {
        Displayable display = plugin.rm.getDisplay(playerSender);
        String loginMsg = display.getColoredLoginMessage();
        String displayName = display.getColor() + playerSender.getName();
        String tag = display.getColoredTag();
        Admin admin = plugin.al.getAdmin(playerSender);
        boolean silent = false;
        if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("-s") || args[0].equalsIgnoreCase("-silent"))
            {
                silent = true;
            }
        }
        if (VANISHED.contains(playerSender))
        {
            msg(ChatColor.GOLD + "You have been unvanished.");
            if (admin.hasLoginMessage())
            {
                loginMsg = FUtil.colorize(admin.getLoginMessage()).replace("%rank%", plugin.rm.getDisplay(admin).getName()).replace("%coloredrank%", plugin.rm.getDisplay(admin).getColoredName());
            }
            if (!silent)
            {
                FUtil.bcastMsg(ChatColor.AQUA + (loginMsg.contains("%name%") ? "" : playerSender.getName() + " is ") + loginMsg);
                FUtil.bcastMsg(playerSender.getName() + " joined the game", ChatColor.YELLOW);
            }
            if (admin.getTag() != null)
            {
                tag = FUtil.colorize(admin.getTag());
            }
            else
            {
                plugin.pl.getPlayer(playerSender).setTag(tag);
            }
            FLog.info(playerSender.getName() + " is no longer vanished.");
            for (Player player : server.getOnlinePlayers())
            {
                if (plugin.al.isAdmin(player))
                {
                    playerMsg(player, ChatColor.YELLOW + sender.getName() + " has unvanished and is now visible to everyone.");
                }
                player.showPlayer(plugin, playerSender);
            }
            plugin.esb.setVanished(playerSender.getName(), false);
            playerSender.setPlayerListName(StringUtils.substring(displayName, 0, 16));
            VANISHED.remove(playerSender);
        }
        else
        {
            msg("You have been vanished.", ChatColor.GOLD);
            if (!silent)
            {
                FUtil.bcastMsg(playerSender.getName() + " left the game", ChatColor.YELLOW);
            }
            FLog.info(playerSender.getName() + " is now vanished.");
            for (Player player : server.getOnlinePlayers())
            {
                {
                    if (plugin.al.isAdmin(player))
                    {
                        playerMsg(player, ChatColor.YELLOW + sender.getName() + " has vanished and is now only visible to admins.");
                    }
                    if (!plugin.al.isAdmin(player))
                    {
                        player.hidePlayer(plugin, playerSender);
                    }
                }
            }
            plugin.esb.setVanished(playerSender.getName(), true);
            VANISHED.add(playerSender);
        }
        return true;
    }
}