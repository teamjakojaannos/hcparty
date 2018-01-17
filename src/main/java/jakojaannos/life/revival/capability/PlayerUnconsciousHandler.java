package jakojaannos.life.revival.capability;

import jakojaannos.life.api.revival.capabilities.IUnconsciousHandler;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerUnconsciousHandler implements IUnconsciousHandler {
    private final EntityPlayer player;
    private int timer;

    public PlayerUnconsciousHandler(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public int getUnconsciousDuration() {
        return 60 * 20; // 1 minute
    }

    @Override
    public int getUnconsciousTimer() {
        return timer;
    }

    @Override
    public void setTimer(int time) {
        this.timer = time;
    }

    @Override
    public void tickTimer() {
        timer++;
    }

    @Override
    public void resetTimer() {
        timer = 0;
    }
}
