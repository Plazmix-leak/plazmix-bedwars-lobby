package net.plazmix.lobby.npc;

import lombok.NonNull;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.inventory.impl.BaseSimpleInventory;
import net.plazmix.protocollib.entity.impl.FakePlayer;
import net.plazmix.utility.ItemUtil;
import net.plazmix.utility.mojang.MojangSkin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BedWarsSpectateNPC extends ServerPlayerNPC {

    public BedWarsSpectateNPC(Location location) {
        super(new MojangSkin("bedwars_spectate",
                UUID.randomUUID().toString(),
                "ewogICJ0aW1lc3RhbXAiIDogMTYzOTE3MjE3OTM5MCwKICAicHJvZmlsZUlkIiA6ICJkMGI4MjE1OThmMTE0NzI1ODBmNmNiZTliOGUxYmU3MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJqYmFydHl5IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg4MzZmZDQ0MmI2M2Y5ZjEwZjdmYTY0NzA2MDhlNzY5M2E4NWIwMjNkMzYxNzBkODNlZDU4ZjI4MjExZjdmNWUiCiAgICB9CiAgfQp9",
                "E2KIiBa0vKXY6Srf0RafHrGDs/RI6T9CDIm3lY8AS83mLwit8A7iU8KlsSXjPWdjJZRYU+px+RBradg92e5uRGxICyD9K1bGG+Lpq0R3lDBEI0yYQrObFD/3AveWsRfr6kzOsHwBHT6GGwRtn16qXFxD+sfqroJvN0jx9DkCXN0GFuMOnyOuXaqj6BNhziZgjaiHjwFpAyDghLCT5bAucNsf0qvzthznHqNnMpYKTCgtpWO86LJDliaFIQ0gCOEoKFI8dD2+U5JzZPPn8hK/tC1iEclYfdIMAUQP6jN61ZyBsTirK1qRH+Pi8qqG9S0r/z4tCt1kWtKq+hqhhQ8/k1uCGTex3e0R/on1cMc1VG/UVXHrbGvFPJ+HLNyW2WcpUAdLMvp3qFej66qNiV8vmAAUYTz8HXzSc8UcDFnIygnydZa/mLuCVtC9CvKZwb9sHhT1S5rPJGcjMhvxk2WFSbFssPgRz69zgUziUDURALldXVC3QCtsurwVeOK37NKeeFcOL2A1hBfMM0+HFZ6ybDtKHfNIxlC37xqaz96Cq0PFGjPCS7M5QrtCtKrhTHvGBwnMsgqGwLpqge8k6K5XBAq8NnC6Qg1z6iP4uKKCqKCDRouSlXmXbgcf8p9qS8rZLINFWSIiRVCUpv1nlSQ1aLp6w5HcBqfLUMdpSupgLuU=",
                System.currentTimeMillis()), location);
    }

    @Override
    public void onReceive(@NonNull FakePlayer fakePlayer) {
        addHolographicLine("§e§lНаблюдатель");
        addHolographicLine("§7Нажмите, чтобы перейти наблюдать");
        addHolographicLine("§7за запущенными аренами");

        fakePlayer.setClickAction(player -> new ArcadeSpectateSelectModeInventory().openInventory(player));
        enableAutoLooking();
    }

    private static class ArcadeSpectateSelectModeInventory extends BaseSimpleInventory {

        public ArcadeSpectateSelectModeInventory() {
            super("Наблюдатель", 5);
        }

        @Override
        public void drawInventory(Player player) {
            setClickItem(21, ItemUtil.newBuilder(Material.WORKBENCH)
                            .setName("§e§lBedWars Solo")
                            .addLore("§7▸ Нажмите, чтобы посмотреть!")
                            .build(),

                    (player1, inventoryClickEvent) -> PlazmixCoreApi.dispatchCommand(player, "gamespectator bws"));

            setClickItem(23, ItemUtil.newBuilder(Material.SKULL_ITEM)
                            .setDurability(3)
                            .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBiZjM0YTcxZTc3MTViNmJhNTJkNWRkMWJhZTVjYjg1Zjc3M2RjOWIwZDQ1N2I0YmZjNWY5ZGQzY2M3Yzk0In19fQ==")

                            .setName("§e§lBedWars Team")
                            .addLore("§7▸ Нажмите, чтобы посмотреть!")
                            .build(),

                    (player1, inventoryClickEvent) -> PlazmixCoreApi.dispatchCommand(player, "gamespectator bwd"));
        }

    }

}
