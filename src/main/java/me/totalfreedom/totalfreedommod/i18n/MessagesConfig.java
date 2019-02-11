package me.totalfreedom.totalfreedommod.i18n;

import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.util.FLog;
import net.pravian.aero.config.YamlConfig;

public class MessagesConfig extends FreedomService
{
    public static final String CONFIG_FILENAME = "messages.yml";
    private final YamlConfig config;

    public MessagesConfig(TotalFreedomMod plugin)
    {
        super(plugin);
        this.config = new YamlConfig(plugin, CONFIG_FILENAME);
    }

    @Override
    protected void onStart()
    {
        config.load();
        FLog.info("Loaded localization for TotalFreedomMod.");
    }

    @Override
    protected void onStop()
    {
    }

    public String getMessage(String message)
    {
        return config.getString(message);
    }
}