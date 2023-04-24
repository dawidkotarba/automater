package dawid.kotarba.automater.executor

class Plan {
    private static final int DEFAULT_SLEEP_BETWEEN_STEPS = 100
    private static final int DEFAULT_MAX_EXECUTION_TIME_SECS = 100
    private List<String> executionLines
    private int sleepBetweenSteps
    private int maxExecutionTimeSecs

    Plan() {
        this.executionLines = []
        this.sleepBetweenSteps = DEFAULT_SLEEP_BETWEEN_STEPS
        this.maxExecutionTimeSecs = DEFAULT_MAX_EXECUTION_TIME_SECS
    }

    Plan withSleepBetweenSteps(int sleepBetweenSteps) {
        if (sleepBetweenSteps <= 0) {
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

    Plan withExecutionLines(List<String> executionLines) {
        this.executionLines = executionLines
        return this
    }

    Plan withMaxExecutionTime(int maxExecutionTimeSecs) {
        if (maxExecutionTimeSecs <= 0) {
            this.maxExecutionTimeSecs = DEFAULT_MAX_EXECUTION_TIME_SECS
        } else {
            this.maxExecutionTimeSecs = maxExecutionTimeSecs
        }
        return this
    }

    void addExecutionLine(String executionLine) {
        executionLines.add(executionLine)
    }

    List<String> getExecutionLines() {
        return executionLines
    }

    int getSleepBetweenSteps() {
        return sleepBetweenSteps
    }

    int getmaxExecutionTimeSecs() {
        return maxExecutionTimeSecs
    }

    private static List<String> parseToExecutionLines(String executionPlan) {
        return executionPlan.tokenize(System.lineSeparator())
    }
}
