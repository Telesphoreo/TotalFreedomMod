package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Enchant items.", usage = "/<command> <list | addall | reset | add <name> | remove <name>>")
public class Command_enchant extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }

        ItemStack item = playerSender.getEquipment().getItemInMainHand();

        if (item == null || item.getType() == Material.AIR)
        {
            msg(plugin.i18n.getMessage("holdItemToEnchant"));
            return true;
        }

        if (args[0].equalsIgnoreCase("list"))
        {
            boolean has_enchantments = false;

            StringBuilder possible_ench = new StringBuilder(plugin.i18n.getMessage("possibleEnchantments"));
            for (Enchantment ench : Enchantment.values())
            {
                if (ench.canEnchantItem(item))
                {
                    has_enchantments = true;
                    possible_ench.append(ench.getName()).append(", ");
                }
            }

            if (has_enchantments)
            {
                msg(possible_ench.toString());
            }
            else
            {
                msg(plugin.i18n.getMessage("noEnchantments"));
            }
        }
        else if (args[0].equalsIgnoreCase("addall"))
        {
            for (Enchantment ench : Enchantment.values())
            {
                try
                {
                    if (ench.canEnchantItem(item))
                    {
                        item.addEnchantment(ench, ench.getMaxLevel());
                    }
                }
                catch (Exception ex)
                {
                    msg(plugin.i18n.getMessage("couldNotAddEnchantment") + ench.getName());
                }
            }

            msg(plugin.i18n.getMessage("addedAllEnchantments"));
        }
        else if (args[0].equalsIgnoreCase("reset"))
        {
            for (Enchantment ench : item.getEnchantments().keySet())
            {
                item.removeEnchantment(ench);
            }

            msg(plugin.i18n.getMessage("removedAllEnchantments"));
        }
        else
        {
            if (args.length < 2)
            {
                return false;
            }

            Enchantment ench = null;

            try
            {
                ench = Enchantment.getByName(args[1].toUpperCase());
            }
            catch (Exception ex)
            {
            }

            if (ench == null)
            {
                msg(plugin.i18n.getMessage("invalidEnchantment", args[1]));
                return true;
            }

            if (args[0].equalsIgnoreCase("add"))
            {
                if (ench.canEnchantItem(item))
                {
                    item.addEnchantment(ench, ench.getMaxLevel());

                    msg(plugin.i18n.getMessage("enchantmentAdded") + ench.getName());
                }
                else
                {
                    msg(plugin.i18n.getMessage("couldNotUseEnchantment"));
                }
            }
            else if (args[0].equals("remove"))
            {
                item.removeEnchantment(ench);

                msg(plugin.i18n.getMessage("enchantmentRemoved") + ench.getName());
            }
        }
        return true;
    }
}
