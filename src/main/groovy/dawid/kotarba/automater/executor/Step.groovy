package dawid.kotarba.automater.executor

interface Step {

    StepType getStepType()

    Optional<String> getSupportedMethod()

    boolean isApplicable(String executionLine)

    void executeIfApplicable(String executionLine)
}