package dawid.kotarba.automater.executor

interface Step {

    StepType getStepType();

    Optional<String> getSupportedMethod();

    void executeIfApplicable(String executionLine)
}