package net.plazmix.lobby.command;

import net.plazmix.command.BaseCommand;
import net.plazmix.lobby.BedWarsLobbyPlugin;
import net.plazmix.utility.location.LocationUtil;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand<Player> {

    private final BedWarsLobbyPlugin plugin;

    public SpawnCommand(BedWarsLobbyPlugin plugin) {
        super("spawn", "ызфцт", "спаун", "спавн", "спаввн");
        this.plugin = plugin;
    }

    @Override
    protected void onExecute(Player player, String[] args) {
        player.teleport(LocationUtil.stringToLocation(plugin.getConfig().getString("locations.spawn")));
    }
}
