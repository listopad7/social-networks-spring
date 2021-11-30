package hu.listopad.socialnetworksspring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
//@PropertySource("web-application.properties")
public class SocialNetworksSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialNetworksSpringApplication.class, args);
	}

}
