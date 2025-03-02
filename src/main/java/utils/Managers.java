package main.java.utils;

import java.io.File;
import java.util.List;
import java.io.IOException;

import main.java.model.Task;
import main.java.interfaces.TaskManager;
import main.java.service.InMemoryTaskManager;
import main.java.service.FileBackedTaskManager;

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
}
