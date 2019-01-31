package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Lists all possible attributes.", usage = "/<command>")
public class Command_attributelist extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        String list = "All possible attributes: ";

        for (Attribute attribute : Attribute.values())
        {
            list += attribute.name() + ", ";
        }

        // Remove extra comma at the end of the list
        list = list.substring(0, list.length() - 2);

        msg(list);
        return true;
    }
}
