package com.greenhouse.cloud.jobs;

public enum GRADE {

    CEO(2),
    ANALYST(1),
    HARVESTER(0);

    int index;

    GRADE(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
