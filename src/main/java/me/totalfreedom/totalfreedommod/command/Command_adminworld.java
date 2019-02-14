package me.totalfreedom.totalfreedommod.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import me.totalfreedom.totalfreedommod.world.WorldTime;
import me.totalfreedom.totalfreedommod.world.WorldWeather;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Go to the AdminWorld.",
        usage = "/<command> [guest <list | purge | add <player> | remove <player>> | time <morning | noon | evening | night> | weather <off | rain | storm>]",
        aliases = "aw")
public class Command_adminworld extends FreedomCommand
{
    private enum CommandMode
    {
        TELEPORT, GUEST, TIME, WEATHER
    }

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        CommandMode commandMode = null;

        if (args.length == 0)
        {
            commandMode = CommandMode.TELEPORT;
        }
        else if (args.length >= 2)
        {
            if ("guest".equalsIgnoreCase(args[0]))
            {
                commandMode = CommandMode.GUEST;
            }
            else if ("time".equalsIgnoreCase(args[0]))
            {
                commandMode = CommandMode.TIME;
            }
            else if ("weather".equalsIgnoreCase(args[0]))
            {
                commandMode = CommandMode.WEATHER;
            }
        }

        if (commandMode == null)
        {
            return false;
        }

        try
        {
            switch (commandMode)
            {
                case TELEPORT:
                {
                    if (!(sender instanceof Player) || playerSender == null)
                    {
                        return true;
                    }

                    World adminWorld = null;
                    try
                    {
                        adminWorld = plugin.wm.adminworld.getWorld();
                    }
                    catch (Exception ex)
                    {
                    }

                    if (adminWorld == null || playerSender.getWorld() == adminWorld)
                    {
                        msg(plugin.i18n.getMessage("goingToMainWorld"));
                        playerSender.teleport(server.getWorlds().get(0).getSpawnLocation());
                    }
                    else
                    {
                        if (plugin.wm.adminworld.canAccessWorld(playerSender))
                        {
                            msg(plugin.i18n.getMessage("goingToAdminWorld"));
                            plugin.wm.adminworld.sendToWorld(playerSender);
                        }
                        else
                        {
                            msg(plugin.i18n.getMessage("noPermissionForAdminWorld"));
                        }
                    }

                    break;
                }
                case GUEST:
                {
                    if (args.length == 2)
                    {
                        if ("list".equalsIgnoreCase(args[1]))
                        {
                            msg(plugin.i18n.getMessage("adminworldGuestList") + plugin.wm.adminworld.guestListToString());
                        }
                        else if ("purge".equalsIgnoreCase(args[1]))
                        {
                            assertCommandPerms(sender, playerSender);
                            plugin.wm.adminworld.purgeGuestList();
                            FUtil.adminAction(sender.getName(), plugin.i18n.getMessage("adminworldGuestListPurged"), false);
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else if (args.length == 3)
                    {
                        assertCommandPerms(sender, playerSender);

                        if ("add".equalsIgnoreCase(args[1]))
                        {
                            final Player player = getPlayer(args[2]);

                            if (player == null)
                            {
                                sender.sendMessage(FreedomCommand.PLAYER_NOT_FOUND);
                                return true;
                            }

                            if (plugin.wm.adminworld.addGuest(player, playerSender))
                            {
                                FUtil.adminAction(sender.getName(), plugin.i18n.getMessage("adminworldGuestAdded") + player.getName(), false);
                            }
                            else
                            {
                                msg(plugin.i18n.getMessage("adminworldGuestCouldNotBeAdded"));
                            }
                        }
                        else if ("remove".equals(args[1]))
                        {
                            final Player player = plugin.wm.adminworld.removeGuest(args[2]);
                            if (player != null)
                            {
                                FUtil.adminAction(sender.getName(), plugin.i18n.getMessage("adminworldGuestRemoved") + player.getName(), false);
                            }
                            else
                            {
                                msg(plugin.i18n.getMessage("adminworldGuestEntryNotFound") + args[2]);
                            }
                        }
                        else
                        {
                            return false;
                        }
                    }

                    break;
                }
                case TIME:
                {
                    assertCommandPerms(sender, playerSender);

                    if (args.length == 2)
                    {
                        WorldTime timeOfDay = WorldTime.getByAlias(args[1]);
                        if (timeOfDay != null)
                        {
                            plugin.wm.adminworld.setTimeOfDay(timeOfDay);
                            msg(plugin.i18n.getMessage("adminworldTimeSet") + timeOfDay.name());
                        }
                        else
                        {
                            msg(plugin.i18n.getMessage("adminworldTimeInvalid"));
                        }
                    }
                    else
                    {
                        return false;
                    }

                    break;
                }
                case WEATHER:
                {
                    assertCommandPerms(sender, playerSender);

                    if (args.length == 2)
                    {
                        WorldWeather weatherMode = WorldWeather.getByAlias(args[1]);
                        if (weatherMode != null)
                        {
                            plugin.wm.adminworld.setWeatherMode(weatherMode);
                            msg(plugin.i18n.getMessage("adminworldWeatherSet") + weatherMode.name());
                        }
                        else
                        {
                            msg(plugin.i18n.getMessage("adminworldWeatherInvalid"));
                        }
                    }
                    else
                    {
                        return false;
                    }

                    break;
                }
                default:
                {
                    return false;
                }
            }
        }
        catch (PermissionDeniedException ex)
        {
            if (ex.getMessage().isEmpty())
            {
                return noPerms();
            }
            sender.sendMessage(ex.getMessage());
            return true;
        }

        return true;
    }

    // TODO: Redo this properly
    private void assertCommandPerms(CommandSender sender, Player playerSender) throws PermissionDeniedException
    {
        if (!(sender instanceof Player) || playerSender == null || !isAdmin(sender))
        {
            throw new PermissionDeniedException();
        }
    }

    private class PermissionDeniedException extends Exception
    {
        private static final long serialVersionUID = 1L;

        private PermissionDeniedException()
        {
            super("");
        }

        private PermissionDeniedException(String string)
        {
            super(string);
        }
    }

    @Override
    public List<String> getTabCompleteOptions(CommandSender sender, Command command, String alias, String[] args)
    {
        if (!plugin.al.isAdmin(sender))
        {
            return Collections.emptyList();
        }
        if (args.length == 1)
        {
            return Arrays.asList("guest", "time", "weather");
        }
        else if (args.length == 2)
        {
            if (args[0].equals("guest"))
            {
                return Arrays.asList("add", "remove", "list", "purge");
            }
            else if (args[0].equals("time"))
            {
                return Arrays.asList("morning", "noon", "evening", "night");
            }
            else if (args[0].equals("weather"))
            {
                return Arrays.asList("off", "rain", "storm");
            }
        }
        else if (args.length == 3)
        {
            if (args[0].equals("guest"))
            {
                if (args[1].equals("add"))
                {
                    return FUtil.getPlayerList();
                }
                else if (args[1].equals("remove"))
                {
                    return plugin.wm.adminworld.getGuestList();
                }
            }
        }
        return Collections.emptyList();
    }
}