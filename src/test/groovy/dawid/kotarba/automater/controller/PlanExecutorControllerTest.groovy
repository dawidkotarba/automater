package dawid.kotarba.automater.controller

import dawid.kotarba.automater.device.Mouse
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

    def 'Should execute a plan by a REST call'() {
        given:
        def plan = """
                    {
                        "sleepBetweenSteps": 10,
                        "executionLines": [
                            "MOUSE moveTo 100 100",
                            "MOUSE moveTo 200 200",
                            "MOUSE moveTo 300 300"
                        ]
                    }
                   """

        mouse.moveTo(0, 0)

        expect:
        mockMvc.perform(post("/start")
                .content(plan)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())

        and:
        mouse.x == 300
        mouse.y == 300
    }
}