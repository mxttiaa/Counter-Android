package com.mxttiaa.mcounter;

public class Counter {
    private int value;
    private final int minLimit;
    private final int maxLimit;

    public Counter(int initialValue, int minLimit, int maxLimit) {
        this.value = initialValue;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value >= minLimit && value <= maxLimit) {
            this.value = value;
        }
    }

    public boolean increment(int gap) {
        if (value + gap <= maxLimit) {
            value += gap;
            return true;
        }
        return false;
    }

    public boolean decrement(int gap) {
        if (value - gap >= minLimit) {
            value -= gap;
            return true;
        }
        return false;
    }

    public void reset() {
        this.value = minLimit;
    }
}