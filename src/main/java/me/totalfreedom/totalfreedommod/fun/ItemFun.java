package me.totalfreedom.totalfreedommod.fun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.util.DepreciationAggregator;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ItemFun extends FreedomService
{
    private static final String COOLDOWN_MESSAGE = ChatColor.RED + "You're on cooldown for this feature.";
    private final Random random = new Random();
    private final Map<String, List<String>> cooldownTracker = new HashMap<>();
    private final Map<Player, Float> orientationTracker = new HashMap<>();
    public List<Player> explosivePlayers = new ArrayList<Player>();

    public ItemFun(TotalFreedomMod plugin)
    {
        super(plugin);
    }

    private void cooldown(Player player, String feature, int seconds)
    {
        if (cooldownTracker.get(player.getName()) == null)
        {
            List<String> featureList = new ArrayList<>();
            featureList.add(feature);
            cooldownTracker.put(player.getName(), featureList);
        }
        else
        {
            cooldownTracker.get(player.getName()).add(feature);
        }
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                cooldownTracker.get(player.getName()).remove(feature);
            }
        }.runTaskLater(plugin, seconds * 20);
    }

    public boolean onCooldown(Player player, String feature)
    {
        if (cooldownTracker.get(player.getName()) == null)
        {
            return false;
        }
        return cooldownTracker.get(player.getName()).contains(feature);
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.LEFT_CLICK_AIR
                || event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            return;
        }

        final Player player = event.getPlayer();
        final FPlayer fPlayer = plugin.pl.getPlayer(player);

        switch (event.getMaterial())
        {
            case TROPICAL_FISH:
            {
                final int RADIUS_HIT = 5;
                final int STRENGTH = 4;

                if (!plugin.al.isSeniorAdmin(player))
                {
                    final StringBuilder msg = new StringBuilder();
                    final char[] chars = ("That's clownery, luv").toCharArray();
                    for (char c : chars)
                    {
                        msg.append(FUtil.randomChatColor()).append(c);
                    }
                    player.sendMessage(msg.toString());

                    player.getEquipment().getItemInMainHand().setType(Material.POTATO);
                    break;
                }

                event.setCancelled(true);
                boolean didHit = false;

                final Location playerLoc = player.getLocation();
                final Vector playerLocVec = playerLoc.toVector();

                final List<Player> players = player.getWorld().getPlayers();
                for (final Player target : players)
                {
                    if (target == player)
                    {
                        continue;
                    }

                    final Location targetPos = target.getLocation();
                    final Vector targetPosVec = targetPos.toVector();

                    try
                    {
                        if (targetPosVec.distanceSquared(playerLocVec) < (RADIUS_HIT * RADIUS_HIT))
                        {
                            FUtil.setFlying(player, false);
                            target.setVelocity(targetPosVec.subtract(playerLocVec).normalize().multiply(STRENGTH));
                            didHit = true;
                        }
                    }
                    catch (IllegalArgumentException ex)
                    {
                    }
                }

                if (didHit)
                {
                    final Sound[] sounds = Sound.values();
                    for (Sound sound : sounds)
                    {
                        if (sound.toString().contains("HIT"))
                        {
                            playerLoc.getWorld().playSound(randomOffset(playerLoc, 5.0), sound, 100.0f, randomDoubleRange(0.5, 2.0).floatValue());
                        }
                    }
                }
                break;
            }

            case CARROT:
            {
                if (!ConfigEntry.ALLOW_EXPLOSIONS.getBoolean())
                {
                    break;
                }

                if (!plugin.al.isSeniorAdmin(player))
                {
                    break;
                }

                if (player.getWorld().equals(plugin.wm.masterBuilderWorld.getWorld()) && plugin.mbwr.doRestrict(player))
                {
                    break;
                }

                /*if (player.getWorld().equals(plugin.wm.hubworld.getWorld()) && plugin.hwr.doRestrict(player))
                {
                    break;
                }*/

                Location location = player.getLocation().clone();

                Vector playerPosition = location.toVector().add(new Vector(0.0, 1.65, 0.0));
                Vector playerDirection = location.getDirection().normalize();

                double distance = 150.0;
                Block targetBlock = DepreciationAggregator.getTargetBlock(player, null, Math.round((float)distance));
                if (targetBlock != null)
                {
                    distance = location.distance(targetBlock.getLocation());
                }

                final List<Block> affected = new ArrayList<>();

                Block lastBlock = null;
                for (double offset = 0.0; offset <= distance; offset += (distance / 25.0))
                {
                    Block block = playerPosition.clone().add(playerDirection.clone().multiply(offset)).toLocation(player.getWorld()).getBlock();

                    if (!block.equals(lastBlock))
                    {
                        if (block.isEmpty())
                        {
                            affected.add(block);
                            block.setType(Material.TNT);
                        }
                        else
                        {
                            break;
                        }
                    }

                    lastBlock = block;
                }

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        for (Block tntBlock : affected)
                        {
                            TNTPrimed tnt = tntBlock.getWorld().spawn(tntBlock.getLocation(), TNTPrimed.class);
                            tnt.setFuseTicks(5);
                            tntBlock.setType(Material.AIR);
                        }
                    }
                }.runTaskLater(plugin, 30L);

                event.setCancelled(true);
                break;
            }

            case BONE:
            {
                if (!fPlayer.mobThrowerEnabled())
                {
                    break;
                }

                Location player_pos = player.getLocation();
                Vector direction = player_pos.getDirection().normalize();

                LivingEntity rezzed_mob = (LivingEntity)player.getWorld().spawnEntity(player_pos.add(direction.multiply(2.0)), fPlayer.mobThrowerCreature());
                rezzed_mob.setVelocity(direction.multiply(fPlayer.mobThrowerSpeed()));
                fPlayer.enqueueMob(rezzed_mob);

                event.setCancelled(true);
                break;
            }

            case GUNPOWDER:
            {
                if (!fPlayer.isMP44Armed())
                {
                    break;
                }

                event.setCancelled(true);

                if (fPlayer.toggleMP44Firing())
                {
                    fPlayer.startArrowShooter(plugin);
                }
                else
                {
                    fPlayer.stopArrowShooter();
                }
                break;
            }

            case BLAZE_ROD:
            {
                if (!ConfigEntry.ALLOW_EXPLOSIONS.getBoolean())
                {
                    break;
                }

                if (!plugin.al.isSeniorAdmin(player))
                {
                    break;
                }

                if (player.getWorld().equals(plugin.wm.masterBuilderWorld.getWorld()) && plugin.mbwr.doRestrict(player))
                {
                    break;
                }

                event.setCancelled(true);
                Block targetBlock;

                if (event.getAction().equals(Action.LEFT_CLICK_AIR))
                {
                    targetBlock = DepreciationAggregator.getTargetBlock(player, null, 120);
                }
                else
                {
                    targetBlock = event.getClickedBlock();
                }

                if (targetBlock == null)
                {
                    player.sendMessage("Can't resolve target block.");
                    break;
                }

                player.getWorld().createExplosion(targetBlock.getLocation(), 4F, true);
                player.getWorld().strikeLightning(targetBlock.getLocation());

                break;
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event)
    {
        Entity entity = event.getEntity();
        Arrow arrow = null;
        if (entity instanceof Arrow)
        {
            arrow = (Arrow)entity;
        }
        if (arrow != null && (arrow.getShooter() instanceof Player))
        {
            if (explosivePlayers.contains((Player)arrow.getShooter()))
            {
                arrow.getLocation().getWorld().createExplosion(arrow.getLocation().getX(), arrow.getLocation().getY(), arrow.getLocation().getZ(), ConfigEntry.EXPLOSIVE_RADIUS.getDouble().floatValue(), false, ConfigEntry.ALLOW_EXPLOSIONS.getBoolean());
                arrow.remove();
            }
        }
    }

    private Location randomOffset(Location a, double magnitude)
    {
        return a.clone().add(randomDoubleRange(-1.0, 1.0) * magnitude, randomDoubleRange(-1.0, 1.0) * magnitude, randomDoubleRange(-1.0, 1.0) * magnitude);
    }

    private Double randomDoubleRange(double min, double max)
    {
        return min + (random.nextDouble() * ((max - min) + 1.0));
    }
}