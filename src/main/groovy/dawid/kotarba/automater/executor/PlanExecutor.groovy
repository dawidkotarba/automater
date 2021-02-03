package dawid.kotarba.automater.executor

import dawid.kotarba.automater.Constants
import dawid.kotarba.automater.device.Mouse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StopWatch

import java.lang.invoke.MethodHandles

@Service
class PlanExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    private Mouse mouse
    private double planProgress
    private boolean loopExecution
    private boolean started

    @Autowired
    PlanExecutor(Mouse mouse) {
        this.mouse = mouse
    }

    PlanStatistics start(Plan plan) {
        def stopWatch = new StopWatch()
        try {
            stopWatch.start()
            started = true
            validate(plan)
            loopExecution = shallLoopExecution(plan)
            def executedSteps = executeSteps(plan)
            def executedStepsInLoop = 0

            while (started && loopExecution) {
                executedStepsInLoop += executeSteps(plan)
            }
            stop()
            stopWatch.stop()
            return new PlanStatistics(executedSteps + executedStepsInLoop, stopWatch.totalTimeMillis)
        }
        catch (Exception e) {
            throw new IllegalStateException(e.message)
        }
    }

    def stop() {
        started = false
        loopExecution = false
    }

    boolean isStarted() {
        return started
    }

    double getPlanProgress() {
        return planProgress
    }

    boolean getLoopPlan() {
        return loopExecution
    }

    static def validate(Plan plan) {
        plan.executionLines.stream()
                .map({ it.trim() })
                .forEach { executionLine ->
                    def match = Steps.steps.keySet().stream().anyMatch { stepLine ->
                        def tokenizedExecutionLine = executionLine.tokenize()
                        tokenizedExecutionLine[0] == stepLine.stepType.name() &&
                                tokenizedExecutionLine[1] == stepLine.method.get() &&
                                tokenizedExecutionLine.size() == 2 + stepLine.argumentsCount
                    }

                    if (!match && !isExecutionLineCommented(executionLine)) {
                        throw new UnsupportedOperationException("Validation error: $executionLine")
                    }
                }
    }


    int executeSteps(Plan plan) {
        def stepsExecuted = 0
        planProgress = 0
        for (int i = 0; i < plan.executionLines.size(); i++) {
            Steps.steps.keySet().forEach { step ->
                if (started) {
                    if (shallSkipWhenMouseIsMoving(plan.executionLines)) {
                        LOGGER.debug("Mouse is moving, skipping ${plan.executionLines[i]}")
                    } else if (!isExecutionLineCommented(plan.executionLines[i])) {
                        if (step.executeIfApplicable(plan.executionLines[i])) {
                            stepsExecuted++
                        }
                    }
                } else {
                    return
                }
            }
            if (!isExecutionLineCommented(plan.executionLines[i])) {
                sleep(plan.sleepBetweenSteps)
            }
            planProgress = (i + 1) / plan.executionLines.size() as double
        }
        return stepsExecuted
    }

    private static boolean shallLoopExecution(Plan plan) {
        plan.executionLines.stream().anyMatch { line ->
            !isExecutionLineCommented(line) && line.contains(Constants.SWITCH_LOOP)
        }
    }

    private boolean shallSkipWhenMouseIsMoving(List<String> executionLines) {
        def runWhenIdleMouse = executionLines.stream().anyMatch { line ->
            !isExecutionLineCommented(line) && line.trim().startsWith(StepType.SWITCH.name()) && line.contains(Constants.SWITCH_MOUSE_IDLE)
        }
        runWhenIdleMouse && mouse.mouseMoving
    }

    private static boolean isExecutionLineCommented(String executionLine) {
        executionLine.trim().startsWith(Constants.COMMENT_PREFIX)
    }
}
