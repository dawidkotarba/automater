package dawid.kotarba.automater.executor

import dawid.kotarba.automater.Constants
import dawid.kotarba.automater.device.Mouse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
        def startTime = System.currentTimeMillis()
        try {
            started = true
            validate(plan)
            loopExecution = shallLoopExecution(plan)

            checkExecutionTimePeriodically(startTime, plan.getmaxExecutionTimeSecs())

            def executedSteps = executeSteps(plan)
            def executedStepsInLoop = 0

            while (started && loopExecution) {
                executedStepsInLoop += executeSteps(plan)
            }
            stop()
            return new PlanStatistics(executedSteps + executedStepsInLoop, getElapsedTime(startTime))
        }
        catch (Exception e) {
            throw new IllegalStateException(e.message)
        }
    }

    private checkExecutionTimePeriodically(startTime, long planExecutionTimeInSecs) {
        new Thread(new Runnable() {
            @Override
            void run() {
                while (started) {
                    started = getElapsedTime(startTime) < planExecutionTimeInSecs * 1000
                    sleep(1000)
                }
            }
        }).start()
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


    private int executeSteps(Plan plan) {
        def stepsExecuted = 0
        planProgress = 0
        for (int i = 0; i < plan.executionLines.size(); i++) {
            Steps.steps.keySet().forEach { step ->
                if (started) {
                    if (shallSkipWhenMouseIsMoving(plan.executionLines) || shallSkipWhenMouseIsActive(plan.executionLines)) {
                        LOGGER.debug("Mouse is moving/active, skipping ${plan.executionLines[i]}")
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
        def isSwitchEnabled = executionLines.stream().anyMatch { line ->
            !isExecutionLineCommented(line) && line.trim().startsWith(StepType.SWITCH.name()) && line.contains(Constants.SWITCH_MOUSE_NOT_MOVING)
        }
        isSwitchEnabled && mouse.mouseMoving
    }

    private boolean shallSkipWhenMouseIsActive(List<String> executionLines) {
        def isSwitchEnabled = executionLines.stream().anyMatch { line ->
            !isExecutionLineCommented(line) && line.trim().startsWith(StepType.SWITCH.name()) && line.contains(Constants.SWITCH_MOUSE_INACTIVE)
        }
        isSwitchEnabled && mouse.mouseActive
    }

    private long getElapsedTime(long startTime) {
        def currentTime = System.currentTimeMillis()
        return currentTime - startTime
    }

    private static boolean isExecutionLineCommented(String executionLine) {
        executionLine.trim().startsWith(Constants.COMMENT_PREFIX)
    }
}
