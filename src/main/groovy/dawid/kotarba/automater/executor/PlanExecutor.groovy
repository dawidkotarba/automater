package dawid.kotarba.automater.executor

import dawid.kotarba.automater.Beans
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

        def shallLoop = plan.executionLines.stream().anyMatch({ line -> line.contains(StepType.LOOP.name()) })
        while (shallLoop) {
            executeSteps(plan)
        }
    }

    private executeSteps(Plan plan) {
        plan.executionLines.forEach { executionLine ->
            allSteps.steps.forEach { step ->
                def runWhenIdleMouse = plan.executionLines.stream().anyMatch({ line -> line.contains(StepType.MOUSE.name()) & line.contains('IDLE') })
                if (runWhenIdleMouse && mouse.isMouseMoving()) {
                    LOGGER.info("Mouse is moving, skipping $executionLine")
                } else {
                    step.executeIfApplicable(executionLine)
                }
            }
        }
    }
}
