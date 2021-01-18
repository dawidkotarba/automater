package dawid.kotarba.automater.executor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

import java.lang.invoke.MethodHandles

@Service
class PlanExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Steps allSteps

    PlanExecutor() {
        this.allSteps = new Steps()
    }

    void execute(Plan plan) {
        LOGGER.info("Executing plan: $plan.name")
        plan.executionLines.forEach { executionLine ->
            allSteps.steps.forEach { step ->
                step.executeIfApplicable(executionLine)
            }
        }
    }
}
