package dawid.kotarba.automater.controller

import dawid.kotarba.automater.executor.Plan
import dawid.kotarba.automater.executor.PlanExecutor
import dawid.kotarba.automater.executor.PlanStatistics
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/**
 * This is an alternative way to execute a plan. It can be executed either in UI or by REST calls.
 */
@RestController
class PlanExecutorController {

    private PlanExecutor planExecutor

    PlanExecutorController(PlanExecutor planExecutor) {
        this.planExecutor = planExecutor
    }

    @PostMapping(path = '/start', consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PlanStatistics> start(@RequestBody PlanDto dto) {
        if (planExecutor.isStarted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 'Another plan execution is in progress')
        }
        def plan = new Plan()
                .withExecutionLines(dto.executionLines)
                .withMaxExecutionTime(dto.maxExecutionTimeSecs)
                .withSleepBetweenSteps(dto.sleepBetweenSteps)

        def planStatistics = planExecutor.start(plan)
        return ResponseEntity.ok(planStatistics)
    }

    @PostMapping(path = '/stop')
    void stop() {
        planExecutor.stop()
    }
}
