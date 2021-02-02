package dawid.kotarba.automater.executor

import dawid.kotarba.automater.device.Mouse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class PlanExecutorTest extends Specification {

    @Autowired
    private PlanExecutor executor

    @Autowired
    private Mouse mouse

    def 'Should throw exception for an incorrect execution line'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('Incorrect line')

        when:
        executor.start(plan)

        then:
        thrown IllegalStateException
    }

    def 'Should throw exception for an incorrect execution line in the middle'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('MOUSE moveTo 100 100')
        plan.addExecutionLine('Incorrect line')
        plan.addExecutionLine('MOUSE moveTo 200 200')

        when:
        executor.start(plan)

        then:
        thrown IllegalStateException
    }

    def 'Should throw exception for an incorrect last execution line'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('MOUSE moveTo 100 100')
        plan.addExecutionLine('MOUSE moveTo 200 200')
        plan.addExecutionLine('Incorrect line')

        when:
        executor.start(plan)

        then:
        thrown IllegalStateException
    }

    def 'Should not throw exception for an incorrect but commented last execution line'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('MOUSE moveTo 100 100')
        plan.addExecutionLine('MOUSE moveTo 200 200')
        plan.addExecutionLine('# Incorrect commented line')

        when:
        executor.start(plan)

        then:
        notThrown IllegalStateException
    }

    def 'Should throw exception for an incorrect line - wrong patameter count'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('MOUSE moveTo 100 100')
        plan.addExecutionLine('MOUSE moveTo 200')

        when:
        executor.start(plan)

        then:
        thrown IllegalStateException
    }

    def 'Should throw exception for an incorrect line - wrong patameter type'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('MOUSE moveTo 100 100')
        plan.addExecutionLine('MOUSE moveTo 200 a')

        when:
        executor.start(plan)

        then:
        thrown IllegalStateException
    }

    def 'Should not throw exception for an incorrect but commented execution line'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('# Incorrect commented line')

        when:
        executor.start(plan)

        then:
        notThrown IllegalStateException
    }

    def 'Should execute a single line successfully'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('MOUSE moveTo 100 100')

        when:
        executor.start(plan)

        then:
        notThrown IllegalStateException
    }

    def 'Should execute multiple lines successfully'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('MOUSE moveTo 100 100')
        plan.addExecutionLine('MOUSE moveTo 200 200')

        when:
        executor.start(plan)

        then:
        notThrown IllegalStateException
    }

    def 'Should move mouse to a valid position'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('MOUSE moveTo 100 100')
        plan.addExecutionLine('MOUSE moveTo 200 300')

        when:
        executor.start(plan)

        then:
        notThrown IllegalStateException

        and:
        mouse.x == 200
        mouse.y == 300
    }

    def 'Should fill execution statistics'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('MOUSE moveTo 100 100')
        plan.addExecutionLine('MOUSE moveTo 200 300')
        plan.addExecutionLine('MOUSE moveTo 500 500')

        when:
        def statistics = executor.start(plan)

        then:
        notThrown IllegalStateException

        and:
        statistics.stepsExecuted == 3
        statistics.executionTime > 0
    }

    def 'Should fill execution statistics but skip commented lines as executed'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('MOUSE moveTo 100 100')
        plan.addExecutionLine('# MOUSE moveTo 200 300')
        plan.addExecutionLine('MOUSE moveTo 500 500')

        when:
        def statistics = executor.start(plan)

        then:
        notThrown IllegalStateException

        and:
        statistics.stepsExecuted == 2
        statistics.executionTime > 0
    }
}