package dawid.kotarba.automater.executor

class Plan {
    private String name
    private List<String> executionLines

    Plan(String name) {
        this.name = name
        executionLines = []
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
