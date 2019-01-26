package me.totalfreedom.totalfreedommod.bridge;

import com.ryanmichela.bukkitssh.BukkitSSH;
import me.totalfreedom.bukkitssh.SSHCommandEvent;
import me.totalfreedom.bukkitssh.SSHPreLoginEvent;
import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.admin.Admin;
import me.totalfreedom.totalfreedommod.util.FLog;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

public class BukkitSSHBridge extends FreedomService
{
    private BukkitSSH BukkitSSHPlugin = null;

    public BukkitSSHBridge(TotalFreedomMod plugin)
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSSHPreLogin(SSHPreLoginEvent event)
    {
        final String ip = event.getIp();
        if (ip == null || ip.isEmpty())
        {
            return;
        }

        final Admin admin = plugin.al.getEntryByIpFuzzy(ip);

        if (admin == null || !admin.isActive() || !admin.getRank().hasConsoleVariant())
        {
            return;
        }

        event.setBypassPassword(true);
        event.setName(admin.getName());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSSHCommand(SSHCommandEvent event)
    {
        if (plugin.cb.isCommandBlocked(event.getCommand(), event.getSender()))
        {
            event.setCancelled(true);
        }
    }

    public BukkitSSH getBukkitSSHPlugin()
    {
        if (BukkitSSHPlugin == null)
        {
            try
            {
                final Plugin BukkitSSH = server.getPluginManager().getPlugin("BukkitSSH");
                if (BukkitSSH != null)
                {
                    if (BukkitSSH instanceof BukkitSSH)
                    {
                        BukkitSSHPlugin = (BukkitSSH)BukkitSSH;
                    }
                }
            }
            catch (Exception ex)
            {
                FLog.severe(ex);
            }
        }

        return BukkitSSHPlugin;
    }
}
