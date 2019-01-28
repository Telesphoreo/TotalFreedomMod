package me.totalfreedom.totalfreedommod.bridge;

import me.telesphoreo.wave.PermissionCheck;
import me.telesphoreo.wave.Wave;
import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.util.FLog;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WaveBridge extends FreedomService
{
    private Wave wavePlugin = null;

    public WaveBridge(TotalFreedomMod plugin)
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

    public Wave getWavePlugin()
    {
        if (wavePlugin == null)
        {
            try
            {
                final Plugin wave = server.getPluginManager().getPlugin("Wave");
                if (wave != null && wave instanceof Wave)
                {
                    wavePlugin = (Wave)wave;
                }
            }
            catch (Exception ex)
            {
                FLog.severe(ex);
            }
        }
        return wavePlugin;
    }

    public void updatePermissions(Player player)
    {
        if (isEnabled())
        {
            PermissionCheck api = new PermissionCheck();
            api.reloadPermissions(player);
        }

    }

    public boolean isEnabled()
    {
        final Wave wave = getWavePlugin();

        return wave != null && wave.isEnabled();
    }
}