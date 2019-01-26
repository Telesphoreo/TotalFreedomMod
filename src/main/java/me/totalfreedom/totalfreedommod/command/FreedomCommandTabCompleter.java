package me.totalfreedom.totalfreedommod.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class FreedomCommandTabCompleter implements TabCompleter
{
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("saconfig"))
        {
            List<String> tabs = new ArrayList<>();
            tabs.add("list");
            tabs.add("info");
            tabs.add("add");
            tabs.add("remove");
            tabs.add("setrank");
            tabs.add("reload");
            tabs.add("clean");
            return tabs;
        }
        return null;
    }
}