package fr.bebedlastreat.astiria.listeners;

import fr.bebedlastreat.astiria.Astiria;
import fr.bebedlastreat.astiria.utils.AstiriaManager;
import fr.bebedlastreat.astiria.utils.Cuboid;
import fr.bebedlastreat.astiria.utils.PlayerInfos;
import fr.bebedlastreat.astiria.utils.ScoreboardSign;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CapitaleListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (AstiriaManager.getFirstBlock() == null || AstiriaManager.getSecondBlock() == null) return;
        Cuboid cuboid = new Cuboid(AstiriaManager.getFirstBlock(), AstiriaManager.getSecondBlock());

        boolean isInCapitale = false;
        for (Block block : cuboid) {
            if (block.getLocation().equals(player.getLocation().getBlock().getLocation())) isInCapitale = true;
        }
        if (Astiria.getInstance().isInCapital.containsKey(player)) {
            if (isInCapitale && !Astiria.getInstance().isInCapital.get(player)) {
                player.sendMessage("§6Vous entrez dans la capitale");
                Astiria.getInstance().isInCapital.remove(player);
                Astiria.getInstance().isInCapital.put(player, isInCapitale);
            }
            if (!isInCapitale && Astiria.getInstance().isInCapital.get(player)) {
                player.sendMessage("§6Vous sortez de la capitale");
                Astiria.getInstance().isInCapital.remove(player);
                Astiria.getInstance().isInCapital.put(player, isInCapitale);
            }
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!(player.hasPermission(Astiria.getInstance().getCommand("capitale").getPermission()))) return;
        if (e.getClickedBlock() == null) return;
        if (e.getItem() == null) return;
        if (!e.getItem().equals(AstiriaManager.tool)) return;
        e.setCancelled(true);
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            AstiriaManager.setFirstBlock(e.getClickedBlock().getLocation());
            player.sendMessage("§aPremière location défini");
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            AstiriaManager.setSecondBlock(e.getClickedBlock().getLocation());
            player.sendMessage("§aSeconde location défini");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (AstiriaManager.getFirstBlock() == null || AstiriaManager.getSecondBlock() == null) return;
        Cuboid cuboid = new Cuboid(AstiriaManager.getFirstBlock(), AstiriaManager.getSecondBlock());

        boolean isInCapitale = false;
        for (Block block : cuboid) {
            if (block.getLocation().equals(player.getLocation().getBlock().getLocation())) isInCapitale = true;
        }
        Astiria.getInstance().isInCapital.put(player, isInCapitale);

        AstiriaManager.setEmerald(player.getUniqueId(), AstiriaManager.getEmerald(player.getUniqueId()));

        PlayerInfos playerInfos = Astiria.getInstance().playerInfos;

        playerInfos.update(player);

        ScoreboardSign scoreboardSign = new ScoreboardSign(player, "§6§lAstiria");
        scoreboardSign.create();
        scoreboardSign.setLine(0, "§r§7§m-------------------");
        scoreboardSign.setLine(1, "§eRank:");
        scoreboardSign.setLine(2, "§6" + player.getName());
        scoreboardSign.setLine(3, "§r");
        scoreboardSign.setLine(4, "§adiamants :");
        scoreboardSign.setLine(5, "§6" + AstiriaManager.getEmerald(player.getUniqueId()));
        scoreboardSign.setLine(6, "§f");
        scoreboardSign.setLine(7, "§eMaison:");
        scoreboardSign.setLine(8, "§c✖");
        scoreboardSign.setLine(9, "§c");
        scoreboardSign.setLine(10, "§eplay.astiria.eu");
        scoreboardSign.setLine(11, "§7§m-------------------");

        (Astiria.getInstance()).boards.put(player, scoreboardSign);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (Astiria.getInstance().isInCapital.containsKey(player)) {
            Astiria.getInstance().isInCapital.remove(player);
        }
    }
}
