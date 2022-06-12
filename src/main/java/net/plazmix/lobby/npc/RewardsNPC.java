package net.plazmix.lobby.npc;

import lombok.NonNull;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.holographic.ProtocolHolographic;
import net.plazmix.holographic.updater.SimpleHolographicUpdater;
import net.plazmix.protocollib.entity.impl.FakeSheep;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;

public class RewardsNPC extends ServerNPC<FakeSheep> {

    public RewardsNPC(Location location) {
        super(location);

        setHandle(new FakeSheep(location));
    }

    @Override
    protected void onReceive(@NonNull FakeSheep fakeSheep) {
        addHolographicLine("§d§lЕжедневные бонусы");
        addHolographicLine("§7§oНажмите, чтобы открыть!");

        holographic.teleport(holographic.getLocation().clone().subtract(0, 0.8, 0));
        holographic.setHolographicUpdater(15, new SimpleHolographicUpdater(holographic) {

            private int colorCounter = 0;
            private final ChatColor[] chatColors = {

                    ChatColor.LIGHT_PURPLE,
                    ChatColor.AQUA,
            };

            private final DyeColor[] dyeColors = {

                    DyeColor.PINK,
                    DyeColor.LIGHT_BLUE,
            };

            @Override
            public void accept(ProtocolHolographic protocolHolographic) {

                fakeSheep.setColor(dyeColors[colorCounter]);
                holographic.setOriginalHolographicLine(0, chatColors[colorCounter] + "§lЕжедневные бонусы");

                colorCounter++;
                if (colorCounter >= chatColors.length) {
                    colorCounter = 0;
                }
            }
        });


        fakeSheep.setClickAction(player -> PlazmixCoreApi.dispatchCommand(player, "rewards"));
        fakeSheep.setColor(DyeColor.PINK);

        enableAutoLooking();
    }

}
