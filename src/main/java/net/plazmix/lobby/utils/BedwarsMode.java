package net.plazmix.lobby.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.plazmix.game.GamePlugin;
import org.bukkit.ChatColor;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum BedwarsMode {

    SOLO(ChatColor.GREEN, "Solo"),
    TEAM(ChatColor.AQUA, "Team");

    public static final BedwarsMode[] VALUES = BedwarsMode.values();

    public static BedwarsMode fromTitle(@NonNull String modeTitle) {
        for (BedwarsMode bedwarsMode : VALUES) {

            if (bedwarsMode.title.equalsIgnoreCase(modeTitle)) {
                return bedwarsMode;
            }
        }

        return null;
    }

    public static BedwarsMode getCurrentMode(@NonNull GamePlugin gamePlugin) {
        return fromTitle(gamePlugin.getService().getServerMode());
    }

    ChatColor color;
    String title;
}
