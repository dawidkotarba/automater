package dawid.kotarba.automater.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dawid.kotarba.automater.device.Mouse
import dawid.kotarba.automater.executor.Plan
import dawid.kotarba.automater.executor.PlanExecutor
import dawid.kotarba.automater.executor.PlanStatistics
import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class PlanExecutorControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private Mouse mouse

    @Autowired
    private PlanExecutor planExecutor

    @Autowired
    private ObjectMapper objectMapper

    def 'Should execute a plan by a REST call'() {
        given:
        def plan = """
                    {
                        "sleepBetweenSteps": 10,
                        "maxExecutionTimeSecs": 100,
                        "executionLines": [
                            "MOUSE moveTo 100 100",
                            "MOUSE moveTo 200 200",
                            "MOUSE moveTo 300 300"
                        ]
                    }
                   """

        mouse.moveTo(0, 0)

        expect:
        def result = mockMvc.perform(post("/start")
                .content(plan)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()

        and:
        mouse.x == 300
        mouse.y == 300

        and:
        String contentAsString = result.getResponse().getContentAsString()
        PlanStatistics planStatistics = objectMapper.readValue(contentAsString, PlanStatistics.class)

        planStatistics.executionTime > 0
    }

    def 'Should not start a second plan by a REST call'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('SLEEP of 20000')

        new Thread({
            planExecutor.start(plan)
        }).start()

        while (!planExecutor.isStarted()) {
            sleep(100)
        }

        assert planExecutor.isStarted()

        expect:
        mockMvc.perform(post("/start")
                .content(new JsonBuilder(plan).toString())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())

        cleanup:
        mockMvc.perform(post("/stop")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
    }

    def 'Should stop a plan by a REST call'() {
        given:
        def plan = new Plan()
        plan.addExecutionLine('SLEEP of 20000')

        new Thread({
            planExecutor.start(plan)
        }).start()

        while (!planExecutor.isStarted()) {
            sleep(100)
        }

        assert planExecutor.isStarted()

        when:
        mockMvc.perform(post("/stop")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())

        then:
        !planExecutor.isStarted()
    }
}