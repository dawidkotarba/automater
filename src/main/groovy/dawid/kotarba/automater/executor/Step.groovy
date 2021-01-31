package dawid.kotarba.automater.executor

interface Step {

    StepType getStepType()

    Optional<String> getMethod()

    int argumentsCount()

    boolean isApplicable(String executionLine)

    void executeIfApplicable(String executionLine)
}