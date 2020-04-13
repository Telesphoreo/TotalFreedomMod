package me.totalfreedom.totalfreedommod.command;

import java.math.RoundingMode;
import java.text.NumberFormat;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import net.pravian.aero.util.Ips;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "See details of a player", usage = "/<command> <player>")
public class Command_whois extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }

        Player player = getNonVanishedPlayer(args[0]);

        if (player == null)
        {
            msg(FreedomCommand.PLAYER_NOT_FOUND);
            return true;
        }

        FPlayer fPlayer = plugin.pl.getPlayer(player);

        NumberFormat format = NumberFormat.getInstance();
        format.setRoundingMode(RoundingMode.DOWN);
        format.setMaximumFractionDigits(1);
        String formattedHealth = format.format(player.getHealth()) + "/" + format.format(player.getMaxHealth());

        final StringBuilder output = new StringBuilder();

        output.append(ChatColor.GREEN + "--- ");
        output.append(ChatColor.GOLD + "WhoIs: ");
        output.append(ChatColor.RED + player.getName());
        output.append(ChatColor.GREEN + " ---").append("\n");
        //
        if (plugin.esb.isEnabled() && plugin.esb.getNickname(player.getName()) != null)
        {
            output.append(ChatColor.GOLD + " - Nickname: ");
            output.append(ChatColor.RED + plugin.esb.getNickname(player.getName())).append("\n");
        }
        //
        output.append(ChatColor.GOLD + " - Health: ");
        output.append(ChatColor.RED + formattedHealth).append("\n");
        //
        if (plugin.al.isAdmin(sender))
        {
            String location = player.getWorld().getName() + ", " +
                    player.getLocation().getBlockX() + ", " +
                    player.getLocation().getBlockY() + ", " +
                    player.getLocation().getBlockZ();

            output.append(ChatColor.GOLD + " - Location: ");
            output.append(ChatColor.RED + location).append("\n");
        }
        //
        if (plugin.al.isAdmin(sender))
        {
            output.append(ChatColor.GOLD + " - IP Address: ");
            output.append(ChatColor.RED + Ips.getIp(player)).append("\n");
        }
        //
        if (plugin.esb.isEnabled())
        {
            output.append(ChatColor.GOLD + " - Playtime: ");
            output.append(ChatColor.RED + plugin.esb.getPlaytime(player.getName())).append("\n");
            //
            output.append(ChatColor.GOLD + " - AFK: ");
            output.append(ChatColor.RED + (plugin.esb.isAFK(player.getName()) ? "true, for " + plugin.esb.getAFKDuration(player.getName()) : "false")).append("\n");
            //
            output.append(ChatColor.GOLD + " - God mode: ");
            output.append(ChatColor.RED + "" + plugin.esb.getGodMode(player.getName())).append("\n");
            //
            output.append(ChatColor.GOLD + " - Speed: ");
            output.append(ChatColor.RED + "" + plugin.esb.getSpeed(player.getName())).append("\n");
        }
        //
        output.append(ChatColor.GOLD + " - Gamemode: ");
        output.append(ChatColor.RED + player.getGameMode().toString().toLowerCase()).append("\n");
        //
        output.append(ChatColor.GOLD + " - Flying: ");
        output.append(ChatColor.RED + "" + player.isFlying()).append("\n");
        //
        output.append(ChatColor.GOLD + " - Muted: ");
        output.append(ChatColor.RED + "" + fPlayer.isMuted()).append("\n");
        //
        output.append(ChatColor.GOLD + " - Caged: ");
        output.append(ChatColor.RED + "" + fPlayer.getCageData().isCaged()).append("\n");
        sender.sendMessage(output.toString());
        return true;
    }
}