package dawid.kotarba.automater.configuration

import org.springframework.context.annotation.Configuration

import javax.annotation.PostConstruct

@Configuration
class AutomaterConfiguration {

    @PostConstruct
    void init() {
        System.setProperty('java.awt.headless', 'false')
    }
}
