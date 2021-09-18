package fr.bebedlastreat.astiria.utils;

import fr.bebedlastreat.astiria.Astiria;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AstiriaManager {
    public static String invName = "§aBanque";
    public static ItemStack tool = new ItemBuilder(Material.GOLD_SPADE).setName("§6Pelle de la capitale").setLore("", "§eClique gauche pour définir le premier point", "§eClique droit pour définir le deuxième point").toItemStack();

    private static File file = new File(Astiria.getInstance().getDataFolder(), "astiria.yml");
    private static YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

    public static void setBanqueLocation(Location loc) {
        configuration.set("location.banque", loc);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getBanqueLocation() {
        if (configuration.get("location.banque") == null) return null;
        return (Location) configuration.get("location.banque");
    }

    public static void setMiddleLocation(Location loc) {
        configuration.set("location.middle", loc);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getMiddleLocation() {
        if (configuration.get("location.middle") == null) return null;
        return (Location) configuration.get("location.middle");
    }

    public static List<UUID> getAllUuids() {
        List<UUID> uuids = new ArrayList<>();
        for (String string : configuration.getConfigurationSection("account.emerald").getKeys(false)) {
            uuids.add(UUID.fromString(string));
        }
        return uuids;
    }

    public static int getEmerald(UUID uuid) {
        if (configuration.get("account.emerald." + uuid) == null) return 0;
        return configuration.getInt("account.emerald." + uuid);
    }
    public static void setEmerald(UUID uuid, int number) {
        configuration.set("account.emerald." + uuid, number);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void addEmerald(UUID uuid, int number) {
        setEmerald(uuid, getEmerald(uuid) + number);
    }
    public static void removeEmerald(UUID uuid, int number) {
        setEmerald(uuid, getEmerald(uuid) - number);
    }

    public static Location getFirstBlock() {
        if (configuration.get("location.block.1") == null) return null;
        return (Location) configuration.get("location.block.1");
    }
    public static void setFirstBlock(Location loc) {
        configuration.set("location.block.1", loc);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getSecondBlock() {
        if (configuration.get("location.block.2") == null) return null;
        return (Location) configuration.get("location.block.2");
    }
    public static void setSecondBlock(Location loc) {
        configuration.set("location.block.2", loc);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
