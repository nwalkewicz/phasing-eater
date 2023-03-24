package dev.nwalk.phasingeater;


public interface BreakProgressTracker {
    public float getBreakProgress(float tickDelta);

    public default float getBreakProgressPercent(float tickDelta) {
        return this.getBreakProgress(tickDelta) * 100;
    }
}