package net.bitnine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableAutoConfiguration
public class CypherTestApplication {

    @RequestMapping("/")
    String hello() {
        return "hello world";
    }

	public static void main(String[] args) {
		SpringApplication.run(CypherTestApplication.class, args);
	}
}
