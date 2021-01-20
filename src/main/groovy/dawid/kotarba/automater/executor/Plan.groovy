package dawid.kotarba.automater.executor

class Plan {
    private String name
    private List<String> executionLines

    Plan(String name) {
        this.name = name
        this.executionLines = []
    }

    Plan(String name, String executionPlan) {
        this.name = name
        this.executionLines = PlanParser.parseToExecutionLines(executionPlan)
    }

    Plan(String name, List<String> executionLines) {
        this.name = name
        this.executionLines = executionLines
    }

    void addExecutionLine(String executionLine) {
        executionLines.add(executionLine)
    }

    String getName() {
        return name
    }

    List<String> getExecutionLines() {
        return executionLines
    }
}
