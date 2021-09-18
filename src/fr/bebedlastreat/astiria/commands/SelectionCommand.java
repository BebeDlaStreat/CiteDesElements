package fr.bebedlastreat.astiria.commands;

import fr.bebedlastreat.astiria.utils.HouseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande !");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("§c/selection save [name]");
            player.sendMessage("§c-> sauvegarder la sélection");
            player.sendMessage("§c/selection delete [name]");
            player.sendMessage("§c-> supprimer la sélection");
            player.sendMessage("§c/selection give");
            player.sendMessage("§c->donne l'outil permettant d'éditer une sélection");
            player.sendMessage("§c/selection list");
            player.sendMessage("§c->donne la liste des sélections existante");
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("save")) {
            String selectionName = args[1];
            if (HouseManager.getTempFirstPoint(player.getUniqueId()) == null || HouseManager.getTempSecondPoint(player.getUniqueId()) == null) {
                player.sendMessage("§cVous n'avez pas encore définit les 2 points !");
                return false;
            }
            if (HouseManager.isSelectionExist(selectionName)) {
                player.sendMessage("§cUne sélection portant ce nom existe déjà !");
                return false;
            }
            HouseManager.setSelectionLocation(HouseManager.getTempFirstPoint(player.getUniqueId()), HouseManager.getTempSecondPoint(player.getUniqueId()), selectionName);
            player.sendMessage("§aVous venez de créer une sélection sous le nom de §l" + selectionName);
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("delete")) {
            String selectionName = args[1];
            if (!HouseManager.isSelectionExist(selectionName)) {
                player.sendMessage("§cAucun sélection exitant possède ce nom là !");
                return false;
            }
            for (String name : HouseManager.getAllHouses()) {
                if (HouseManager.getHouse(name).contains(selectionName)) {
                    player.sendMessage("§cVous ne pouvez pas supprimer cette sélection car la maison '" + name + "' contient cette sélection");
                    return false;
                }
            }
            HouseManager.deleteSelection(selectionName);
            player.sendMessage("§aVous venez de supprimer la sélection portant le nom §l" + selectionName);
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("give")) {
            player.getInventory().addItem(HouseManager.tool);
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            player.sendMessage("§a" + HouseManager.getAllSelections().toString());
        }
        return false;
    }
}
