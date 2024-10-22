package me.totalfreedom.totalfreedommod.config;

import java.util.List;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;

public enum ConfigEntry
{
    FORCE_IP_ENABLED(Boolean.class, "forceip.enabled"),
    FORCE_IP_PORT(Integer.class, "forceip.port"),
    FORCE_IP_KICKMSG(String.class, "forceip.kickmsg"),
    //
    ALLOW_EXPLOSIONS(Boolean.class, "allow.explosions"),
    ALLOW_FIRE_PLACE(Boolean.class, "allow.fire_place"),
    ALLOW_FIRE_SPREAD(Boolean.class, "allow.fire_spread"),
    ALLOW_FLUID_SPREAD(Boolean.class, "allow.fluid_spread"),
    ALLOW_LAVA_DAMAGE(Boolean.class, "allow.lava_damage"),
    ALLOW_LAVA_PLACE(Boolean.class, "allow.lava_place"),
    ALLOW_TNT_MINECARTS(Boolean.class, "allow.tnt_minecarts"),
    ALLOW_WATER_PLACE(Boolean.class, "allow.water_place"),
    ALLOW_REDSTONE(Boolean.class, "allow.redstone"),
    ALLOW_FIREWORK_EXPLOSION(Boolean.class, "allow.fireworks"),
    ALLOW_FROSTWALKER(Boolean.class, "allow.frostwalker"),
    ALLOW_UNSAFE_ENCHANTMENTS(Boolean.class, "allow.unsafe_enchantments"),
    ALLOW_BELLS(Boolean.class, "allow.bells"),
    ALLOW_ARMOR_STANDS(Boolean.class, "allow.armorstands"),
    ALLOW_MINECARTS(Boolean.class, "allow.minecarts"),
    ALLOW_CLEAR_ON_JOIN(Boolean.class, "allow.clearonjoin"),
    ALLOW_TPR_ON_JOIN(Boolean.class, "allow.tpronjoin"),
    ALLOW_STRUCTURE_BLOCKS(Boolean.class, "allow.structureblocks"),
    ALLOW_JIGSAWS(Boolean.class, "allow.jigsaws"),
    ALLOW_GRINDSTONES(Boolean.class, "allow.grindstones"),
    ALLOW_JUKEBOXES(Boolean.class, "allow.jukeboxes"),
    ALLOW_SPAWNERS(Boolean.class, "allow.spawners"),
    ALLOW_BEEHIVES(Boolean.class, "allow.beehives"),
    //
    BLOCKED_WILDCARD_COMMANDS(List.class, "blocked.wildcard_commands"),
    BLOCKED_MUTED_COMMANDS(List.class, "blocked.muted_commands"),
    BLOCKED_CHATCODES(String.class, "blocked.chatcodes"),
    //
    MOB_LIMITER_ENABLED(Boolean.class, "moblimiter.enabled"),
    MOB_LIMITER_MAX(Integer.class, "moblimiter.max"),
    MOB_LIMITER_DISABLE_DRAGON(Boolean.class, "moblimiter.disable.dragon"),
    MOB_LIMITER_DISABLE_GHAST(Boolean.class, "moblimiter.disable.ghast"),
    MOB_LIMITER_DISABLE_GIANT(Boolean.class, "moblimiter.disable.giant"),
    MOB_LIMITER_DISABLE_SLIME(Boolean.class, "moblimiter.disable.slime"),
    //
    HTTPD_ENABLED(Boolean.class, "httpd.enabled"),
    HTTPD_PORT(Integer.class, "httpd.port"),
    HTTPD_PUBLIC_FOLDER(String.class, "httpd.public_folder"),
    //
    SERVER_COLORFUL_MOTD(Boolean.class, "server.colorful_motd"),
    SERVER_NAME(String.class, "server.name"),
    SERVER_ADDRESS(String.class, "server.address"),
    SERVER_MOTD(String.class, "server.motd"),
    SERVER_LOGIN_TITLE(String.class, "server.login_title.title"),
    SERVER_LOGIN_SUBTITLE(String.class, "server.login_title.subtitle"),
    SERVER_OWNERS(List.class, "server.owners"),
    SERVER_ASSISTANT_EXECUTIVES(List.class, "server.assistant_executives"),
    SERVER_EXECUTIVES(List.class, "server.executives"),
    SERVER_MASTER_BUILDER_MANAGEMENT(List.class, "server.master_builder_management"),
    SERVER_BAN_URL(String.class, "server.ban_url"),
    SERVER_PERMBAN_URL(String.class, "server.permban_url"),
    SERVER_TABLIST_HEADER(String.class, "server.tablist_header"),
    SERVER_TABLIST_FOOTER(String.class, "server.tablist_footer"),
    SERVER_BAN_MOTD(String.class, "server.motds.ban"),
    SERVER_ADMINMODE_MOTD(String.class, "server.motds.adminmode"),
    SERVER_LOCKDOWN_MOTD(String.class, "server.motds.lockdown"),
    SERVER_WHITELIST_MOTD(String.class, "server.motds.whitelist"),
    SERVER_FULL_MOTD(String.class, "server.motds.full"),
    //
    DISCORD_TOKEN(String.class, "discord.token"),
    DISCORD_REPORT_CHANNEL_ID(String.class, "discord.report_channel_id"),
    DISCORD_CHAT_CHANNEL_ID(String.class, "discord.chat_channel_id"),
    DISCORD_ROLE_SYNC(Boolean.class, "discord.role_sync"),
    DISCORD_SERVER_ID(String.class, "discord.server_id"),
    DISCORD_MASTER_BUILDER_ROLE_ID(String.class, "discord.master_builder_role_id"),
    DISCORD_SUPER_ROLE_ID(String.class, "discord.super_role_id"),
    DISCORD_TELNET_ROLE_ID(String.class, "discord.telnet_role_id"),
    DISCORD_SENIOR_ROLE_ID(String.class, "discord.senior_role_id"),
    DISCORD_DEVELOPER_ROLE_ID(String.class, "discord.developer_role_id"),
    DISCORD_ASSISTANT_EXECUTIVE_ROLE_ID(String.class, "discord.assistant_executive_role_id"),
    DISCORD_EXECUTIVE_ROLE_ID(String.class, "discord.executive_role_id"),
    DISCORD_SERVER_OWNER_ROLE_ID(String.class, "discord.server_owner_role_id"),
    //
    SHOP_ENABLED(Boolean.class, "shop.enabled"),
    SHOP_PREFIX(String.class, "shop.prefix"),
    //
    ADMINLIST_CLEAN_THESHOLD_HOURS(Integer.class, "adminlist.clean_threshold_hours"),
    ADMINLIST_CONSOLE_IS_SENIOR(Boolean.class, "adminlist.console_is_senior"),
    //
    COREPROTECT_MYSQL_ENABLED(Boolean.class, "coreprotect.enabled"),
    COREPROTECT_MYSQL_HOST(String.class, "coreprotect.host"),
    COREPROTECT_MYSQL_PORT(String.class, "coreprotect.port"),
    COREPROTECT_MYSQL_USERNAME(String.class, "coreprotect.username"),
    COREPROTECT_MYSQL_PASSWORD(String.class, "coreprotect.password"),
    COREPROTECT_MYSQL_DATABASE(String.class, "coreprotect.database"),
    //
    DISABLE_NIGHT(Boolean.class, "disable.night"),
    DISABLE_WEATHER(Boolean.class, "disable.weather"),
    //
    ENABLE_PREPROCESS_LOG(Boolean.class, "preprocess_log"),
    ENABLE_PET_PROTECT(Boolean.class, "petprotect.enabled"),
    //
    LANDMINES_ENABLED(Boolean.class, "landmines_enabled"),
    TOSSMOB_ENABLED(Boolean.class, "tossmob_enabled"),
    AUTOKICK_ENABLED(Boolean.class, "autokick.enabled"),
    MP44_ENABLED(Boolean.class, "mp44_enabled"),
    FOURCHAN_ENABLED(Boolean.class, "4chan_enabled"),
    //
    PROTECTAREA_ENABLED(Boolean.class, "protectarea.enabled"),
    PROTECTAREA_SPAWNPOINTS(Boolean.class, "protectarea.auto_protect_spawnpoints"),
    PROTECTAREA_RADIUS(Double.class, "protectarea.auto_protect_radius"),
    //
    NUKE_MONITOR_ENABLED(Boolean.class, "nukemonitor.enabled"),
    NUKE_MONITOR_COUNT_BREAK(Integer.class, "nukemonitor.count_break"),
    NUKE_MONITOR_COUNT_PLACE(Integer.class, "nukemonitor.count_place"),
    NUKE_MONITOR_RANGE(Double.class, "nukemonitor.range"),
    //
    AUTOKICK_THRESHOLD(Double.class, "autokick.threshold"),
    AUTOKICK_TIME(Integer.class, "autokick.time"),
    //
    LOGS_SECRET(String.class, "logs.secret"),
    LOGS_URL(String.class, "logs.url"),
    //
    FLATLANDS_GENERATE(Boolean.class, "flatlands.generate"),
    FLATLANDS_GENERATE_PARAMS(String.class, "flatlands.generate_params"),
    //
    ANNOUNCER_ENABLED(Boolean.class, "announcer.enabled"),
    ANNOUNCER_INTERVAL(Integer.class, "announcer.interval"),
    ANNOUNCER_PREFIX(String.class, "announcer.prefix"),
    ANNOUNCER_ANNOUNCEMENTS(List.class, "announcer.announcements"),
    //
    EXPLOSIVE_RADIUS(Double.class, "explosive_radius"),
    FREECAM_TRIGGER_COUNT(Integer.class, "freecam_trigger_count"),
    SERVICE_CHECKER_URL(String.class, "service_checker_url"),
    BLOCKED_COMMANDS(List.class, "blocked_commands"),
    HOST_SENDER_NAMES(List.class, "host_sender_names"),
    FAMOUS_PLAYERS(List.class, "famous_players"),
    OVERLORD_IPS(List.class, "overlord_ips"),
    NOADMIN_IPS(List.class, "noadmin_ips"),
    ADMIN_ONLY_MODE(Boolean.class, "admin_only_mode"),
    ADMIN_INFO(List.class, "admininfo"),
    MASTER_BUILDER_INFO(List.class, "masterbuilderinfo"),
    VOTING_INFO(List.class, "votinginfo"),
    AUTO_ENTITY_WIPE(Boolean.class, "auto_wipe"),
    //
    AMP_ENABLED(Boolean.class, "amp.enabled"),
    AMP_USERNAME(String.class, "amp.username"),
    AMP_PASSWORD(String.class, "amp.password"),
    AMP_URL(String.class, "amp.url");
    //
    private final Class<?> type;
    private final String configName;

    ConfigEntry(Class<?> type, String configName)
    {
        this.type = type;
        this.configName = configName;
    }

    public static ConfigEntry findConfigEntry(String name)
    {
        name = name.toLowerCase().replace("_", "");
        for (ConfigEntry entry : values())
        {
            if (entry.toString().toLowerCase().replace("_", "").equals(name))
            {
                return entry;
            }
        }
        return null;
    }

    public Class<?> getType()
    {
        return type;
    }

    public String getConfigName()
    {
        return configName;
    }

    public String getString()
    {
        return getConfig().getString(this);
    }

    public String setString(String value)
    {
        getConfig().setString(this, value);
        return value;
    }

    public Double getDouble()
    {
        return getConfig().getDouble(this);
    }

    public Double setDouble(Double value)
    {
        getConfig().setDouble(this, value);
        return value;
    }

    public Boolean getBoolean()
    {
        return getConfig().getBoolean(this);
    }

    public Boolean setBoolean(Boolean value)
    {
        getConfig().setBoolean(this, value);
        return value;
    }

    public Integer getInteger()
    {
        return getConfig().getInteger(this);
    }

    public Integer setInteger(Integer value)
    {
        getConfig().setInteger(this, value);
        return value;
    }

    public List<?> getList()
    {
        return getConfig().getList(this);
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList()
    {
        return (List<String>)getList();
    }

    private MainConfig getConfig()
    {
        return TotalFreedomMod.plugin().config;
    }
}
