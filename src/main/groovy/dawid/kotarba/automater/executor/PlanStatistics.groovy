package dawid.kotarba.automater.executor

class PlanStatistics {
    private int stepsExecuted
    private long executionTime

    PlanStatistics(int stepsExecuted, long executionTime) {
        this.stepsExecuted = stepsExecuted
        this.executionTime = executionTime
    }

    int getStepsExecuted() {
        return stepsExecuted
    }

    long getExecutionTime() {
        return executionTime
    }
}
