package dawid.kotarba.automater.executor

import dawid.kotarba.automater.Beans
import dawid.kotarba.automater.Constants
import dawid.kotarba.automater.device.Mouse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

import java.lang.invoke.MethodHandles

@Service
class PlanExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

    private final Mouse mouse = Beans.mouse
    private Plan currentPlan
    private double planProgress
    private boolean started

    void start(Plan plan) {
        LOGGER.info("Executing plan: $plan.name")
        currentPlan = plan
        started = true
        executeSteps(plan)

        while (shallLoopExecution(plan)) {
            executeSteps(plan)
        }
        stop()
    }

    void stop() {
        started = false
    }

    boolean isStarted() {
        return started
    }

    Plan getCurrentPlan() {
        return currentPlan
    }

    double getPlanProgress() {
        return planProgress
    }

    private executeSteps(Plan plan) {
        planProgress = 0
        for (int i = 0; i < plan.executionLines.size(); i++) {
            Steps.steps.forEach { step ->
                if (this.started) {
                    if (shallSkipWhenMouseIsMoving(plan.executionLines)) {
                        LOGGER.info("Mouse is moving, skipping ${plan.executionLines[i]}")
                    } else if (!isExecutionLineCommented(plan.executionLines[i])) {
                        step.executeIfApplicable(plan.executionLines[i])
                    }
                }
            }
            planProgress = (i + 1) / plan.getExecutionLines().size() as double
        }
    }

    private static boolean shallLoopExecution(Plan plan) {
        plan.executionLines.stream().anyMatch({ line -> !isExecutionLineCommented(line) & line.contains(StepType.LOOP.name()) })
    }

    private boolean shallSkipWhenMouseIsMoving(List<String> executionLines) {
        def runWhenIdleMouse = executionLines.stream().anyMatch({ line -> !isExecutionLineCommented(line) & line.contains(StepType.MOUSE.name()) & line.contains(Constants.MOUSE_IDLE) })
        runWhenIdleMouse && mouse.isMouseMoving()
    }

    private static boolean isExecutionLineCommented(String executionLine) {
        executionLine.startsWith(Constants.COMMENT_PREFIX)
    }
}
