package me.totalfreedom.totalfreedommod.player;

import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDataListener extends FreedomService
{
    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    public PlayerDataListener(TotalFreedomMod plugin)
    {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event)
    {
        Player refreshPlayer = null;
        Inventory inventory = event.getView().getTopInventory();
        InventoryType inventoryType = inventory.getType();
        Player player = (Player)event.getWhoClicked();
        FPlayer fPlayer = plugin.pl.getPlayer(player);
        if (inventoryType == InventoryType.PLAYER && fPlayer.isInvSee())
        {
            final InventoryHolder inventoryHolder = inventory.getHolder();
            if (inventoryHolder != null && inventoryHolder instanceof HumanEntity)
            {
                Player invOwner = (Player)inventoryHolder;
                Rank recieverRank = plugin.rm.getRank(player);
                Rank playerRank = plugin.rm.getRank(invOwner);
                if (playerRank.ordinal() >= recieverRank.ordinal() || !invOwner.isOnline())
                {
                    event.setCancelled(true);
                    refreshPlayer = player;
                }
            }
        }
        if (refreshPlayer != null)
        {
            final Player p = refreshPlayer;
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    p.updateInventory();
                }
            }.runTaskLater(plugin, 20L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event)
    {
        Player refreshPlayer = null;
        Inventory inventory = event.getView().getTopInventory();
        InventoryType inventoryType = inventory.getType();
        Player player = (Player)event.getPlayer();
        FPlayer fPlayer = plugin.pl.getPlayer(player);
        if (inventoryType == InventoryType.PLAYER && fPlayer.isInvSee())
        {
            fPlayer.setInvSee(false);
            refreshPlayer = player;
        }
        if (refreshPlayer != null)
        {
            final Player p = refreshPlayer;
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    p.updateInventory();
                }
            }.runTaskLater(plugin, 20L);
        }
    }
}
