package me.totalfreedom.totalfreedommod.i18n;

import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FUtil;
import net.pravian.aero.config.YamlConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        return FUtil.colorize(config.getString(message, ChatColor.DARK_RED + "String " + message + " was not found in the messages.yml file."));
    }

    public String getMessage(String message, Player player)
    {
        return FUtil.colorize(
                config.getString(message,
                        ChatColor.DARK_RED + "String " + message + " was not found in the messages.yml file.")
                        .replace("%player%", player.getName()));
    }

    public String getMessage(String message, CommandSender sender, String inputMessage)
    {
        return FUtil.colorize(
                config.getString(message,
                        ChatColor.DARK_RED + "String " + message + " was not found in the messages.yml file.")
                        .replace("%sender%", sender.getName())
                        .replace("%input%", inputMessage));
    }

    public String getMessage(String message, Player player, String inputMessage)
    {
        return FUtil.colorize(
                config.getString(message,
                        ChatColor.DARK_RED + "String " + message + " was not found in the messages.yml file.")
                        .replace("%player%", player.getName())
                        .replace("%input%", inputMessage));
    }

    public String getMessage(String message, String inputMessage)
    {
        return FUtil.colorize(
                config.getString(message,
                        ChatColor.DARK_RED + "String " + message + " was not found in the messages.yml file.")
                        .replace("%input%", inputMessage));
    }

    public String getMessage(String message, int amount)
    {
        StringBuilder output = new StringBuilder(
                FUtil.colorize(config.getString(message,
                        ChatColor.DARK_RED + "String " + message + " was not found in the messages.yml file."))
                        .replace("%amount%", Integer.toString(amount)));

        return output.toString();
    }

    public String getMessage(String message, double amount)
    {
        StringBuilder output = new StringBuilder(
                FUtil.colorize(config.getString(message,
                        ChatColor.DARK_RED + "String " + message + " was not found in the messages.yml file."))
                        .replace("%amount%", Double.toString(amount)));

        return output.toString();
    }

    public String getMessage(String message, int amount, String inputMessage)
    {
        StringBuilder output = new StringBuilder(
                FUtil.colorize(config.getString(message,
                        ChatColor.DARK_RED + "String " + message + " was not found in the messages.yml file."))
                        .replace("%amount%", Integer.toString(amount))
                        .replace("%input%", inputMessage));

        return output.toString();
    }
}