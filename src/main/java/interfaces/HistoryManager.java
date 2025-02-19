package main.java.interfaces;

import java.util.List;
import java.util.Map;

import main.java.model.Task;

public interface HistoryManager {

	// добавить задачу в историю
	Integer addToHistory(Task task);

	// получить историю задач (без ограничений)
	List<Task> getHistory();

	// получить историю задач в обратном порядке (без ограничений)
	List<Task> getHistoryReverse();

	// удадить задачу из истории задач
	Integer remove(int id);

	// удалить задачи из истории по таблице
	<T extends Task> Integer removeAll(Map<Integer, T> taskList);
}
