package me.totalfreedom.totalfreedommod;

import io.papermc.lib.PaperLib;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;
import me.rayzr522.jsonmessage.JSONMessage;
import me.totalfreedom.totalfreedommod.command.Command_vanish;
import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.masterbuilder.MasterBuilder;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.playerverification.VPlayer;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FSync;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class LoginProcess extends FreedomService
{
    public static final int DEFAULT_PORT = 25565;
    public static final int MIN_USERNAME_LENGTH = 2;
    public static final int MAX_USERNAME_LENGTH = 20;
    public static final Pattern USERNAME_REGEX = Pattern.compile("^[\\w\\d_]{3,20}$");
    //
    @Getter
    @Setter
    private static boolean lockdownEnabled = false;

    public LoginProcess(TotalFreedomMod plugin)
    {
        super(plugin);
    }

    public static boolean isLockdownEnabled()
    {
        return lockdownEnabled;
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    /*
     * Banning and Permban checks are their respective services
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event)
    {
        final String ip = event.getAddress().getHostAddress().trim();
        final boolean isAdmin = plugin.al.getEntryByIp(ip) != null;

        // Check if the player is already online
        for (Player onlinePlayer : server.getOnlinePlayers())
        {
            if (!onlinePlayer.getName().equalsIgnoreCase(event.getName()))
            {
                continue;
            }

            if (isAdmin)
            {
                event.allow();
                FSync.playerKick(onlinePlayer, "An admin just logged in with the username you are using.");
                return;
            }

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Your username is already logged into this server.");
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        final Player player = event.getPlayer();
        final String username = player.getName();
        final String ip = event.getAddress().getHostAddress().trim();

        // Check username length
        if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH)
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your username is an invalid length (must be between 3 and 20 characters long).");
            return;
        }

        // Check username characters
        if (!USERNAME_REGEX.matcher(username).find())
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your username contains invalid characters.");
            return;
        }

        // Check force-IP match
        if (ConfigEntry.FORCE_IP_ENABLED.getBoolean())
        {
            final String hostname = event.getHostname().replace("\u0000FML\u0000", ""); // Forge fix - https://github.com/TotalFreedom/TotalFreedomMod/issues/493
            final String connectAddress = ConfigEntry.SERVER_ADDRESS.getString();
            final int connectPort = server.getPort();

            if (!hostname.equalsIgnoreCase(connectAddress + ":" + connectPort) && !hostname.equalsIgnoreCase(connectAddress + ".:" + connectPort))
            {
                final int forceIpPort = ConfigEntry.FORCE_IP_PORT.getInteger();
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                        ConfigEntry.FORCE_IP_KICKMSG.getString()
                                .replace("%address%", ConfigEntry.SERVER_ADDRESS.getString() + (forceIpPort == DEFAULT_PORT ? "" : ":" + forceIpPort)));
                return;
            }
        }

        // Check if player is admin
        final boolean isAdmin = plugin.al.getEntryByIp(ip) != null;

        // Validation below this point
        if (isAdmin) // Player is admin
        {
            // Force-allow log in
            event.allow();

            int count = server.getOnlinePlayers().size();
            if (count >= server.getMaxPlayers())
            {
                for (Player onlinePlayer : server.getOnlinePlayers())
                {
                    if (!plugin.al.isAdmin(onlinePlayer))
                    {
                        onlinePlayer.kickPlayer("You have been kicked to free up room for an admin.");
                        count--;
                    }

                    if (count < server.getMaxPlayers())
                    {
                        break;
                    }
                }
            }

            if (count >= server.getMaxPlayers())
            {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, "The server is full and a player could not be kicked, sorry!");
                return;
            }

            return;
        }

        // Player is not an admin
        // Server full check
        if (server.getOnlinePlayers().size() >= server.getMaxPlayers())
        {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, "Sorry, but this server is full.");
            return;
        }

        // Admin-only mode
        if (ConfigEntry.ADMIN_ONLY_MODE.getBoolean())
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is temporarily open to admins only.");
            return;
        }

        // Lockdown mode
        if (lockdownEnabled)
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is currently in lockdown mode.");
            return;
        }

        // Whitelist
        if (server.hasWhitelist() && !player.isWhitelisted())
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "You are not whitelisted on this server.");
            return;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();
        final FPlayer fPlayer = plugin.pl.getPlayer(player);
        VPlayer vPlayer = plugin.pv.getVerificationPlayer(player);

        player.sendTitle(FUtil.colorize(ConfigEntry.SERVER_LOGIN_TITLE.getString()), FUtil.colorize(ConfigEntry.SERVER_LOGIN_SUBTITLE.getString()), 20, 100, 60);

        if (ConfigEntry.ALLOW_TPR_ON_JOIN.getBoolean())
        {
            int x = FUtil.randomInteger(-10000, 10000);
            int z = FUtil.randomInteger(-10000, 10000);
            int y = player.getWorld().getHighestBlockYAt(x, z);
            Location location = new Location(player.getLocation().getWorld(), x, y, z);
            PaperLib.teleportAsync(player, location);
            player.sendMessage(ChatColor.GOLD + "You have been teleported to a random location automatically.");
            return;
        }

        if (ConfigEntry.ALLOW_CLEAR_ON_JOIN.getBoolean())
        {
            player.getInventory().clear();
            player.sendMessage(ChatColor.AQUA + "Your inventory has been cleared automatically.");
            return;
        }

        if (!ConfigEntry.SERVER_TABLIST_HEADER.getString().isEmpty())
        {
            player.setPlayerListHeader(FUtil.colorize(ConfigEntry.SERVER_TABLIST_HEADER.getString()).replace("\\n", "\n"));
        }

        if (!ConfigEntry.SERVER_TABLIST_FOOTER.getString().isEmpty())
        {
            player.setPlayerListFooter(FUtil.colorize(ConfigEntry.SERVER_TABLIST_FOOTER.getString()).replace("\\n", "\n"));
        }

        for (Player p : Command_vanish.VANISHED)
        {
            if (!plugin.al.isAdmin(player))
            {
                player.hidePlayer(plugin, p);
            }
        }

        if (!plugin.al.isAdmin(player))
        {
            if (plugin.mbl.isMasterBuilder(player))
            {
                MasterBuilder masterBuilder = plugin.mbl.getMasterBuilder(player);
                if (masterBuilder.getTag() != null)
                {
                    fPlayer.setTag(FUtil.colorize(masterBuilder.getTag()));
                }
            }
            else
            {
                if (vPlayer.getEnabled() && vPlayer.getTag() != null)
                {
                    fPlayer.setTag(FUtil.colorize(vPlayer.getTag()));
                }
            }
        }

        int noteCount = vPlayer.getNotes().size();
        if (noteCount != 0)
        {
            String noteMessage = "This player has " + noteCount + " staff note" + (noteCount > 1 ? "s" : "") + ".";
            JSONMessage notice = JSONMessage.create(ChatColor.GOLD + noteMessage + " Click here to view them.")
                    .tooltip("Click here to view them.")
                    .runCommand("/notes " + player.getName() + " list");
            FLog.info(noteMessage);
            for (Player p : server.getOnlinePlayers())
            {
                if (plugin.al.isAdminImpostor(p))
                {
                    notice.send(p);
                }
            }
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (ConfigEntry.ADMIN_ONLY_MODE.getBoolean())
                {
                    player.sendMessage(ChatColor.RED + "Server is currently closed to non-admins.");
                }

                if (lockdownEnabled)
                {
                    FUtil.playerMsg(player, "Warning: Server is currenty in lockdown-mode, new players will not be able to join!", ChatColor.RED);
                }
            }
        }.runTaskLater(plugin, 20L);
    }

    public boolean setLockdownEnabled(boolean toggle)
    {
        return lockdownEnabled = toggle;
    }
}
