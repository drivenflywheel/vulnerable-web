package drivenflywheel.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@RestController
public class VulnerableWebApplication {

	public static final String tempDir = "/tmp";


	public static void main(String[] args) {
		SpringApplication.run(VulnerableWebApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(name = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@PostMapping("/writeMessage")
	public ResponseEntity<String> writeFile(@RequestBody MessageModel messageModel) {

		// let's do dangerous stuff

		String fullPath = messageModel.getBasePath() + "/" + messageModel.getFileName();

		try {
			String safePath = ensurePathIsSafe(tempDir, fullPath);
			Path outputPath = Paths.get(safePath);

			return ResponseEntity.ok("Let's pretend we wrote the message to " + outputPath);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	private String ensurePathIsSafe(String requiredBase, String filePath) {
		Path normalizedBasePath = Paths.get(requiredBase).normalize().toAbsolutePath();
		Path normalizedFilePath = Paths.get(filePath).normalize().toAbsolutePath();

		// append path separator to default traversal via a sibling directory with a similar name
		if (!normalizedFilePath.startsWith(normalizedBasePath.toString() + "/")) {
			throw new IllegalArgumentException("Normalized file path (\"" + filePath + "\") is outside the required base path (\""
			+ requiredBase + "\")");
		}
		return normalizedFilePath.toString();

	}

}
