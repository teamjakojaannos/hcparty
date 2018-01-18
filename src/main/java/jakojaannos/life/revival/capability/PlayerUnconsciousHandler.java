package jakojaannos.life.revival.capability;

import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import jakojaannos.life.config.ModConfig;

public class PlayerUnconsciousHandler implements IUnconsciousHandler {
    private int duration;
    private int timer;

    public PlayerUnconsciousHandler() {
        this.timer = 0;
        this.duration = ModConfig.revival.unconscious.duration;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int getTimer() {
        return timer;
    }

    @Override
    public void setTimer(int time) {
        this.timer = time;
    }
}
