package thesis.effigy.com.effigy.interfaces.scores;

/**
 * Created by Borys on 1/16/17.
 */

public interface ScoreUpdate {
    void scoreWasUpdated(boolean success);
    void updateTotalScore(int totalScore);
}
