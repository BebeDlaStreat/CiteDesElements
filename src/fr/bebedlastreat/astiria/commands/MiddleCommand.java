package fr.bebedlastreat.astiria.commands;

import fr.bebedlastreat.astiria.utils.AstiriaManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class MiddleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande !");
            return false;
        }
        Player player = (Player) sender;
        AstiriaManager.setMiddleLocation(player.getLocation());
        player.sendMessage("§aLa capitale a bien été défini");
        return false;
    }
}
