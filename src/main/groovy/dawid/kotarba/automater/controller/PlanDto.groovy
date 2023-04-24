package dawid.kotarba.automater.controller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PlanDto {
    private int sleepBetweenSteps
    private int maxExecutionTimeSecs
    private Collection<String> executionLines

    int getSleepBetweenSteps() {
        return sleepBetweenSteps
    }

    int getMaxExecutionTimeSecs() {
        return maxExecutionTimeSecs
    }

    Collection<String> getExecutionLines() {
        return executionLines
    }
}
