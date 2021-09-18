package fr.bebedlastreat.astiria.utils;

import fr.bebedlastreat.astiria.Astiria;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HouseManager {
    public static ItemStack tool = new ItemBuilder(Material.GOLD_SWORD).setName("§6Épée des maisons").setLore("", "§eClique gauche pour définir le premier point", "§eClique droit pour définir le deuxième point").toItemStack();

    private static File file = new File(Astiria.getInstance().getDataFolder(), "house.yml");
    private static YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

    public static void setTempFirstPoint(Location loc, UUID uuid) {
        configuration.set("temp." + uuid + ".1", loc);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setTempSecondPoint(Location loc, UUID uuid) {
        configuration.set("temp." + uuid + ".2", loc);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Location getTempFirstPoint(UUID uuid) {
        if (configuration.get("temp." + uuid + ".1") == null) return null;
        return (Location) configuration.get("temp." + uuid + ".1");
    }
    public static Location getTempSecondPoint(UUID uuid) {
        if (configuration.get("temp." + uuid + ".2") == null) return null;
        return (Location) configuration.get("temp." + uuid + ".2");
    }

    public static void setSelectionLocation(Location point1, Location point2, String name) {
        configuration.set("selection." + name + ".1", point1);
        configuration.set("selection." + name + ".2", point2);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteSelection(String name) {
        configuration.set("selection." + name, null);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getFirstPoint(String name) {
        if (configuration.get("selection." + name + ".1") == null) return null;
        return (Location) configuration.get("selection." + name + ".1");
    }
    public static Location getSecondPoint(String name) {
        if (configuration.get("selection." + name + ".2") == null) return null;
        return (Location) configuration.get("selection." + name + ".2");
    }

    public static List<String> getAllSelections() {
        List<String> names = new ArrayList<>();
        try {
            for (String name : configuration.getConfigurationSection("selection").getKeys(false)) {
                names.add(name);
            }
        } catch (Exception e) {

        }

        return names;
    }
    public static List<String> getAllHouses() {
        List<String> names = new ArrayList<>();
        try {
            for (String name : configuration.getConfigurationSection("house").getKeys(false)) {
                names.add(name);
            }
        } catch (Exception e) {

        }

        return names;
    }

    public static void setHouse(String name, List<String> selections) {
        configuration.set("house." + name + ".selections", selections);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteHouse(String name) {
        configuration.set("house." + name, null);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<String> getHouse(String name) {
        if (configuration.get("house." + name + ".selections") == null) return null;
        return (List<String>) configuration.get("house." + name + ".selections");
    }

    public static void setHouseLoc(String name, Location loc) {
        configuration.set("house." + name + ".location", loc);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Location getHouseLoc(String name) {
        if (configuration.get("house." + name + ".location") == null) return null;
        return(Location) configuration.get("house." + name + ".location");
    }

    public static void setHouseOwner(String name, UUID uuid) {
        configuration.set("house." + name + ".owner", uuid.toString());
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static UUID getHouseOwner(String name) {
        if (configuration.get("house." + name + ".owner") == null) return null;
        return UUID.fromString(configuration.getString("house." + name + ".owner"));
    }

    public static boolean hasHouse(UUID uuid) {
        for (String name : getAllHouses()) {
            if (getHouseOwner(name) != null && getHouseOwner(name).equals(uuid)) return true;
        }
        return false;
    }
    public static String getHouse(UUID uuid) {
        for (String name : getAllHouses()) {
            if (getHouseOwner(name) != null && getHouseOwner(name).equals(uuid)) return name;
        }
        return null;
    }

    public static boolean houseHasOwner(String name) {
        return (configuration.get("house." + name + ".owner") != null);
    }

    public static boolean isSelectionExist(String name) {
        return (configuration.get("selection." + name) != null);
    }

    public static boolean isHouseExist(String name) {
        return (configuration.get("house." + name) != null);
    }
}
