package fr.bebedlastreat.astiria.listeners;

import fr.bebedlastreat.astiria.Astiria;
import fr.bebedlastreat.astiria.utils.*;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class HouseListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!(player.hasPermission(Astiria.getInstance().getCommand("selection").getPermission()))) return;
        if (e.getClickedBlock() == null) return;
        if (e.getItem() == null) return;
        if (!e.getItem().equals(HouseManager.tool)) return;
        e.setCancelled(true);
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            HouseManager.setTempFirstPoint(e.getClickedBlock().getLocation(), player.getUniqueId());
            player.sendMessage("§aPremière location défini");
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            HouseManager.setTempSecondPoint(e.getClickedBlock().getLocation(), player.getUniqueId());
            player.sendMessage("§aSeconde location défini");
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent e) {
        Player player = e.getPlayer();
        if (!(player.hasPermission(Astiria.getInstance().getCommand("selection").getPermission()))) return;
        if (e.getLine(0) == null || e.getLine(1) == null || e.getLine(2) == null || e.getLine(3) == null) return;
        e.setLine(0, e.getLine(0).replace("\uF701", ""));
        e.setLine(1, e.getLine(1).replace("\uF701", ""));
        e.setLine(2, e.getLine(2).replace("\uF701", ""));
        e.setLine(3, e.getLine(3).replace("\uF701", ""));

        if (!e.getLine(0).equalsIgnoreCase("HouseSelling")) return;
        if (HouseManager.isHouseExist(e.getLine(3))) {
            player.sendMessage("§cCe nom de maison existe déjà !");
            return;
        }
        int number = 0;
//        number = Integer.valueOf(e.getLine(2).replace("\uF701", ""));
        try {
            number = Integer.valueOf(e.getLine(2));

        } catch (Exception ex) {
            player.sendMessage("§4La troisième ligne n'est pas un nombre !");
            return;
        }
        if (number <= 0) {
            player.sendMessage("§4Le nombre entré doit être supérieur à 0 !");
            return;
        }
        if (e.getLine(1).startsWith("[") && e.getLine(1).endsWith("]")) {
            if (!(e.getLine(1).length() >= 3)) {
                player.sendMessage("§cErreur de syntaxe à la ligne 2:\nVous devez mettre du texte entre '[' et ']' ");
                return;
            }
            String names = e.getLine(1).substring(1, e.getLine(1).length() - 1);
            try {
                String[] selections = names.split(",");
                for (String selection : selections) {
                    if (!HouseManager.isSelectionExist(selection)) {
                        player.sendMessage("§cLa sélection §l" + selection + " §r§cn'éxiste pas");
                        return;
                    }
                }
                HouseManager.setHouse(e.getLine(3), Arrays.asList(selections));
                HouseManager.setHouseLoc(e.getLine(3), e.getBlock().getLocation());
                player.sendMessage("§aLa maison a bien été créé !");
                e.setLine(0, "§cMaison En Vente");
                e.setLine(1, "§a" + number + " diamants");
                e.setLine(2,  e.getLine(3));
                e.setLine(3, "§a[Acheter]");
            } catch (Exception ex) {
                player.sendMessage("§cErreur de syntaxe à la ligne 2:\nVous devez séparer les nom de sélections entre crochet par une virgule ou si vous ne souhaitez mettre qu'une sélection, ne mettez pas de crochet");
                return;
            }
        } else {
            if (HouseManager.isSelectionExist(e.getLine(1))) {
                HouseManager.setHouse(e.getLine(3), Arrays.asList(e.getLine(1)));
                HouseManager.setHouseLoc(e.getLine(3), e.getBlock().getLocation());
                player.sendMessage("§aLa maison a bien été créé !");
                e.setLine(0, "§cMaison En Vente");
                e.setLine(1, "§a" + number + " diamants");
                e.setLine(2,  e.getLine(3));
                e.setLine(3, "§a[Acheter]");
            } else {
                player.sendMessage("§cCette sélection n'existe pas !");
                return;
            }
        }
    }

    @EventHandler
    public void clickOnSign(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getClickedBlock() == null) return;
        BlockState state = e.getClickedBlock().getState();
        if (!(state instanceof Sign)) return;
        Sign sign = (Sign) state;
        if (!sign.getLine(0).equals("§cMaison En Vente")) return;
        int price = 0;
        String priceLine = sign.getLine(1);
        if (priceLine.length() <= 12) {
            player.sendMessage("§cErreur de format à la deuxième ligne (le prix n'est pas valide)");
            return;
        }
        try {
            price = Integer.valueOf(priceLine.substring(2, priceLine.length() - 10));
        } catch (Exception ex) {
            player.sendMessage("§cUne erreur s'est produite lors de l'obtention du prix :\nImpossible de convertir " + priceLine.substring(2, priceLine.length() - 10) + " en nombre");
            return;
        }
        if (sign.getLine(2).length() <= 2) {
            player.sendMessage("§cErreur de format à la troisième ligne (le nom n'est pas valide)");
            return;
        }
        if (!HouseManager.isHouseExist(sign.getLine(2))) {
            player.sendMessage("§cCette maison n'existe pas");
            return;
        }
        if (HouseManager.getHouseOwner(sign.getLine(2)) != null) {
            player.sendMessage("§cUn joueur a déjà acheté cette maison");
            return;
        }
        if (HouseManager.hasHouse(player.getUniqueId())) {
            player.sendMessage("§cVous posséder déjà une maison !");
        }
        int emerald = 0;
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null) continue;
            if(player.getInventory().getItem(i).getType().equals(Material.DIAMOND)) {
                emerald += player.getInventory().getItem(i).getAmount();
            }
        }
        if (emerald < price) {
            player.sendMessage("§cVous n'avez pas assez de diamant(s) ! Il vous en manque " + (price - emerald));
            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G));
            return;
        }
        HouseManager.setHouseOwner(sign.getLine(2), player.getUniqueId());
        int rest = price;
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null) continue;
            ItemStack current = player.getInventory().getItem(i);
            if(current.getType().equals(Material.DIAMOND)) {
                if (current.getAmount() <= rest) {
                    rest -= current.getAmount();
                    player.getInventory().clear(i);
                } else {
                    current.setAmount(current.getAmount() - rest);
                    rest = 0;
                }

                if (rest <= 0) break;
            }
        }
        player.updateInventory();
        player.sendMessage("§aVous venez d'acheter une maison !");

        sign.setLine(0, "§aMaison possédée par");
        sign.setLine(1, "§e" + player.getName());
        sign.setLine(2, String.valueOf(price));
        sign.setLine(3, "");
        sign.update();
    }

    @EventHandler
    public void onSignBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        BlockState state = e.getBlock().getState();
        if (!(state instanceof Sign)) return;
        Sign sign = (Sign) state;
        for (String name : HouseManager.getAllHouses()) {
            if (HouseManager.getHouseLoc(name) != null && HouseManager.getHouseLoc(name).equals(sign.getLocation())) {
                e.setCancelled(true);
                if (!(player.hasPermission(Astiria.getInstance().getCommand("selection").getPermission()))) {
                    player.sendMessage("§cVous ne pouvez pas casser ce panneau !");
                } else {
                    player.sendMessage("§cPour supprimer une maison faîtes /house delete [name]");
                }
            }
        }
    }

    @EventHandler
    public void onHouseBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Location loc = e.getBlock().getLocation();
        if (player.hasPermission(Astiria.getInstance().getCommand("selection").getPermission())) return;
        for (String houseName : HouseManager.getAllHouses()) {
            if (HouseManager.getHouseOwner(houseName) != null && HouseManager.getHouseOwner(houseName).equals(player.getUniqueId())) continue;
            for (String selectionName : HouseManager.getHouse(houseName)) {
                Cuboid cuboid = new Cuboid(HouseManager.getFirstPoint(selectionName), HouseManager.getSecondPoint(selectionName));
                for (Block block : cuboid) {
                    if (block.getLocation().equals(loc)) {
                        e.setCancelled(true);
                        player.sendMessage("§cVous ne pouvez pas casser ce bloc car cette maison ne vous appartient pas !");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHousePlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Location loc = e.getBlock().getLocation();
        if (player.hasPermission(Astiria.getInstance().getCommand("selection").getPermission())) return;
        for (String houseName : HouseManager.getAllHouses()) {
            if (HouseManager.getHouseOwner(houseName) != null && HouseManager.getHouseOwner(houseName).equals(player.getUniqueId())) continue;
            for (String selectionName : HouseManager.getHouse(houseName)) {
                Cuboid cuboid = new Cuboid(HouseManager.getFirstPoint(selectionName), HouseManager.getSecondPoint(selectionName));
                for (Block block : cuboid) {
                    if (block.getLocation().equals(loc)) {
                        e.setCancelled(true);
                        player.sendMessage("§cVous ne pouvez pas casser ce bloc car cette maison ne vous appartient pas !");
                    }
                }
            }
        }
    }
}
