package net.recondev.commons.builders;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
@SuppressWarnings("unused")
public class SimpleScoreboardBuilder {

    private final Scoreboard scoreboard;
    private final Objective  objective ;
    private       int        index     ;

    public SimpleScoreboardBuilder(final Scoreboard scoreboard, final Objective objective, final int index) {
        this.scoreboard = scoreboard;
        this.objective  = objective ;
        this.index      = index     ;
    }

    public SimpleScoreboardBuilder(final Scoreboard scoreboard, final Objective objective) {
        this(scoreboard, objective, getMinValue(objective));
    }

    public SimpleScoreboardBuilder(final Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        final Objective objective  = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (objective == null) {
            this.index     = 15                                                                  ;
            this.objective = scoreboard.registerNewObjective("board","dummy");
        } else {
            this.objective = objective;
            this.index     = getMinValue(objective);
        }
    }

    public SimpleScoreboardBuilder(final Scoreboard scoreboard, final int index) {
        this.scoreboard = scoreboard;
        final Objective objective  = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (objective == null) {
            this.index     = index                                                               ;
            this.objective = scoreboard.registerNewObjective("board","dummy");
        } else {
            this.objective = objective;
            this.index     = index    ;
        }
    }

    public SimpleScoreboardBuilder() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective  = scoreboard.registerNewObjective("board","dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.index = 15;
    }

    public SimpleScoreboardBuilder nextLine(final String s) {
        final Score score = objective.getScore(s);
        score.setScore(index--);
        return this;
    }

    public SimpleScoreboardBuilder removeLine(final int line) {
        for (final String entry : scoreboard.getEntries()) if (objective.getScore(entry).getScore() == line) scoreboard.resetScores(entry);
        return this;
    }

    public void removeLine(final int line, final String s) {
        for (final String entry : scoreboard.getEntries()) if (!entry.equals(s) && objective.getScore(entry).getScore() == line) scoreboard.resetScores(entry);
    }
    public SimpleScoreboardBuilder setLine(final int line, final String s) {
        if (objective.getScore(s).getScore() == line) return this; // If the string already there It will take less resource from the server
        final Score score = objective.getScore(s);
        score.setScore(line);
        removeLine(line, s);
        return this;
    }

    public SimpleScoreboardBuilder setDisplayName(final String s) {
        objective.setDisplayName(s);
        return this;
    }

    public Scoreboard build() {
        return scoreboard;
    }

    // Utils

    public static SimpleScoreboardBuilder getOrCreate(final Player player) {
        final  Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) return new SimpleScoreboardBuilder();
        return new SimpleScoreboardBuilder(scoreboard, 0);
    }

    public static Objective getObjective(final Scoreboard scoreboard) {
        if (scoreboard.getObjective(DisplaySlot.SIDEBAR) == null) return null;
        return scoreboard.getObjective(DisplaySlot.SIDEBAR);
    }

    public static int getMaxValue(final Objective objective) {
        if (objective == null) return 0;
        int index = 0;

        final Scoreboard scoreboard = objective.getScoreboard();
        if (scoreboard == null) return 0;

        for (final String key : scoreboard.getEntries()) {
            final Score score = objective.getScore(key);
            if (index < score.getScore()) index = score.getScore();
        }

        return index;
    }

    public static int getMinValue(final Objective objective) {
        if (objective == null) return 0;
        int index = 0;

        final Scoreboard scoreboard = objective.getScoreboard();
        if (scoreboard == null) return 0;

        for (final String key : scoreboard.getEntries()) {
            final Score score = objective.getScore(key);
            if (index > score.getScore()) index = score.getScore();
        }

        return index;
    }
}
