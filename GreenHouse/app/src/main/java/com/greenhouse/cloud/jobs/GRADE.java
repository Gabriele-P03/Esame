package com.greenhouse.cloud.jobs;

public enum GRADE {
    CEO(3),
    COO(2),
    ANALYST(1),
    EMPLOYEE(0);

    int index;

    GRADE(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
