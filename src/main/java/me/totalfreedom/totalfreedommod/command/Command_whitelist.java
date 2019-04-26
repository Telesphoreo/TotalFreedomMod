package me.totalfreedom.totalfreedommod.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.DepreciationAggregator;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Manage the whitelist.", usage = "/<command> <on | off | list | count | add <player> | remove <player> | addall | purge>")
public class Command_whitelist extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }

        // list
        if (args[0].equalsIgnoreCase("list"))
        {
            if (server.getWhitelistedPlayers().isEmpty())
            {
                msg("There are no whitelisted players.");
                return true;
            }
            msg("Whitelisted players: " + FUtil.playerListToNames(server.getWhitelistedPlayers()));
            return true;
        }

        // count
        if (args[0].equalsIgnoreCase("count"))
        {
            int onlineWPs = 0;
            int offlineWPs = 0;
            int totalWPs = 0;

            for (OfflinePlayer player : server.getWhitelistedPlayers())
            {
                if (player.isOnline())
                {
                    onlineWPs++;
                }
                else
                {
                    offlineWPs++;
                }
                totalWPs++;
            }

            msg("Online whitelisted players: " + onlineWPs);
            msg("Offline whitelisted players: " + offlineWPs);
            msg("Total whitelisted players: " + totalWPs);
            return true;
        }

        // Commands below are restricted to admins
        checkRank(Rank.SUPER_ADMIN);

        // on
        if (args[0].equalsIgnoreCase("on"))
        {
            FUtil.adminAction(sender.getName(), "Turning the whitelist on", true);
            server.setWhitelist(true);
            return true;
        }

        // off
        if (args[0].equalsIgnoreCase("off"))
        {
            FUtil.adminAction(sender.getName(), "Turning the whitelist off", true);
            server.setWhitelist(false);
            return true;
        }

        // add
        if (args[0].equalsIgnoreCase("add"))
        {
            if (args.length < 2)
            {
                return false;
            }

            String search_name = args[1].trim().toLowerCase();

            OfflinePlayer player = getPlayer(search_name);

            if (player == null)
            {
                player = DepreciationAggregator.getOfflinePlayer(server, search_name);
            }

            FUtil.adminAction(sender.getName(), "Adding " + player.getName() + " to the whitelist", false);
            player.setWhitelisted(true);
            return true;
        }

        // remove
        if ("remove".equals(args[0]))
        {
            if (args.length < 2)
            {
                return false;
            }

            String search_name = args[1].trim().toLowerCase();

            OfflinePlayer player = getPlayer(search_name);

            if (player == null)
            {
                player = DepreciationAggregator.getOfflinePlayer(server, search_name);
            }

            if (player.isWhitelisted())
            {
                FUtil.adminAction(sender.getName(), "Removing " + player.getName() + " from the whitelist", false);
                player.setWhitelisted(false);
                return true;
            }
            else
            {
                msg("That player is not whitelisted");
                return true;
            }
        }

        // addall
        if (args[0].equalsIgnoreCase("addall"))
        {
            FUtil.adminAction(sender.getName(), "Adding all online players to the whitelist", false);
            int counter = 0;
            for (Player player : server.getOnlinePlayers())
            {
                if (!player.isWhitelisted())
                {
                    player.setWhitelisted(true);
                    counter++;
                }
            }

            msg("Whitelisted " + counter + " players.");
            return true;
        }

        // Telnet only
        checkConsole();
        checkRank(Rank.TELNET_ADMIN);

        // purge
        if (args[0].equalsIgnoreCase("purge"))
        {
            FUtil.adminAction(sender.getName(), "Removing all players from the whitelist", false);
            msg("Removed " + plugin.si.purgeWhitelist() + " players from the whitelist.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> getTabCompleteOptions(CommandSender sender, Command command, String alias, String[] args)
    {
        if (args.length == 1)
        {
            List<String> arguments = new ArrayList<>();
            arguments.addAll(Arrays.asList("list", "count"));
            if (plugin.al.isAdmin(sender))
            {
                arguments.addAll(Arrays.asList("on", "off", "add", "remove", "addall"));
                if (!(sender instanceof Player))
                {
                    arguments.add("purge");
                }
            }
            return arguments;
        }
        else if (args.length == 2 && plugin.al.isAdmin(sender))
        {
            if (args[0].equals("add"))
            {
                return FUtil.getPlayerList();
            }
            else if (args[0].equals("remove"))
            {
                return getWhitelistedNames();
            }
        }

        return Collections.emptyList();
    }

    public List<String> getWhitelistedNames()
    {
        List<String> names = new ArrayList<>();
        for (Object name : plugin.si.getWhitelisted())
        {
            names.add(String.valueOf(name));
        }
        return names;
    }
}
