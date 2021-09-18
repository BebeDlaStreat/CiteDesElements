package fr.bebedlastreat.astiria.commands;

import fr.bebedlastreat.astiria.utils.HouseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HouseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande !");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("§c/house delete [name]");
            player.sendMessage("§c/house tp [name]");
            player.sendMessage("§c/house list");
            return false;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("delete")) {
            String name = args[1];
            if (!HouseManager.isHouseExist(name)) {
                player.sendMessage("§cCette maison n'existe pas !");
                return false;
            }
            if (HouseManager.getHouseLoc(name) != null) {
                if (HouseManager.getHouseLoc(name).getBlock().getState() instanceof Sign) {
                    Sign sign = (Sign) HouseManager.getHouseLoc(name).getBlock().getState();
                    try {
                        int price = Integer.valueOf(sign.getLine(2));
                        if (HouseManager.getHouseOwner(name) != null && price > 0) {
                            if (Bukkit.getPlayer(HouseManager.getHouseOwner(name)) != null) {
                                price = Math.round(Float.valueOf(price) / 2);
                                Bukkit.getPlayer(HouseManager.getHouseOwner(name)).getInventory().addItem(new ItemStack(Material.DIAMOND, price));
                                Bukkit.getPlayer(HouseManager.getHouseOwner(name)).sendMessage("§aVotre maison a été supprimé, en compensation vous recevez la moitier de son prix en émeraude");
                            }
                        }
                    } catch (Exception e) {

                    }
                }
                HouseManager.getHouseLoc(name).getBlock().setType(Material.AIR);
            }
            HouseManager.deleteHouse(name);
            player.sendMessage("§cLa maison a bien été supprimé !");
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("tp")) {
            String name = args[1];
            if (!HouseManager.isHouseExist(name)) {
                player.sendMessage("§cCette maison n'existe pas !");
                return false;
            }
            if (HouseManager.getHouseLoc(name) != null) {
                player.teleport(HouseManager.getHouseLoc(name));
                player.sendMessage("§aVous avez bien été téléporter au panneau de la maison");
            } else {
                player.sendMessage("§cERREUR: impossible de trouver la location de la maison");
            }
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            player.sendMessage("§a" + HouseManager.getAllHouses().toString());
        }

        return false;
    }
}
