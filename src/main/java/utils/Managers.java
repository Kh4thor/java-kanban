package main.java.utils;

import java.util.List;

import main.java.interfaces.TaskManager;
import main.java.model.Task;
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
}
