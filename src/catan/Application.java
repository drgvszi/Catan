package catan;

import catan.game.gameType.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@SpringBootApplication
public class Application {
	public static Map<String, Game> games = new HashMap<>();

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World!";
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class);

		/*
		try {
			ConnectivitySimulation connectivity = new ConnectivitySimulation();
			connectivity.simulation();
		} catch (InterruptedException | IOException exception) {
			exception.printStackTrace();
		}
		 */
	}
}
