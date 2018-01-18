package jakojaannos.life.entity.capability;

import com.google.common.base.Preconditions;
import jakojaannos.life.api.entity.capability.IHealthTracker;
import jakojaannos.life.config.ModConfig;

class HealthTracker implements IHealthTracker {
    private int backlogLength;
    private int nEntries;
    private float[] values;
    private int timer;

    HealthTracker() {
        backlogLength = ModConfig.revival.spawningHealth.healthTrackingSamples;

        values = new float[1];
    }

    @Override
    public int getBacklogLength() {
        return backlogLength;
    }

    @Override
    public void setBacklogLength(int ticks) {
        if (ticks == values.length) {
            return;
        }

        backlogLength = ticks;

        float[] newValues = new float[ticks];
        System.arraycopy(values, 0, newValues, 0, newValues.length);
        values = newValues;
    }

    @Override
    public void addEntry(float health) {
        // Shift array indices
        System.arraycopy(values, 0, values, 1, nEntries);
        nEntries += nEntries < values.length - 1 ? 1 : 0;

        values[0] = health;
    }

    @Override
    public float getHealth(int ticks) {
        Preconditions.checkState(ticks < getBacklogLength() && ticks >= 0);
        return values[Math.min(ticks, nEntries)];
    }

    @Override
    public float getTrackedMaxHealth() {
        return 0;
    }


    @Override
    public int getTimer() {
        return timer;
    }

    @Override
    public void tick() {
        timer++;
    }

    @Override
    public int getUpdateInterval() {
        return ModConfig.revival.spawningHealth.healthTrackingInterval;
    }
}
