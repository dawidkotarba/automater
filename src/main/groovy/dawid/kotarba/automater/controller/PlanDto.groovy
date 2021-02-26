package dawid.kotarba.automater.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.commons.lang3.StringUtils

@JsonIgnoreProperties(ignoreUnknown = true)
class PlanDto {
    private static final int DEFAULT_SLEEP_BETWEEN_STEPS = 100
    private int sleepBetweenSteps
    private List<String> executionLines

    @JsonCreator
    Plan(@JsonProperty("sleepBetweenSteps") String sleepBetweenSteps,
         @JsonProperty("executionLines") List<String> executionLines) {
        if (StringUtils.isEmpty(sleepBetweenSteps)) {
            this.sleepBetweenSteps = DEFAULT_SLEEP_BETWEEN_STEPS
        }
        this.executionLines = executionLines
    }

    int getSleepBetweenSteps() {
        return sleepBetweenSteps
    }

    void setSleepBetweenSteps(int sleepBetweenSteps) {
        this.sleepBetweenSteps = sleepBetweenSteps
    }

    List<String> getExecutionLines() {
        return executionLines
    }

    void setExecutionLines(List<String> executionLines) {
        this.executionLines = executionLines
    }
}
