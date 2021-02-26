package dawid.kotarba.automater.executor

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class PlanStatistics {
    private int stepsExecuted
    private long executionTime

    @JsonCreator
    PlanStatistics(@JsonProperty('stepsExecuted') int stepsExecuted,
                   @JsonProperty('executionTime') long executionTime) {
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
