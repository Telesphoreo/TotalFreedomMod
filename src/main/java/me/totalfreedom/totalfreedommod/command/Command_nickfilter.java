package me.totalfreedom.totalfreedommod.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.totalfreedom.totalfreedommod.rank.Rank;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "NickFilter: Prefix any command with this command to replace nicknames in that command with real names. Nicknames should be prefixed with a !.",
        usage = "/<command> <other_command> !<playernick>",
        aliases = "nf")
public class Command_nickfilter extends FreedomCommand
{
    private static Player getPlayerByDisplayName(String needle)
    {
        needle = needle.toLowerCase().trim();

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player.getDisplayName().toLowerCase().trim().contains(needle))
            {
                return player;
            }
        }

        return null;
    }

    private static Player getPlayerByDisplayNameAlt(String needle)
    {
        needle = needle.toLowerCase().trim();

        Integer minEditDistance = null;
        Player minEditMatch = null;

        for (Player player : Bukkit.getOnlinePlayers())
        {
            String haystack = player.getDisplayName().toLowerCase().trim();
            int editDistance = StringUtils.getLevenshteinDistance(needle, haystack.toLowerCase());
            if (minEditDistance == null || minEditDistance > editDistance)
            {
                minEditDistance = editDistance;
                minEditMatch = player;
            }
        }

        return minEditMatch;
    }

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        boolean nickMatched = false;

        final List<String> outputCommand = new ArrayList<>();

        if (args.length >= 1)
        {
            for (String arg : args)
            {
                Player player = null;

                Matcher matcher = Pattern.compile("^!(.+)$").matcher(arg);
                if (matcher.find())
                {
                    String displayName = matcher.group(1);

                    player = getPlayerByDisplayName(displayName);

                    if (player == null || Command_vanish.VANISHED.contains(player) && !plugin.al.isAdmin(sender))
                    {
                        player = getPlayerByDisplayNameAlt(displayName);

                        if (player == null || Command_vanish.VANISHED.contains(player) && !plugin.al.isAdmin(sender))
                        {
                            sender.sendMessage(ChatColor.GRAY + "Can't find player by nickname: " + displayName);
                            return true;
                        }
                    }
                }

                if (player == null)
                {
                    outputCommand.add(arg);
                }
                else
                {
                    nickMatched = true;
                    outputCommand.add(player.getName());
                }
            }
        }

        if (!nickMatched)
        {
            msg("No nicknames replaced in command.");
            return true;
        }

        String newCommand = StringUtils.join(outputCommand, " ");

        if (plugin.cb.isCommandBlocked(newCommand, sender))
        {
            // CommandBlocker handles messages and broadcasts
            return true;
        }

        msg("Sending command: \"" + newCommand + "\".");
        server.dispatchCommand(sender, newCommand);

        return true;
    }
}
