package dawid.kotarba.automater.executor

class Plan {
    private static final int DEFAULT_SLEEP_BETWEEN_STEPS = 100
    private int sleepBetweenSteps
    private List<String> executionLines

    Plan() {
        this(DEFAULT_SLEEP_BETWEEN_STEPS)
    }

    Plan(int sleepBetweenSteps) {
        this([], DEFAULT_SLEEP_BETWEEN_STEPS)
    }

    Plan(String executionPlan, int sleepBetweenSteps) {
        this.executionLines = PlanParser.parseToExecutionLines(executionPlan)
        this.sleepBetweenSteps = sleepBetweenSteps
    }

    Plan(List<String> executionLines, int sleepBetweenSteps) {
        this.executionLines = executionLines
        this.sleepBetweenSteps = sleepBetweenSteps
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
}
