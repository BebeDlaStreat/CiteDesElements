package fr.bebedlastreat.astiria.listeners;

import fr.bebedlastreat.astiria.Astiria;
import fr.bebedlastreat.astiria.utils.AstiriaManager;
import fr.bebedlastreat.astiria.utils.ItemBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ClickListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getClickedBlock() == null) return;
        if (AstiriaManager.getBanqueLocation() == null) return;
        if (e.getClickedBlock().getLocation().equals(AstiriaManager.getBanqueLocation())) {
            e.setCancelled(true);
            Inventory inv = Bukkit.createInventory(null, InventoryType.BREWING, AstiriaManager.invName);

            inv.setItem(0, new ItemBuilder(Material.DIAMOND).setName("§6diamant(s)").setLore("","§eCliquez pour déposer des diamants").toItemStack());

            inv.setItem(1, new ItemBuilder(Material.BARRIER).setName("§cFERMER").toItemStack());

            inv.setItem(2, new ItemBuilder(Material.DIAMOND_BLOCK).setName("§6Bloc(s) de diamants").setLore("","§eCliquez pour déposer des blocs de diamant").toItemStack());

            inv.setItem(3, new ItemBuilder(Material.ARROW).setName("§6Solde").setLore("","§eVotre solde est de", "§a" + AstiriaManager.getEmerald(player.getUniqueId()) + " §ediamants").toItemStack());


            player.openInventory(inv);
            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.A));
        }
    }

    @EventHandler
    public void onInventoryClick1(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player1 = (Player) e.getWhoClicked();
        if (!e.getInventory().getName().equals(AstiriaManager.invName)) return;
        e.setCancelled(true);
        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().getName().equals(AstiriaManager.invName)) return;
        if (e.getCurrentItem() == null) return;
        ItemStack current = e.getCurrentItem();
        if (current.getType().equals(Material.AIR)) return;

        player1.playNote(player1.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.C));

        if (current.getType().equals(Material.BARRIER)) {
            player1.closeInventory();
        }

        if (current.getType().equals(Material.DIAMOND)) {
            new AnvilGUI.Builder()
                    .onComplete((player, text) -> {                             //called when the inventory output slot is clicked
                        player.setLevel(player.getLevel());
                        try {
                            int number = Integer.parseInt(text);
                            if (number <= 0) {
                                player.sendMessage("§cLe nombre entré doit être supérieur à 0 !");
                                player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G));
                                return AnvilGUI.Response.close();
                            }
                            depositEmerald(player, number);
                            return AnvilGUI.Response.close();
                        } catch (Exception ex) {
                            player.sendMessage("§cNombre invalide !");
                            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G));
                            return AnvilGUI.Response.close();
                        }
                    })
                    .text("Combien")                       //sets the text the GUI should start with
                    .itemLeft(new ItemStack(Material.DIAMOND))               //use a custom item for the first slot
                    .onLeftInputClick(player -> player.sendMessage("§eCliquez sur le slot tout à droite pour confirmer"))    //called when the left input slot is clicked
                    .plugin(Astiria.getInstance())                                   //set the plugin instance
                    .open(player1);                                            //opens the GUI for the player provided

        }


        if (current.getType().equals(Material.DIAMOND_BLOCK)) {
            new AnvilGUI.Builder()
                    .onComplete((player, text) -> {                             //called when the inventory output slot is clicked
                        player.setLevel(player.getLevel());
                        try {
                            int number = Integer.parseInt(text);
                            if (number <= 0) {
                                player.sendMessage("§cLe nombre entré doit être supérieur à 0 !");
                                player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G));
                                return AnvilGUI.Response.close();
                            }
                            depositBlock(player, number);
                            return AnvilGUI.Response.close();
                        } catch (Exception ex) {
                            player.sendMessage("§cNombre invalide !");
                            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G));
                            return AnvilGUI.Response.close();
                        }
                    })
                    .text("Combien")                       //sets the text the GUI should start with
                    .itemLeft(new ItemStack(Material.DIAMOND_BLOCK))               //use a custom item for the first slot
                    .onLeftInputClick(player -> player.sendMessage("§eCliquez sur le slot tout à droite pour confirmer"))    //called when the left input slot is clicked
                    .plugin(Astiria.getInstance())                                   //set the plugin instance
                    .open(player1);                                            //opens the GUI for the player provided
        }
    }

    private static void depositEmerald(Player player, int number) {
        int emerald = 0;
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null) continue;
            if(player.getInventory().getItem(i).getType().equals(Material.DIAMOND)) {
                emerald += player.getInventory().getItem(i).getAmount();
            }
        }
        if (emerald < number) {
            player.sendMessage("§cVous n'avez pas assez de diamant(s) ! Il vous en manque " + (number - emerald));
            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G));
            return;
        }
        AstiriaManager.addEmerald(player.getUniqueId(), number);
        int rest = number;
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
        player.sendMessage("§eVous venez de déposer §a" + number + " §ediamant(s) à la banque !");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3f, 0.5f);
    }

    private static void depositBlock(Player player, int number) {
        int block = 0;
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null) continue;
            if(player.getInventory().getItem(i).getType().equals(Material.DIAMOND_BLOCK)) {
                block += player.getInventory().getItem(i).getAmount();
            }
        }
        if (block < number) {
            player.sendMessage("§cVous n'avez pas assez de bloc(s) ! Il vous en manque " + (number - block));
            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G));
            return;
        }
        AstiriaManager.addEmerald(player.getUniqueId(), number * 9);
        int rest = number;
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null) continue;
            ItemStack current = player.getInventory().getItem(i);
            if(current.getType().equals(Material.DIAMOND_BLOCK)) {
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
        player.sendMessage("§eVous venez de déposer §a" + number + " §ebloc(s) de diamant à la banque !");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3f, 0.5f);
    }
}
