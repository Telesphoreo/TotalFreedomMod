package me.totalfreedom.totalfreedommod.command;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.world.gamemode.GameMode;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitWorldGuardPlatform;
import com.sk89q.worldguard.config.ConfigurationManager;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.session.SessionManager;
import java.nio.file.Path;
import java.util.Map;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.TELNET_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "Wipe all WorldGuard regions for a specified world.", usage = "/<command> <world>")
public class Command_wiperegions extends FreedomCommand
{
    private WorldGuard worldGuardPlugin;

    public WorldGuard getWorldGuardPlugin()
    {
        return worldGuardPlugin;
    }

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        World world = server.getWorld(args[0]);
        if (world == null)
        {
            msg("World: \"" + args[0] + "\" not found.");
            return true;
        }
        if (world.equals(plugin.wm.adminworld.getWorld()))
        {
            checkRank(Rank.SENIOR_ADMIN);
        }
        if (wipeRegions(world))
        {
            FUtil.adminAction(sender.getName(), "Wiping regions for world: " + world.getName(), true);
            return true;
        }
        else
        {
            msg(ChatColor.RED + "No regions were found in: \"" + world.getName() + "\".");
            return true;
        }
    }

    public Boolean wipeRegions(World world)
    {
        FLog.info("wipe regions");
        BukkitWorldGuardPlatform wgp = new BukkitWorldGuardPlatform();
        RegionContainer container = wgp.getRegionContainer();
        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);
        RegionManager rm = container.get(weWorld);
        if (rm != null)
        {
            Map<String, ProtectedRegion> regions = rm.getRegions();
            for (ProtectedRegion region : regions.values())
            {
                rm.removeRegion(region.getId());
            }
            return true;
        }
        return false;
    }
}
