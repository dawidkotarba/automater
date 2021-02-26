package dawid.kotarba.automater.controller

import dawid.kotarba.automater.executor.Plan
import dawid.kotarba.automater.executor.PlanExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * This is an alternative way to execute a plan. It can be executed either in UI or by REST calls.
 */
@RestController
class PlanExecutorController {

    private PlanExecutor planExecutor

    @Autowired
    PlanExecutorController(PlanExecutor planExecutor) {
        this.planExecutor = planExecutor
    }

    @PostMapping(path = '/start', consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void start(@RequestBody PlanDto dto) {
        if (planExecutor.isStarted()) {
            throw new IllegalStateException('Another plan is in progress')
        }
        def plan = new Plan(dto.executionLines, dto.sleepBetweenSteps)
        planExecutor.start(plan)
    }

    @PostMapping(path = '/stop')
    void stop() {
        planExecutor.stop()
    }
}
