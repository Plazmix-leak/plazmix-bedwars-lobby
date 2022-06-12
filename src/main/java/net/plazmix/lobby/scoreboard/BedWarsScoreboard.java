package net.plazmix.lobby.scoreboard;

import lombok.NonNull;
import net.plazmix.scoreboard.BaseScoreboardBuilder;
import net.plazmix.scoreboard.BaseScoreboardScope;
import net.plazmix.scoreboard.animation.ScoreboardDisplayFlickAnimation;
import net.plazmix.utility.NumberUtil;
import net.plazmix.utility.player.PlazmixUser;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class BedWarsScoreboard {

    public BedWarsScoreboard(@NonNull Player player) {
        PlazmixUser plazmixUser = PlazmixUser.of(player);

        BaseScoreboardBuilder scoreboardBuilder = BaseScoreboardBuilder.newScoreboardBuilder();
        scoreboardBuilder.scoreboardScope(BaseScoreboardScope.PROTOTYPE);

        ScoreboardDisplayFlickAnimation displayFlickAnimation = new ScoreboardDisplayFlickAnimation();

        displayFlickAnimation.addColor(ChatColor.LIGHT_PURPLE);
        displayFlickAnimation.addColor(ChatColor.DARK_PURPLE);
        displayFlickAnimation.addColor(ChatColor.WHITE);
        displayFlickAnimation.addColor(ChatColor.DARK_PURPLE);

        displayFlickAnimation.addTextToAnimation(plazmixUser.localization().getMessageText("BEDWARS_BOARD_TITLE"));

        scoreboardBuilder.scoreboardDisplay(displayFlickAnimation);
        scoreboardBuilder.scoreboardUpdater((baseScoreboard, player1) -> {

            baseScoreboard.setScoreboardDisplay(displayFlickAnimation);

            List<String> scoreboardLineList = getScoreboardLines(plazmixUser);

            for (String scoreboardLine : scoreboardLineList) {
                baseScoreboard.setScoreboardLine(scoreboardLineList.size() - scoreboardLineList.indexOf(scoreboardLine), player, scoreboardLine);
            }

        }, 20);

        scoreboardBuilder.build().setScoreboardToPlayer(player);
    }

    private List<String> getScoreboardLines(@NonNull PlazmixUser plazmixUser) {
        List<String> scoreboardLineList = new LinkedList<>();

        int solo_kills = plazmixUser.getDatabaseValue("BedWarsSolo", "Kills");
        int team_kills = plazmixUser.getDatabaseValue("BedWarsTeam", "Kills");
        int count_kills = solo_kills + team_kills;

        int solo_wins = plazmixUser.getDatabaseValue("BedWarsSolo", "Wins");
        int team_wins = plazmixUser.getDatabaseValue("BedWarsTeam", "Wins");
        int count_wins = solo_wins + team_wins;

        for (String scoreboardLine : plazmixUser.localization().getMessageList("BEDWARS_BOARD_LINES")) {
            scoreboardLine = scoreboardLine

                    //TODO: Доделать когда Миша решит написать св
                    //.replace("sw_level", NumberUtil.spaced(plazmixUser.getDatabaseValue("", "")))

                    //.replace("%min%", NumberUtil.spaced(plazmixUser.getDatabaseValue("", "")))
                    //.replace("%max%", NumberUtil.spaced(plazmixUser.getDatabaseValue("", "")))
                    //.replace("%progress%", NumberUtil.spaced(plazmixUser.getDatabaseValue("", "")))

                    .replace("%kills%", NumberUtil.spaced(count_kills))
                    .replace("%wins%", NumberUtil.spaced(count_wins))

                    //.replace("%deaths%", NumberUtil.spaced(plazmixUser.getDatabaseValue("", "")))

                    .replace("%plazma%", NumberUtil.spaced(plazmixUser.getGolds()))
                    .replace("%money%", NumberUtil.spaced(plazmixUser.getCoins()));

            scoreboardLineList.add(ChatColor.translateAlternateColorCodes('&', scoreboardLine));
        }

        return scoreboardLineList;
    }
}