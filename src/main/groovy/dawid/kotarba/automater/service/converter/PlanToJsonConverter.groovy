package dawid.kotarba.automater.service.converter

import dawid.kotarba.automater.service.executor.Plan
import groovy.json.JsonOutput

import java.util.stream.Collectors

class PlanToJsonConverter {

    String convertToJson(String planAsString, String sleepBetweenStepsField, String executionTimeField) {
        def plan = new Plan()
                .withExecutionPlan(planAsString)
                .withSleepBetweenSteps(Integer.parseInt(sleepBetweenStepsField))
                .withMaxExecutionTime(Integer.parseInt(executionTimeField))

        return convertToJson(plan)
    }

    String convertToJson(Plan plan) {
        def executionLines = plan.getExecutionLines()
                .stream()
                .filter({ !it.startsWith("#") })
                .map({ "\"$it\"" })
                .collect(Collectors.toList())

        def json =
                """
            {
              "sleepBetweenSteps": ${plan.getSleepBetweenSteps()},
              "maxExecutionTimeSecs": ${plan.getMaxExecutionTimeSecs()},
              "executionLines": ${executionLines}
            }
        """

        return JsonOutput.prettyPrint(json)
    }
}
