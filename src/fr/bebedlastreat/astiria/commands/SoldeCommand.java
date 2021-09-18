package fr.bebedlastreat.astiria.commands;

import fr.bebedlastreat.astiria.utils.AstiriaManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SoldeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande !");
            return false;
        }
        Player player = (Player) sender;
        player.sendMessage("§eVotre solde dans votre banque est de §a" + AstiriaManager.getEmerald(player.getUniqueId()) + " §eémeraude(s)");
        return false;
    }
}
