package fr.bebedlastreat.astiria;

import fr.bebedlastreat.astiria.commands.*;
import fr.bebedlastreat.astiria.listeners.ClickListener;
import fr.bebedlastreat.astiria.listeners.CapitaleListener;
import fr.bebedlastreat.astiria.listeners.HouseListener;
import fr.bebedlastreat.astiria.utils.ArrowTask;
import fr.bebedlastreat.astiria.utils.PlayerInfos;
import fr.bebedlastreat.astiria.utils.ScoreboardRunnable;
import fr.bebedlastreat.astiria.utils.ScoreboardSign;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Astiria extends JavaPlugin {
    private static Astiria instance;
    public HashMap<Player, Boolean> isInCapital;
    public Map<Player, ScoreboardSign> boards = new HashMap<>();
    public PlayerInfos playerInfos;

    @Override
    public void onEnable() {
        instance = this;

        isInCapital = new HashMap<>();
        playerInfos = new PlayerInfos();

        getCommand("banque").setExecutor(new BanqueCommand());
        getCommand("solde").setExecutor(new SoldeCommand());
        getCommand("setmiddle").setExecutor(new MiddleCommand());
        getCommand("capitale").setExecutor(new CapitaleCommand());
        getCommand("diamond").setExecutor(new EmeraldCommand());
        getCommand("selection").setExecutor(new SelectionCommand());
        getCommand("house").setExecutor(new HouseCommand());

        Bukkit.getPluginManager().registerEvents(new ClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new CapitaleListener(), this);
        Bukkit.getPluginManager().registerEvents(new HouseListener(), this);

        Bukkit.getScheduler().runTaskTimer(this, new ArrowTask(), 10L, 10L);
        Bukkit.getScheduler().runTaskTimer(this, new ScoreboardRunnable(this), 20L, 20L);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static Astiria getInstance() {
        return instance;
    }
}
