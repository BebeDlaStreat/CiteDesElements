package fr.bebedlastreat.astiria.utils;

import fr.bebedlastreat.astiria.Astiria;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ScoreboardRunnable implements Runnable {
    Astiria astiria;

    public ScoreboardRunnable(Astiria astiria) {
        this.astiria = astiria;
    }

    @Override
    public void run() {
        for (Player pls : Bukkit.getOnlinePlayers()) {
            if ((Astiria.getInstance()).boards.containsKey(pls)) {
                Astiria.getInstance().boards.get(pls).setLine(2, "rank");

                Astiria.getInstance().boards.get(pls).setLine(5, "§6" + AstiriaManager.getEmerald(pls.getUniqueId()));

                String house = "§c✖";
                if (HouseManager.hasHouse(pls.getUniqueId())) {
                    if (HouseManager.getHouseLoc(HouseManager.getHouse(pls.getUniqueId())) == null) {
                        house = "§c?";
                    } else {
                        house = ArrowTargetUtils.calculateArrow(pls, HouseManager.getHouseLoc(HouseManager.getHouse(pls.getUniqueId())));
                    }
                    for (String selectionName : HouseManager.getHouse(HouseManager.getHouse(pls.getUniqueId()))) {
                        Cuboid cuboid = new Cuboid(HouseManager.getFirstPoint(selectionName), HouseManager.getSecondPoint(selectionName));
                        for (Block block : cuboid) {
                            if (block.getLocation().equals(pls.getLocation().getBlock().getLocation())) {
                                house = "§a✔";
                            }
                        }
                    }
                }
                ((ScoreboardSign)(Astiria.getInstance()).boards.get(pls)).setLine(8, house);
            }
        }
    }
}
