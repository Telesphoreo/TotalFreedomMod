package me.totalfreedom.totalfreedommod.blocking;

import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

public class EventBlocker extends FreedomService
{

    public EventBlocker(TotalFreedomMod plugin)
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBurn(BlockBurnEvent event)
    {
        if (!ConfigEntry.ALLOW_FIRE_SPREAD.getBoolean())
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockIgnite(BlockIgniteEvent event)
    {
        if (!ConfigEntry.ALLOW_FIRE_PLACE.getBoolean())
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockFromTo(BlockFromToEvent event)
    {
        if (!ConfigEntry.ALLOW_FLUID_SPREAD.getBoolean())
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityExplode(EntityExplodeEvent event)
    {
        if (!ConfigEntry.ALLOW_EXPLOSIONS.getBoolean())
        {
            event.blockList().clear();
            return;
        }

        event.setYield(0.0F);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onExplosionPrime(ExplosionPrimeEvent event)
    {
        if (!ConfigEntry.ALLOW_EXPLOSIONS.getBoolean())
        {
            event.setCancelled(true);
            return;
        }

        event.setRadius(ConfigEntry.EXPLOSIVE_RADIUS.getDouble().floatValue());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityCombust(EntityCombustEvent event)
    {
        if (!ConfigEntry.ALLOW_EXPLOSIONS.getBoolean())
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event)
    {
        if (ConfigEntry.AUTO_ENTITY_WIPE.getBoolean())
        {
            event.setDroppedExp(0);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event)
    {
        switch (event.getCause())
        {
            case LAVA:
            {
                if (!ConfigEntry.ALLOW_LAVA_DAMAGE.getBoolean())
                {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (ConfigEntry.ENABLE_PET_PROTECT.getBoolean())
        {
            Entity entity = event.getEntity();
            if (entity instanceof Tameable)
            {
                if (((Tameable)entity).isTamed())
                {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        if (!ConfigEntry.AUTO_ENTITY_WIPE.getBoolean())
        {
            return;
        }

        if (event.getPlayer().getWorld().getEntities().size() > 750 && !plugin.al.isAdmin(event.getPlayer()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLeavesDecay(LeavesDecayEvent event
    )
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFireworkExplode(FireworkExplodeEvent event)
    {
        if (!ConfigEntry.ALLOW_FIREWORK_EXPLOSION.getBoolean())
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPistonRetract(BlockPistonRetractEvent event)
    {
        if (!ConfigEntry.ALLOW_REDSTONE.getBoolean())
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPistonExtend(BlockPistonExtendEvent event)
    {
        if (!ConfigEntry.ALLOW_REDSTONE.getBoolean())
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockRedstone(BlockRedstoneEvent event)
    {
        if (!ConfigEntry.ALLOW_REDSTONE.getBoolean())
        {
            event.setNewCurrent(0);
        }
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
