package dawid.kotarba.automater.model

import dawid.kotarba.automater.service.executor.Plan
import groovy.json.JsonSlurper
import spock.lang.Specification

class PlanTest extends Specification {

    def 'Should convert an empty plan to JSON'() {
        given:
        def plan = new Plan()

        when:
        def planAsJson = plan.toJson()

        then:
        def expected = """
            {
            "sleepBetweenSteps": 100,
            "maxExecutionTimeSecs": 100,
            "executionLines": [
            ]
            }
        """

        assertPlansAreEqual(expected, planAsJson)
    }

    def 'Should convert a plan to JSON'() {
        given:
        def plan = new Plan()
        plan.withSleepBetweenSteps(10)
        plan.withMaxExecutionTime(60)
        plan.addExecutionLine('MOUSE moveTo 100 100')
        plan.addExecutionLine('MOUSE moveTo 200 200')

        when:
        def planAsJson = plan.toJson()

        then:
        def expected = """
            {
            "sleepBetweenSteps": 10,
            "maxExecutionTimeSecs": 60,
            "executionLines": [
                "MOUSE moveTo 100 100",
                "MOUSE moveTo 200 200"
            ]
            }
        """

        assertPlansAreEqual(expected, planAsJson)
    }

    def 'Should not include commented line into JSON'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('MOUSE moveTo 100 100')
        plan.addExecutionLine('# MOUSE moveTo 300 300')
        plan.addExecutionLine('MOUSE moveTo 200 200')

        when:
        def planAsJson = plan.toJson()

        then:
        def expected = """
            {
            "sleepBetweenSteps": 100,
            "maxExecutionTimeSecs": 100,
            "executionLines": [
                "MOUSE moveTo 100 100",
                "MOUSE moveTo 200 200"
            ]
            }
        """

        assertPlansAreEqual(expected, planAsJson)
    }

    private static void assertPlansAreEqual(String expected, String actual) {
        def jsonSlurper = new JsonSlurper()
        assert jsonSlurper.parseText(expected) == jsonSlurper.parseText(actual)
    }
}
