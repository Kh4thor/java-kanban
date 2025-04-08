package main.java;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.model.MainTask;
import main.java.model.SubTask;
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
		System.out.println("Maintask**");
		MainTask maintask = new MainTask("task-1", "description");

		int idmt = tm.addMainTask(maintask);
		maintask = tm.getMainTask(idmt).get();
		String jStrmt = gson.toJson(maintask);

		System.out.println(jStrmt);

		System.out.println();
		System.out.println("Subtask**");
		SubTask subtask1 = new SubTask("sTask-1", "descript", 1, LocalDateTime.now(), Duration.ofMinutes(15));

		String staskjson = gson.toJson(subtask1);
		System.out.println(staskjson);

		System.out.println();
		System.out.println("Task");
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
