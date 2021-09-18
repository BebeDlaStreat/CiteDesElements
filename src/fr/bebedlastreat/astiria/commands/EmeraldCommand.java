package fr.bebedlastreat.astiria.commands;

import fr.bebedlastreat.astiria.Astiria;
import fr.bebedlastreat.astiria.utils.AstiriaManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class EmeraldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 3) {
            sender.sendMessage("§c/diamond add/set/remove {player} {number}");
            sender.sendMessage("§c@a Pour tous les joueurs connectés");
            sender.sendMessage("§call pour tous les joueurs s'étant déjà connecté");
            return false;
        }
        String targetName = args[1];
        if (args[1].equalsIgnoreCase("@a")) {
            for (Player pls : Bukkit.getOnlinePlayers()) {
                UUID targetUUID = pls.getUniqueId();

                int number = 0;
                try {
                    number = Integer.parseInt(args[2]);

                } catch (NumberFormatException e) {
                    sender.sendMessage("§4Le troisième argument n'est pas un nombre !");
                    return false;
                }
                if (args[0].equals("add")) {
                    AstiriaManager.addEmerald(targetUUID, number);
                    sender.sendMessage("§cVous venez d'ajouter " + number + " émeraudes à tous les joueurs connectés");
                }
                if (args[0].equals("remove")) {
                    AstiriaManager.removeEmerald(targetUUID, number);
                    sender.sendMessage("§cVous venez de retirer" + number + " émeraudes à tous les joueurs connectés");
                }
                if (args[0].equals("set")) {
                    AstiriaManager.setEmerald(targetUUID, number);
                    sender.sendMessage("§cVous venez de définir à " + number + " le nombre d'émeraudes de tous les joueurs connectés");
                }
            }
        } else if (args[1].equalsIgnoreCase("all")) {
            for (UUID targetUUID : AstiriaManager.getAllUuids()) {
                int number = 0;
                try {
                    number = Integer.parseInt(args[2]);

                } catch (NumberFormatException e) {
                    sender.sendMessage("§4Le troisième argument n'est pas un nombre !");
                    return false;
                }
                if (args[0].equals("add")) {
                    AstiriaManager.addEmerald(targetUUID, number);
                    sender.sendMessage("§cVous venez d'ajouter " + number + " émeraudes à tous les joueurs s'étant déjà connectés");
                }
                if (args[0].equals("remove")) {
                    AstiriaManager.removeEmerald(targetUUID, number);
                    sender.sendMessage("§cVous venez de retirer " + number + " émeraudes à tous les joueurs s'étant déjà connectés");
                }
                if (args[0].equals("set")) {
                    AstiriaManager.setEmerald(targetUUID, number);
                    sender.sendMessage("§cVous venez de définir à " + number + " le nombre d'émeraudes de tous les joueurs s'étant déjà connectés");
                }
            }
        } else {
            if (!Astiria.getInstance().playerInfos.exist(targetName)) {
                sender.sendMessage("§4Ce joueur n'existe pas !");
                return false;
            }
            UUID targetUUID = Astiria.getInstance().playerInfos.getUUID(targetName);

            int number = 0;
            try {
                number = Integer.parseInt(args[2]);

            } catch (NumberFormatException e) {
                sender.sendMessage("§4Le troisième argument n'est pas un nombre !");
                return false;
            }
            if (args[0].equals("add")) {
                AstiriaManager.addEmerald(targetUUID, number);
                sender.sendMessage("§cVous venez d'ajouter " + number + " émeraudes à " + targetName);
            }
            if (args[0].equals("remove")) {
                AstiriaManager.removeEmerald(targetUUID, number);
                sender.sendMessage("§cVous venez de retirer " + number + " émeraudes à " + targetName);
            }
            if (args[0].equals("set")) {
                AstiriaManager.setEmerald(targetUUID, number);
                sender.sendMessage("§cVous venez de définir à " + number + " le nombre d'émeraudes de " + targetName);
            }
        }
        return false;
    }
}
