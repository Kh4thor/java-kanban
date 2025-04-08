package main.java.utils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.interfaces.TaskManager;
import main.java.model.Task;
import main.java.service.FileBackedTaskManager;
import main.java.service.InMemoryTaskManager;

public final class Managers {

	static TaskManager inMemoryTaskManager = new InMemoryTaskManager();

	// вызов дефолтного менеджера задач
	public static TaskManager getDefault() {
		return inMemoryTaskManager;
	}

	// вызов истории задач дефолтного менеджера
	public static List<Task> getDefaultHistory() throws Exception {
		return inMemoryTaskManager.getHistory();
	}

	// вызов истории задач дефолтного менеджера в обратном порядке
	public static List<Task> getDefaultHistoryReverse() throws Exception {
		return inMemoryTaskManager.getHistoryReverse();
	}

	// вызов дефолтного менеджера задач для работы с дефолтным файлом
	public static TaskManager getDefaultFileBackedTaskManager() throws IOException {
		return FileBackedTaskManager.loadFromFile();
	}

	// вызов дефолтного менеджера задач для работы со сторонним файлом
	public static TaskManager getDefaultFileBackedTaskManager(File file) throws IOException {
		return FileBackedTaskManager.loadFromFile(file);
	}

	public static Gson getGson() {
		return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.registerTypeAdapter(Duration.class, new DurationAdapter()).serializeNulls().create();
	}
}
