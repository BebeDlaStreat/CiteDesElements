package fr.bebedlastreat.astiria.utils;

import fr.bebedlastreat.astiria.Astiria;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerInfos {

    private File file = new File(Astiria.getInstance().getDataFolder(), "players.yml");
    private YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

    /**
     * Actualiser/créer les informations du joueur
     * @param player
     */
    public void update(Player player){
        if(configuration.getString(player.getUniqueId().toString()) == null) {
            configuration.set(player.getUniqueId().toString(), player.getName());
        } else if (!(configuration.getString(player.getUniqueId().toString()).equalsIgnoreCase(player.getName()))) {
            configuration.set(player.getUniqueId().toString(), player.getName());
        }

        try {
            configuration.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public boolean exist(String playerName){
        for (String uuid : configuration.getConfigurationSection("").getKeys(false))
            if (configuration.getString(uuid).equalsIgnoreCase(playerName)) {
                return true;
            }
        return false;
    }
    public UUID getUUID(String playerName){
        try {
            return Bukkit.getOfflinePlayer(playerName).getUniqueId();
        } catch (Exception e) {
            System.out.println("§cJoueur inexistant !");
        }
        return null;
    }

}