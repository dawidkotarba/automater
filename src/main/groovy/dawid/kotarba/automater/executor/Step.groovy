package dawid.kotarba.automater.executor

interface Step {

    StepType getStepType()

    Optional<String> getMethod()

    int getArgumentsCount()

    boolean isApplicable(String executionLine)

    void executeIfApplicable(String executionLine)
}