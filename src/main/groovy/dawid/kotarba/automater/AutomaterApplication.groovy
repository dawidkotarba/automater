package dawid.kotarba.automater

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class AutomaterApplication {

    static void main(String[] args) {
        SpringApplication.run(AutomaterApplication, args)
    }
}
