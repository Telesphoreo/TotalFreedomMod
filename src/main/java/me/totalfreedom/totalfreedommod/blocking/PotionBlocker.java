package me.totalfreedom.totalfreedommod.blocking;

import java.util.Collection;
import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class PotionBlocker extends FreedomService
{
    public PotionBlocker(TotalFreedomMod plugin)
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

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onThrowPotion(PotionSplashEvent event)
    {
        ThrownPotion potion = event.getEntity();
        ProjectileSource projectileSource = potion.getShooter();
        Player player = null;
        if (projectileSource instanceof Player)
        {
            player = (Player)projectileSource;
        }

        if (isDeathPotion(potion.getEffects()))
        {
            if (player != null)
            {
                player.sendMessage(ChatColor.RED + "You are not allowed to use death potions.");
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onThrowLingeringPotion(LingeringPotionSplashEvent event)
    {
        ThrownPotion potion = event.getEntity();
        ProjectileSource projectileSource = potion.getShooter();
        Player player = null;
        if (projectileSource instanceof Player)
        {
            player = (Player)projectileSource;
        }

        if (isDeathPotion(potion.getEffects()))
        {
            if (player != null)
            {
                player.sendMessage(ChatColor.RED + "You are not allowed to use death potions.");
            }
            event.setCancelled(true);
        }
    }

    public boolean isDeathPotion(Collection<PotionEffect> effects)
    {
        for (PotionEffect effect : effects)
        {
            int amplifier = effect.getAmplifier();
            if (effect.getType().equals(PotionEffectType.HEAL) && (amplifier == 29 || amplifier == 61 || amplifier == 93 || amplifier == 125))
            {
                return true;
            }
        }
        return false;
    }
}