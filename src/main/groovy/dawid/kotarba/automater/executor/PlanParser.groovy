package dawid.kotarba.automater.executor

import org.springframework.core.io.ClassPathResource

import java.nio.file.Path

final class PlanParser {

    static List<String> parseToExecutionLines(String executionPlan) {
        return executionPlan.tokenize('\n')
    }

    static List<String> parseToExecutionLines(Path filePath) {
        return filePath.toFile().readLines()
    }

    static List<String> parseToExecutionLines(ClassPathResource classPathResource) {
        return classPathResource.file.readLines()
    }
}
