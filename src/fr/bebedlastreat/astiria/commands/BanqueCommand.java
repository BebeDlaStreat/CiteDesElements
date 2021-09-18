package fr.bebedlastreat.astiria.commands;

import fr.bebedlastreat.astiria.utils.AstiriaManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class BanqueCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande !");
            return false;
        }
        Player player = (Player) sender;
        if (player.getTargetBlock((Set<Material>) null, 5) == null) {
            player.sendMessage("§cVous ne regardez aucun block !");
            return false;
        }
        if (player.getTargetBlock((Set<Material>) null, 5).getType().equals(Material.AIR)) {
            player.sendMessage("§cVous ne regardez aucun block !");
            return false;
        }
        AstiriaManager.setBanqueLocation(player.getTargetBlock((Set<Material>) null, 5).getLocation());
        player.sendMessage("§aLa banque a bien été défini");
        return false;
    }
}
