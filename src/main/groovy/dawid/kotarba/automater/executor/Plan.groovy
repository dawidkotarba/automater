package dawid.kotarba.automater.executor

import org.slf4j.LoggerFactory

import java.lang.invoke.MethodHandles

class Plan {
    private static final def LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int DEFAULT_SLEEP_BETWEEN_STEPS = 100
    private static final int DEFAULT_MAX_EXECUTION_TIME_SECS = 100
    private Collection<String> executionLines
    private int sleepBetweenSteps
    private int maxExecutionTimeSecs

    Plan() {
        this.executionLines = []
        this.sleepBetweenSteps = DEFAULT_SLEEP_BETWEEN_STEPS
        this.maxExecutionTimeSecs = DEFAULT_MAX_EXECUTION_TIME_SECS
    }

    Plan withSleepBetweenSteps(int sleepBetweenSteps) {
        if (sleepBetweenSteps <= 0) {
            LOGGER.info("Sleep between steps has to be positive. Current value: $sleepBetweenSteps")
            this.sleepBetweenSteps = DEFAULT_SLEEP_BETWEEN_STEPS
        } else {
            this.sleepBetweenSteps = sleepBetweenSteps
        }
        return this
    }

    Plan withExecutionPlan(String executionPlan) {
        this.executionLines = parseToExecutionLines(executionPlan)
        return this
    }

    Plan withExecutionLines(Collection<String> executionLines) {
        this.executionLines = executionLines
        return this
    }

    Plan withMaxExecutionTime(int maxExecutionTimeSecs) {
        if (maxExecutionTimeSecs <= 0) {
            LOGGER.info("Max execution time has to be positive. Current value: $maxExecutionTimeSecs")
            this.maxExecutionTimeSecs = DEFAULT_MAX_EXECUTION_TIME_SECS
        } else {
            this.maxExecutionTimeSecs = maxExecutionTimeSecs
        }
        return this
    }

    void addExecutionLine(String executionLine) {
        executionLines.add(executionLine)
    }

    Collection<String> getExecutionLines() {
        return executionLines
    }

    int getSleepBetweenSteps() {
        return sleepBetweenSteps
    }

    int getMaxExecutionTimeSecs() {
        return maxExecutionTimeSecs
    }

    private static Collection<String> parseToExecutionLines(String executionPlan) {
        return executionPlan.tokenize(System.lineSeparator())
    }
}
