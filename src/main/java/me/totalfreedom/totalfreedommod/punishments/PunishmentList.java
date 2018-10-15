package me.totalfreedom.totalfreedommod.punishments;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.util.FLog;
import net.pravian.aero.config.YamlConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class PunishmentList extends FreedomService
{

    private final Set<Punishment> punishments = Sets.newHashSet();
    public static final String CONFIG_FILENAME = "punishments.yml";

    //
    private final YamlConfig config;

    public PunishmentList(TotalFreedomMod plugin)
    {
        super(plugin);
        this.config = new YamlConfig(plugin, CONFIG_FILENAME);
    }

    @Override
    protected void onStart()
    {
        config.load();

        punishments.clear();
        for (String id : config.getKeys(false))
        {
            if (!config.isConfigurationSection(id))
            {
                FLog.warning("Failed to load punishment number " + id + "!");
                continue;
            }

            Punishment punishment = new Punishment();
            punishment.loadFrom(config.getConfigurationSection(id));

            if (!punishment.isValid())
            {
                FLog.warning("Not adding punishment number " + id + ". Missing information.");
                continue;
            }

            punishments.add(punishment);
        }

        FLog.info("Loaded " + punishments.size() + " punishments.");
    }

    @Override
    protected void onStop()
    {
        saveAll();
        logger.info("Saved " + punishments.size() + " player bans");
    }

    public void saveAll()
    {
        config.clear();

        for (Punishment punishment : punishments)
        {
            punishment.saveTo(config.createSection(String.valueOf(punishment.hashCode())));
        }

        // Save config
        config.save();
    }

    public int clear()
    {
        int removed = punishments.size();
        punishments.clear();
        saveAll();

        return removed;
    }

    public int clear(String username)
    {
        List<Punishment> removed = new ArrayList<>();

        for (Punishment punishment : punishments)
        {
            if (punishment.getUsername().equalsIgnoreCase(username))
            {
                removed.add(punishment);
            }
        }

        if (removed.size() != 0)
        {
            punishments.removeAll(removed);
            saveAll();
        }

        return removed.size();
    }

    public int getLastPunishmentID()
    {
        int size = punishments.size();

        if (size == 0)
        {
            return 1;
        }

        return size;
    }

    public boolean logPunishment(Punishment punishment)
    {
        if (punishments.add(punishment))
        {
            saveAll();
            return true;
        }

        return false;
    }

    public boolean getPlayerPunishments(String player, CommandSender sender)
    {
        final StringBuilder message = new StringBuilder(ChatColor.GOLD + "Player: " + ChatColor.BLUE + player + "\n");
        message.append(ChatColor.GOLD + "--------------\n");
        String banned_by;
        String date;
        String ip;
        String reason;
        for (String id : config.getKeys(false))
        {
            if (config.getString(id + ".username").equals(player) && config.getString(id + ".type").equals("ban"))
            {
                banned_by = ChatColor.BLUE + config.getString(id + ".by");
                date = ChatColor.BLUE + config.getString(id + ".issued_on");
                ip = ChatColor.BLUE + config.getString(id + ".ip");
                reason = ChatColor.BLUE + config.getString(id + ".reason");
                message.append(ChatColor.GOLD + "Issued by: " + banned_by + "\n");
                message.append(ChatColor.GOLD + "Date: ").append(date).append("\n");
                message.append(ChatColor.GOLD + "IP: ").append(ip).append("\n");
                message.append(ChatColor.GOLD + "Reason: " + reason + "\n");
                message.append(ChatColor.GOLD + "--------------\n");
            }
        }
        sender.sendMessage(message.toString());
        return true;
    }
}
