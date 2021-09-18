package fr.bebedlastreat.astiria.utils;

import fr.bebedlastreat.astiria.Astiria;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArrowTask extends BukkitRunnable implements Runnable {
    @Override
    public void run() {
        if (AstiriaManager.getMiddleLocation() != null) {
            for (Player pls : Bukkit.getOnlinePlayers()) {
                if (!Astiria.getInstance().isInCapital.containsKey(pls)) continue;
                if (Astiria.getInstance().isInCapital.get(pls)) {
                    ActionBarUtil.sendActionBarMessage(pls, "§eVous êtes dans la capitale");
                } else {
                    ActionBarUtil.sendActionBarMessage(pls, "§eCapitale : §a" + ArrowTargetUtils.calculateArrow(pls, AstiriaManager.getMiddleLocation()), 1, Astiria.getInstance());
                }
            }
        }
    }
}
