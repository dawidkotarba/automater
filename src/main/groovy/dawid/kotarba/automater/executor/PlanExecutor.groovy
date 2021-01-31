package dawid.kotarba.automater.executor

import dawid.kotarba.automater.Beans
import dawid.kotarba.automater.Constants
import dawid.kotarba.automater.device.Mouse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

import java.lang.invoke.MethodHandles
import java.util.stream.Collectors

@Service
class PlanExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

    private final Mouse mouse = Beans.mouse
    private double planProgress
    private boolean loopExecution
    private boolean started

    def start(Plan plan) {
        try {
            started = true
            preValidate(plan)
            loopExecution = shallLoopExecution(plan)
            executeSteps(plan)

            while (started & loopExecution) {
                executeSteps(plan)
            }
            stop()
        }
        catch (Exception e) {
            throw new IllegalStateException(e.getMessage())
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

    def preValidate(Plan plan) {
        def supportedStepLines = Steps.steps.keySet()
                .stream()
                .map({
            "${it.getStepType()} ${it.getMethod().get()}"
        })
                .collect(Collectors.toList())

        plan.executionLines.stream()
                .map({ it.trim() })
                .forEach { line ->
            def match = supportedStepLines.stream().anyMatch { stepLine ->
                line.startsWith(stepLine)
            }

            if (!match & !isExecutionLineCommented(line)) {
                throw new UnsupportedOperationException("Cannot execute: $line")
            }
        }
    }


    def executeSteps(Plan plan) {
        planProgress = 0
        for (int i = 0; i < plan.executionLines.size(); i++) {
            Steps.steps.keySet().forEach { step ->
                if (started) {
                    if (shallSkipWhenMouseIsMoving(plan.executionLines)) {
                        LOGGER.debug("Mouse is moving, skipping ${plan.executionLines[i]}")
                    } else if (!isExecutionLineCommented(plan.executionLines[i])) {
                        step.executeIfApplicable(plan.executionLines[i])
                    }
                } else {
                    return
                }
            }
            if (!isExecutionLineCommented(plan.executionLines[i])) {
                sleep(plan.sleepBetweenSteps)
            }
            planProgress = (i + 1) / plan.getExecutionLines().size() as double
        }
    }

    private static boolean shallLoopExecution(Plan plan) {
        plan.executionLines.stream().anyMatch({ line ->
            !isExecutionLineCommented(line) & line.trim().startsWith(StepType.SWITCH.name()) & line.contains(Constants.SWITCH_LOOP)
        })
    }

    private boolean shallSkipWhenMouseIsMoving(List<String> executionLines) {
        def runWhenIdleMouse = executionLines.stream().anyMatch({ line ->
            !isExecutionLineCommented(line) & line.trim().startsWith(StepType.SWITCH.name()) & line.contains(Constants.SWITCH_MOUSE_IDLE)
        })
        runWhenIdleMouse && mouse.isMouseMoving()
    }

    private static boolean isExecutionLineCommented(String executionLine) {
        executionLine.trim().startsWith(Constants.COMMENT_PREFIX)
    }
}
