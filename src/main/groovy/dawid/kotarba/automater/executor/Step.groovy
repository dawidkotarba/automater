package dawid.kotarba.automater.executor

interface Step {

    StepType getStepType();

    String getSupportedMethod();

    void executeIfApplicable(String executionLine)
}