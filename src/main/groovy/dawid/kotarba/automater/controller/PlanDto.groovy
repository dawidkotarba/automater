package dawid.kotarba.automater.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class PlanDto {
    private int sleepBetweenSteps
    private int maxExecutionTimeSecs
    private List<String> executionLines

    int getSleepBetweenSteps() {
        return sleepBetweenSteps
    }

    int getmaxExecutionTimeSecs() {
        return maxExecutionTimeSecs
    }

    List<String> getExecutionLines() {
        return executionLines
    }
}
