package fr.bebedlastreat.astiria.utils;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardSign {
    private boolean created = false;

    private final VirtualTeam[] lines = new VirtualTeam[15];

    private final Player player;

    private String objectiveName;

    public ScoreboardSign(Player player, String objectiveName) {
        this.player = player;
        this.objectiveName = objectiveName;
    }

    public void create() {
        if (this.created)
            return;
        PlayerConnection player = getPlayer();
        player.sendPacket((Packet)createObjectivePacket(0, this.objectiveName));
        player.sendPacket((Packet)setObjectiveSlot());
        int i = 0;
        while (i < this.lines.length)
            sendLine(i++);
        this.created = true;
    }

    public void destroy() {
        if (!this.created)
            return;
        getPlayer().sendPacket((Packet)createObjectivePacket(1, null));
        byte b;
        int i;
        VirtualTeam[] arrayOfVirtualTeam;
        for (i = (arrayOfVirtualTeam = this.lines).length, b = 0; b < i; ) {
            VirtualTeam team = arrayOfVirtualTeam[b];
            if (team != null)
                getPlayer().sendPacket((Packet)team.removeTeam());
            b++;
        }
        this.created = false;
    }

    public void setObjectiveName(String name) {
        this.objectiveName = name;
        if (this.created)
            getPlayer().sendPacket((Packet)createObjectivePacket(2, name));
    }

    public void setLine(int line, String value) {
        VirtualTeam team = getOrCreateTeam(line);
        String old = team.getCurrentPlayer();
        if (old != null && this.created)
            getPlayer().sendPacket((Packet)removeLine(old));
        team.setValue(value);
        sendLine(line);
    }

    public void removeLine(int line) {
        VirtualTeam team = getOrCreateTeam(line);
        String old = team.getCurrentPlayer();
        if (old != null && this.created) {
            getPlayer().sendPacket((Packet)removeLine(old));
            getPlayer().sendPacket((Packet)team.removeTeam());
        }
        this.lines[line] = null;
    }

    public String getLine(int line) {
        if (line > 14)
            return null;
        if (line < 0)
            return null;
        return getOrCreateTeam(line).getValue();
    }

    public VirtualTeam getTeam(int line) {
        if (line > 14)
            return null;
        if (line < 0)
            return null;
        return getOrCreateTeam(line);
    }

    private PlayerConnection getPlayer() {
        return (((CraftPlayer)this.player).getHandle()).playerConnection;
    }

    private void sendLine(int line) {
        if (line > 14)
            return;
        if (line < 0)
            return;
        if (!this.created)
            return;
        int score = 15 - line;
        VirtualTeam val = getOrCreateTeam(line);
        for (Packet packet : val.sendLine())
            getPlayer().sendPacket(packet);
        getPlayer().sendPacket((Packet)sendScore(val.getCurrentPlayer(), score));
        val.reset();
    }

    private VirtualTeam getOrCreateTeam(int line) {
        if (this.lines[line] == null)
            this.lines[line] = new VirtualTeam("__fakeScore" + line, null);
        return this.lines[line];
    }

    private PacketPlayOutScoreboardObjective createObjectivePacket(int mode, String displayName) {
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        setField(packet, "a", this.player.getName());
        setField(packet, "d", Integer.valueOf(mode));
        if (mode == 0 || mode == 2) {
            setField(packet, "b", displayName);
            setField(packet, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        }
        return packet;
    }

    private PacketPlayOutScoreboardDisplayObjective setObjectiveSlot() {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();
        setField(packet, "a", Integer.valueOf(1));
        setField(packet, "b", this.player.getName());
        return packet;
    }

    private PacketPlayOutScoreboardScore sendScore(String line, int score) {
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line);
        setField(packet, "b", this.player.getName());
        setField(packet, "c", Integer.valueOf(score));
        setField(packet, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);
        return packet;
    }

    private PacketPlayOutScoreboardScore removeLine(String line) {
        return new PacketPlayOutScoreboardScore(line);
    }

    public class VirtualTeam {
        private final String name;

        private String prefix;

        private String suffix;

        private String currentPlayer;

        private String oldPlayer;

        private boolean prefixChanged;

        private boolean suffixChanged;

        private boolean playerChanged = false;

        private boolean first = true;

        private VirtualTeam(String name, String prefix, String suffix) {
            this.name = name;
            this.prefix = prefix;
            this.suffix = suffix;
        }

        private VirtualTeam(String name, String s) {
            this(name, "", "");
        }

        public String getName() {
            return this.name;
        }

        public String getPrefix() {
            return this.prefix;
        }

        public void setPrefix(String prefix) {
            if (this.prefix == null || !this.prefix.equals(prefix))
                this.prefixChanged = true;
            this.prefix = prefix;
        }

        public String getSuffix() {
            return this.suffix;
        }

        public void setSuffix(String suffix) {
            if (this.suffix == null || !this.suffix.equals(this.prefix))
                this.suffixChanged = true;
            this.suffix = suffix;
        }

        private PacketPlayOutScoreboardTeam createPacket(int mode) {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            ScoreboardSign.setField(packet, "a", this.name);
            ScoreboardSign.setField(packet, "h", Integer.valueOf(mode));
            ScoreboardSign.setField(packet, "b", "");
            ScoreboardSign.setField(packet, "c", this.prefix);
            ScoreboardSign.setField(packet, "d", this.suffix);
            ScoreboardSign.setField(packet, "i", Integer.valueOf(0));
            ScoreboardSign.setField(packet, "e", "always");
            ScoreboardSign.setField(packet, "f", Integer.valueOf(0));
            return packet;
        }

        public PacketPlayOutScoreboardTeam createTeam() {
            return createPacket(0);
        }

        public PacketPlayOutScoreboardTeam updateTeam() {
            return createPacket(2);
        }

        public PacketPlayOutScoreboardTeam removeTeam() {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            ScoreboardSign.setField(packet, "a", this.name);
            ScoreboardSign.setField(packet, "h", Integer.valueOf(1));
            this.first = true;
            return packet;
        }

        public void setPlayer(String name) {
            if (this.currentPlayer == null || !this.currentPlayer.equals(name))
                this.playerChanged = true;
            this.oldPlayer = this.currentPlayer;
            this.currentPlayer = name;
        }

        public Iterable<PacketPlayOutScoreboardTeam> sendLine() {
            List<PacketPlayOutScoreboardTeam> packets = new ArrayList<>();
            if (this.first) {
                packets.add(createTeam());
            } else if (this.prefixChanged || this.suffixChanged) {
                packets.add(updateTeam());
            }
            if (this.first || this.playerChanged) {
                if (this.oldPlayer != null)
                    packets.add(addOrRemovePlayer(4, this.oldPlayer));
                packets.add(changePlayer());
            }
            if (this.first)
                this.first = false;
            return packets;
        }

        public void reset() {
            this.prefixChanged = false;
            this.suffixChanged = false;
            this.playerChanged = false;
            this.oldPlayer = null;
        }

        public PacketPlayOutScoreboardTeam changePlayer() {
            return addOrRemovePlayer(3, this.currentPlayer);
        }

        public PacketPlayOutScoreboardTeam addOrRemovePlayer(int mode, String playerName) {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            ScoreboardSign.setField(packet, "a", this.name);
            ScoreboardSign.setField(packet, "h", Integer.valueOf(mode));
            try {
                Field f = packet.getClass().getDeclaredField("g");
                f.setAccessible(true);
                ((List<String>)f.get(packet)).add(playerName);
            } catch (NoSuchFieldException|IllegalAccessException e) {
                e.printStackTrace();
            }
            return packet;
        }

        public String getCurrentPlayer() {
            return this.currentPlayer;
        }

        public String getValue() {
            return String.valueOf(getPrefix()) + getCurrentPlayer() + getSuffix();
        }

        public void setValue(String value) {
            if (value.length() <= 16) {
                setPrefix("");
                setSuffix("");
                setPlayer(value);
            } else if (value.length() <= 32) {
                setPrefix(value.substring(0, 16));
                setPlayer(value.substring(16));
                setSuffix("");
            } else if (value.length() <= 48) {
                setPrefix(value.substring(0, 16));
                setPlayer(value.substring(16, 32));
                setSuffix(value.substring(32));
            } else {
                throw new IllegalArgumentException("Too long value ! Max 48 characters, value was " + value.length() + " !");
            }
        }
    }

    private static void setField(Object edit, String fieldName, Object value) {
        try {
            Field field = edit.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(edit, value);
        } catch (NoSuchFieldException|IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}