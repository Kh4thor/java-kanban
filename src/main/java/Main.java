package main.java;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.model.Task;
import main.java.service.InMemoryTaskManager;
import main.java.utils.DurationAdapter;
import main.java.utils.LocalDateTimeAdapter;
import server.HttpTaskServer;

public class Main {

	public static void main(String[] args) throws IOException {
		InMemoryTaskManager tm = new InMemoryTaskManager();
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.registerTypeAdapter(Duration.class, new DurationAdapter()).serializeNulls().create();

		HttpTaskServer server = new HttpTaskServer();
		server.start();
		LocalDateTime ldt = LocalDateTime.now();
		System.out.println(ldt);

		Task task = new Task("task-1", "description");
		int id = tm.addTask(task);
		task = tm.getTask(id).get();
		System.out.println(task);

		System.out.println("***");

		String jStr = gson.toJson(task);
		System.out.println(jStr);
		System.out.println("***");

		task = gson.fromJson(jStr, Task.class);

		System.out.println(task);
	}
}
