package me.totalfreedom.totalfreedommod;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class ServerInterface extends FreedomService
{
    public final static String nmsVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    public ServerInterface(TotalFreedomMod plugin)
    {
        super(plugin);
    }

    public static void warnVersion()
    {
        final String nms = FUtil.getNMSVersion();

        if (!nmsVersion.equals(nms))
        {
            FLog.warning(TotalFreedomMod.pluginName + " is compiled for " + nmsVersion + " but the server is running version " + nms + "!");
            FLog.warning("This might result in unexpected behaviour!");
        }
    }

    public static void warnJavaVersion()
    {
        double javaVersion = Double.parseDouble(System.getProperty("java.specification.version"));
        if (javaVersion < 11.0)
        {
            FLog.warning("Java 8 is EOL. Eventually, there will be no more support or updates for Java 8.");
            FLog.warning("Please consider upgrading to Java 11 or later.");
        }
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    // Need to do this. Bukkit's methods don't save, so online mode will always default to true.
    public boolean setOnlineMode(boolean mode)
    {
        if (Bukkit.getOnlineMode() == mode)
        {
            return true;
        }

        String classNmsVersion = "net.minecraft.server." + nmsVersion;

        try
        {
            Class<?> c = Class.forName(classNmsVersion + ".MinecraftServer");
            Object obj = c.getMethod("getServer").invoke(null);
            obj.getClass().getMethod("setOnlineMode", boolean.class).invoke(obj, mode);
            Object server = c.getDeclaredField("server").get(obj);
            Field f = server.getClass().getDeclaredField("online");
            f.setAccessible(true);
            Object wrapper = f.get(server);
            Field sf = wrapper.getClass().getDeclaredField("value");
            sf.setAccessible(true);
            sf.set(wrapper, mode);
            return true;
        }
        catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException | InvocationTargetException | NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public int purgeWhitelist()
    {
        int size = 0;
        for (OfflinePlayer player : Bukkit.getWhitelistedPlayers())
        {
            Bukkit.getWhitelistedPlayers().remove(player);
            size++;
        }

        try
        {
           Bukkit.reloadWhitelist();
        }
        catch (Exception ex)
        {
            FLog.warning("Could not purge the whitelist!");
            FLog.warning(ex);
        }
        return size;
    }

    public boolean isWhitelisted()
    {
        return Bukkit.hasWhitelist();
    }

    public List<?> getWhitelisted()
    {
        return (List<?>)Bukkit.getWhitelistedPlayers();
    }

    public String getVersion()
    {
        return Bukkit.getVersion();
    }
}
