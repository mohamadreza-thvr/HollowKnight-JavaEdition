package com.hallowKnight.Menus.endGameMenu;

import com.hallowKnight.gameplay.knight.KnightModel;
import com.hallowKnight.gameplay.core.AchievementManager;

public class EndGameModel {
    public int kills;
    public int deaths;
    public String time;

    public EndGameModel(KnightModel knight) {
        this.kills = knight.getTotalKills();
        this.deaths = knight.getTotalDeaths();

        float totalTimeInSeconds = AchievementManager.getInstance().totalPlayTime;

        int minutes = (int) (totalTimeInSeconds / 60);
        int seconds = (int) (totalTimeInSeconds % 60);
        this.time = String.format("%02d:%02d", minutes, seconds);
    }
}
