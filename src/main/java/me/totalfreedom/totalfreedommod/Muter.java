package me.totalfreedom.totalfreedommod;

import java.util.ArrayList;
import java.util.List;
import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FSync;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Muter extends FreedomService
{
    private final static List<String> MUTE_COMMANDS = ConfigEntry.BLOCKED_MUTED_COMMANDS.getStringList();
    public final ArrayList MUTED_PLAYERS = new ArrayList();

    public Muter(TotalFreedomMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();

        FPlayer fPlayer = plugin.pl.getPlayerSync(player);

        if (!fPlayer.isMuted())
        {
            return;
        }

        if (plugin.al.isAdminSync(player))
        {
            fPlayer.setMuted(false);
            MUTED_PLAYERS.remove(player.getName());
            return;
        }

        if (fPlayer.isQuietMuted())
        {
            FSync.playerMsg(event.getPlayer(), event.getFormat());
            event.setCancelled(true);
            return;
        }

        FSync.playerMsg(event.getPlayer(), ChatColor.RED + "You are muted, STFU! - You will be unmuted in 5 minutes.");
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        FPlayer fPlayer = plugin.pl.getPlayer(event.getPlayer());

        // Block commands if player is muted
        if (!fPlayer.isMuted())
        {
            return;
        }

        String message = event.getMessage();
        if (plugin.al.isAdmin(player))
        {
            fPlayer.setMuted(false);
            return;
        }

        String cmdName = message.split(" ")[0].toLowerCase();
        if (cmdName.startsWith("/"))
        {
            cmdName = cmdName.substring(1);
        }

        Command command = server.getPluginCommand(cmdName);
        if (command != null)
        {
            cmdName = command.getName().toLowerCase();
        }

        if (MUTE_COMMANDS.contains(cmdName))
        {
            player.sendMessage(ChatColor.RED + "That command is blocked while you are muted.");
            event.setCancelled(true);
            return;
        }

        // TODO: Should this go here?
        if (ConfigEntry.ENABLE_PREPROCESS_LOG.getBoolean())
        {
            FLog.info(String.format("[PREPROCESS_COMMAND] %s(%s): %s", player.getName(), ChatColor.stripColor(player.getDisplayName()), message), true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        FPlayer playerdata = plugin.pl.getPlayer(player);

        if (MUTED_PLAYERS.contains(player.getName()))
        {
            playerdata.setMuted(true);
        }
    }
}
