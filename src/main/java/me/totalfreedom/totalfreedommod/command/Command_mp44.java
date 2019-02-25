package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Modern weaponry, FTW. Use 'draw' to start firing, 'sling' to stop firing.", usage = "/<command> <draw | sling>")
public class Command_mp44 extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!ConfigEntry.MP44_ENABLED.getBoolean())
        {
            msg(plugin.i18n.getMessage("mp44Disabled"));
            return true;
        }

        if (args.length == 0)
        {
            return false;
        }

        FPlayer playerdata = plugin.pl.getPlayer(playerSender);

        if (args[0].equalsIgnoreCase("draw"))
        {
            playerdata.armMP44();

            msg(plugin.i18n.getMessage("mp44Armed"));

            playerSender.getEquipment().setItemInMainHand(new ItemStack(Material.GUNPOWDER, 1));
        }
        else
        {
            playerdata.disarmMP44();

            msg(plugin.i18n.getMessage("mp44Disarmed"));
        }

        return true;
    }
}
