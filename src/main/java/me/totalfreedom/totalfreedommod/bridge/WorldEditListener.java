package me.totalfreedom.totalfreedommod.bridge;

import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.command.Command_vanish;
import me.totalfreedom.worldedit.LimitChangedEvent;
import me.totalfreedom.worldedit.SelectionChangedEvent;
import net.pravian.aero.component.PluginListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class WorldEditListener extends PluginListener<TotalFreedomMod>
{
    public WorldEditListener(TotalFreedomMod plugin)
    {
        super(plugin);
    }

    @EventHandler
    public void onSelectionChange(final SelectionChangedEvent event)
    {
        final Player player = event.getPlayer();

        if (plugin.al.isAdmin(player))
        {
            return;
        }

        if (plugin.pa.isInProtectedArea(
                event.getMinVector(),
                event.getMaxVector(),
                event.getWorld().getName()))
        {
            player.sendMessage(ChatColor.RED + "The region that you selected contained a protected area. Selection cleared.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLimitChanged(LimitChangedEvent event)
    {
        Player player = event.getPlayer();
        Player target = event.getTarget();

        if (plugin.al.isAdmin(player))
        {
            return;
        }

        if (!player.equals(target) || Command_vanish.VANISHED.contains(target))
        {
            player.sendMessage(ChatColor.RED + "Could not resolve session for " + player.getName());
            event.setCancelled(true);
            return;
        }

        if (event.getLimit() < 0 || event.getLimit() > 200000)
        {
            player.sendMessage(ChatColor.RED + "You cannot set your limit higher than 200000 or to -1!");
            event.setCancelled(true);
            return;
        }
    }
}
