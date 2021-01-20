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
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Steps allSteps
    private final Mouse mouse = Beans.mouse

    PlanExecutor() {
        this.allSteps = new Steps()
    }

    void execute(Plan plan) {
        LOGGER.info("Executing plan: $plan.name")
        executeSteps(plan)

        while (shallLoopExecution(plan)) {
            executeSteps(plan)
        }
    }

    private executeSteps(Plan plan) {
        plan.executionLines.forEach { executionLine ->
            allSteps.steps.forEach { step ->
                if (shallSkipWhenMouseIsMoving(plan.executionLines)) {
                    LOGGER.info("Mouse is moving, skipping $executionLine")
                } else if (!isExecutionLineCommented(executionLine)) {
                    step.executeIfApplicable(executionLine)
                }
            }
        }
    }

    private static boolean shallLoopExecution(Plan plan) {
        plan.executionLines.stream().anyMatch({ line -> !isExecutionLineCommented(line) & line.contains(StepType.LOOP.name()) })
    }

    private boolean shallSkipWhenMouseIsMoving(List<String> executionLines) {
        def runWhenIdleMouse = executionLines.stream().anyMatch({ line -> line.contains(StepType.MOUSE.name()) & line.contains(Constants.MOUSE_IDLE) })
        runWhenIdleMouse && mouse.isMouseMoving()
    }

    private static boolean isExecutionLineCommented(String executionLine) {
        executionLine.startsWith(Constants.COMMENT_PREFIX)
    }
}
