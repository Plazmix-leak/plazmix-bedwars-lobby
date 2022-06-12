package net.plazmix.lobby;

import net.plazmix.PlazmixApi;
import net.plazmix.lobby.command.SpawnCommand;
import net.plazmix.lobby.listener.ScoreboardListener;
import net.plazmix.lobby.listener.SpawnListener;
import net.plazmix.lobby.npc.RewardsNPC;
import net.plazmix.lobby.npc.BWLobbyNPC;
import net.plazmix.lobby.npc.BedWarsSpectateNPC;
import net.plazmix.lobby.npc.manager.ServerNPCManager;
import net.plazmix.lobby.playertop.PlayerTopsStorage;
import net.plazmix.lobby.playertop.database.type.PlayerTopsMysqlDatabase;
import net.plazmix.lobby.playertop.pagination.PlayerTopsPaginationChanger;
import net.plazmix.utility.location.LocationUtil;
import net.plazmix.utility.mojang.MojangSkin;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public final class BedWarsLobbyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // World processes.
        addNpcs();

        // Add tops.
        addTops();

        registerCommands();
        registerListeners();

        enableWorldTicker();

    }

    private void registerListeners() {

        // Default player actions.
        getServer().getPluginManager().registerEvents(new ScoreboardListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
    }

    private void registerCommands() {
        PlazmixApi.registerCommand(new SpawnCommand(this));
    }

    private void addNpcs() {

        // Daily rewards.
        ServerNPCManager.INSTANCE.register(new RewardsNPC(LocationUtil.stringToLocation(getConfig().getString("locations.npc.rewards"))));

        // Arena spectator.
        ServerNPCManager.INSTANCE.register(new BedWarsSpectateNPC(LocationUtil.stringToLocation(getConfig().getString("locations.npc.spectate"))));

        // Select game mode.1x8
        ServerNPCManager.INSTANCE.register(new BWLobbyNPC(new MojangSkin("BEDWARS_SOLO", UUID.randomUUID().toString(), "ewogICJ0aW1lc3RhbXAiIDogMTYzOTE2NzEzMzAwOSwKICAicHJvZmlsZUlkIiA6ICIxN2Q0ODA1ZDRmMTA0YTA5OWRiYzJmNzYzMDNjYmRkZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJnaWZ0bWV0b25uZXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGE5N2M5YjFlMzNmMWFhZjkwNWUwOTI0M2Q4ODdlN2UyMTU0MGQ1YjIxMGUxM2ZkMGRiZTUyMjViY2YzMTA0NSIKICAgIH0KICB9Cn0=", "np02prZJbhAi5EKwA75aSig3SQnHbhI2w1WEHaKy3RrqvqSIfWlZnbNbNpDSqKFma5IPx0L5GPAU2OPeOCHq2in1pN2sRDgpLhVyQTlJHnhxFOdomec4CFw2Y6Q0zpIOOCGpSOzdei0Df3dIbOMw48rZjtbq6NcVYFy0mdruuk/sCHOOmq4tElxyf34ZbC6ZyKLQz826px7IQ/o5mObQKgDlVOkiqTBLKJr7jLuR42STxDiR3UQoPgSAi+F1ywLIkQvERV7LlKEmkiH/9kdlBjqvCvb7uVBeATeXCdh76wF9LeJlnfvgm1KaUfgxKRRfWWSeXK5UeP29tVYCH6fBtuGfzuc7a1W+pPQ41vcmPc+v0l7vZS5y09ZBQE22JkFSfOXfcU3gBlSCxFv91Qi8T7bEXjA/+CzbnP+a1yQQfCl7U8zLxNggQZnUBx4SBlTcP226uFpatPlLXHRkOD2jwmkqczkKJlIJTWgPlvPGNRUFOxTr4Xd3DRZVeUS8EQQ7e/XejW+yxB1x++/CYbLkY//rNFxxWWUcGZfo/gqxfh/FDnv7D+RSmUsjH3LJG6PHEdIdw1trhnkJjxBPlTAtYNJWL7qL0FoRoBxCLdeIX4SrEc3yTzmBvd+MYisuEbq6hZau1Z+2o4AyV5V8ZE5U8oPJKkKi1rZaK3QauRvD/+Q=", System.currentTimeMillis()), "§b§lBedWars 1x8", "bws", LocationUtil.stringToLocation(getConfig().getString("locations.npc.solo"))));
        ServerNPCManager.INSTANCE.register(new BWLobbyNPC(new MojangSkin("BEDWARS_TEAM", UUID.randomUUID().toString(), "ewogICJ0aW1lc3RhbXAiIDogMTY0NTQ2Mzk0NTk4OSwKICAicHJvZmlsZUlkIiA6ICJmMTA0NzMxZjljYTU0NmI0OTkzNjM4NTlkZWY5N2NjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJ6aWFkODciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2EwNzgyODViN2ZkOGZkMTJiMTg2YjVhNDFmMDNjODMzMGM1ZWUwYzBlOWFkY2Q1YzI3M2JmMDY3Y2ZkMTBhYyIKICAgIH0KICB9Cn0=", "xzBtrytvyEcddm94Ah8CnHlrhRJdD4aGn7LkwhwrNWX5vMWj3IK/TyeDO3e3sZtEB0f/VozQ2oB4eBIuo59TdzHJeUV1ZwIb2JqF44/URIQGcyQ6gdzqTaLpkOvqc1VmdDuosyt6OP7NwIh/Dh1OiWlhjQ43DF7uE7iVwaQ+ddm/O9D3HO1kJLr6Bqj2bC0Ph8D378nSJIPDD6JH1Vt2DFO+ZlTEfU26pfKQ4PjDGNC4Dlxm/CTzdXaNl5YDC3iKRZjOYAuHHac+kqPvzEHuCHd2K8WAWB2MmK0dlc03Yl3Om5ireNWyG/IKLdHRiHhABFq5+trpL2V9FDN4W3PsngFsc5rbkBJbT9/SctJjJp1l+A7bcOE5SvoF+75LRcOfKI/hH9eyJCuBs6SExz6O5bRyqte/jVVqc67RiU1nrZmbU44LUEvo/t4SgEl6vCsL3ASKSuiCe4ZKSZ8R8E4CrL9HqHlgOrUofllsmneUC8fUmjuLRnNCKnCEIgK8umKJTSmUOcwq/sKkHnKI45+uIDI0X97uYBgOQZPoHjirAzw3f5aOUqGh07A32yNYpnjMCcKa5Rrwl5ZWCukcPyLbGTdMQ+n5/Zv2MCdT9j+zpnAtaNfBi1pacLQ5Rkb7gfFgM7pV71lHgKu/h+xx//9LivBo+os8Lac1hkPP6IvnBdk=", System.currentTimeMillis()), "§a§lBedWars 2x8", "bwd", LocationUtil.stringToLocation(getConfig().getString("locations.npc.team"))));
    }

    private void enableWorldTicker() {
        for (World world : getServer().getWorlds()) {
            world.setPVP(false);
            world.setDifficulty(Difficulty.NORMAL);

            new BukkitRunnable() {

                @Override
                public void run() {

                    world.setTime(12040);

                    // Set world settings.
                    world.setThundering(false);
                    world.setStorm(false);

                    world.setGameRuleValue("randomTickSpeed", "0");

                    world.setWeatherDuration(0);

                }

            }.runTaskTimer(this, 0, 1);
        }
    }

    private void addTops() {
        Location location = LocationUtil.stringToLocation(getConfig().getString("locations.tops"));
        PlayerTopsPaginationChanger paginationChanger = PlayerTopsPaginationChanger.create();

       // Solo
       paginationChanger.addPlayerTops(PlayerTopsStorage.newBuilder()
               .setDatabaseManager(new PlayerTopsMysqlDatabase("BedWarsSolo", "Wins"))

               .setLocation(location)
               .setSkullParticle(Particle.TOTEM)

               .setLimit(10)
               .setUpdater(60)

               .setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBiZjM0YTcxZTc3MTViNmJhNTJkNWRkMWJhZTVjYjg1Zjc3M2RjOWIwZDQ1N2I0YmZjNWY5ZGQzY2M3Yzk0In19fQ==")

               .setStatsName("BedWars Solo")
               .setDescription("Топ 10 игроков, набравшие наибольшее",
                       "количество побед по данному режиму")

               .setValueSuffix("побед"));

       //// Team
       //paginationChanger.addPlayerTops(PlayerTopsStorage.newBuilder()
       //        .setDatabaseManager(new PlayerTopsMysqlDatabase("BedWarsTeam", "Wins"))
       //
       //        .setLocation(location)
       //        .setSkullParticle(Particle.TOTEM)
       //
       //        .setLimit(10)
       //        .setUpdater(60)
       //
       //        .setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDY0YmRjNmY2MDA2NTY1MTFiZWY1OTZjMWExNmFhYjFkM2Y1ZGJhYWU4YmVlMTlkNWMwNGRlMGRiMjFjZTkyYyJ9fX0=")
       //
       //        .setStatsName("SkyWars Team")
       //        .setDescription("Топ 10 игроков, набравшие наибольшее",
       //                "количество побед по данному режиму")
       //
       //        .setValueSuffix("побед"));

        // Spawn all tops.
        paginationChanger.spawn();
    }

}
