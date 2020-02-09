package me.totalfreedom.totalfreedommod.blocking;

import java.util.List;
import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FUtil;
import me.totalfreedom.totalfreedommod.util.Groups;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class BlockBlocker extends FreedomService
{
    public BlockBlocker(TotalFreedomMod plugin)
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
    public void onBlockPlace(BlockPlaceEvent event)
    {
        final Player player = event.getPlayer();

        switch (event.getBlockPlaced().getType())
        {
            case LAVA:
            {
                if (ConfigEntry.ALLOW_LAVA_PLACE.getBoolean())
                {
                    FLog.info(String.format("%s placed lava @ %s", player.getName(), FUtil.formatLocation(event.getBlock().getLocation())));
                }
                else
                {
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                    player.sendMessage(ChatColor.GRAY + "Lava placement is currently disabled.");
                    event.setCancelled(true);
                }
                break;
            }
            case WATER:
            {
                if (ConfigEntry.ALLOW_WATER_PLACE.getBoolean())
                {
                    FLog.info(String.format("%s placed water @ %s", player.getName(), FUtil.formatLocation(event.getBlock().getLocation())));
                }
                else
                {
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                    player.sendMessage(ChatColor.GRAY + "Water placement is currently disabled.");

                    event.setCancelled(true);
                }
                break;
            }
            case FIRE:
            {
                if (ConfigEntry.ALLOW_FIRE_PLACE.getBoolean())
                {
                    FLog.info(String.format("%s placed fire @ %s", player.getName(), FUtil.formatLocation(event.getBlock().getLocation())));
                }
                else
                {
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                    player.sendMessage(ChatColor.GRAY + "Fire placement is currently disabled.");
                    event.setCancelled(true);
                }
                break;
            }
            case TNT:
            {
                if (ConfigEntry.ALLOW_EXPLOSIONS.getBoolean())
                {
                    FLog.info(String.format("%s placed TNT @ %s", player.getName(), FUtil.formatLocation(event.getBlock().getLocation())));
                }
                break;
            }
            case STRUCTURE_BLOCK:
            {
                if (!ConfigEntry.ALLOW_STRUCTURE_BLOCKS.getBoolean())
                {
                    player.sendMessage(ChatColor.GRAY + "Structure blocks are disabled.");
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                    event.setCancelled(true);
                }
                break;
            }
            case JIGSAW:
            {
                if (!ConfigEntry.ALLOW_JIGSAWS.getBoolean())
                {
                    player.sendMessage(ChatColor.GRAY + "Jigsaws are disabled.");
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                    event.setCancelled(true);
                }
                break;
            }
            case GRINDSTONE:
            {
                if (!ConfigEntry.ALLOW_GRINDSTONES.getBoolean())
                {
                    player.sendMessage(ChatColor.GRAY + "Grindstones are disabled.");
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                    event.setCancelled(true);
                }
                break;
            }
            case JUKEBOX:
            {
                if (!ConfigEntry.ALLOW_JUKEBOXES.getBoolean())
                {
                    player.sendMessage(ChatColor.GRAY + "Jukeboxes are disabled.");
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                    event.setCancelled(true);
                }
                break;
            }
            case SPAWNER:
            {
                if (!ConfigEntry.ALLOW_SPAWNERS.getBoolean())
                {
                    player.sendMessage(ChatColor.GRAY + "Spawners are disabled.");
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                    event.setCancelled(true);
                }
                break;
            }
            case BEEHIVE:
            case BEE_NEST:
            {
                if (!ConfigEntry.ALLOW_BEEHIVES.getBoolean())
                {
                    player.sendMessage(ChatColor.GRAY + "Bee hives are disabled.");
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                    event.setCancelled(true);
                }
                break;
            }
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            {
                Skull skull = (Skull)event.getBlockPlaced().getState();
                if (skull.hasOwner())
                {
                    if (skull.getOwner().contains("§"))
                    {
                        skull.setOwner(skull.getOwner().replace("§", ""));
                        SkullMeta meta = (SkullMeta)event.getItemInHand().getItemMeta();
                        if (meta != null)
                        {
                            ItemStack newHead = new ItemStack(Material.PLAYER_HEAD, 1);
                            ItemMeta headMeta = newHead.getItemMeta();
                            headMeta.setDisplayName(ChatColor.YELLOW + "C-sectioned Head");
                            newHead.setItemMeta(headMeta);
                            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), newHead);
                            player.sendMessage(ChatColor.GRAY + "The player head you are attempting to place has a section symbol. Your player head has been C-sectioned.");
                            event.setCancelled(true);
                        }
                    }
                    if (skull.getOwner().length() > 100)
                    {
                        player.sendMessage(ChatColor.GRAY + "Instead of using Pi to crash players, be useful with your life and use it to discover things.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }
        }

        if (Groups.BANNERS.contains(event.getBlockPlaced().getType()))
        {
            Banner banner = (Banner)event.getBlockPlaced().getState();
            List<Pattern> patterns = banner.getPatterns();
            Banner handBanner = (Banner)(((Block)event.getItemInHand()).getState());
            List<Pattern> handPatterns = banner.getPatterns();
            if (patterns.size() >= 2)
            {
                banner.setPatterns(patterns.subList(0, 2));
                handBanner.setPatterns(handPatterns.subList(0, 2));
                player.sendMessage(ChatColor.GRAY + "Your banner had too many patterns on it, so some were removed.");
            }
        }
    }
}