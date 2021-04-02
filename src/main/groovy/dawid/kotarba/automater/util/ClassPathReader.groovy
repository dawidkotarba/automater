package dawid.kotarba.automater.util

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

import java.util.stream.Collectors

final class ClassPathReader {
    private ClassPathReader() {}

    static String readAsString(String filename) {
        Resource resource = new ClassPathResource(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        reader.lines().collect(Collectors.joining(System.lineSeparator()))
    }
}
