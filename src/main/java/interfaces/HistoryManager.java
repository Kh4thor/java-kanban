package main.java.interfaces;

import java.util.List;
import java.util.Map;

import main.java.model.Task;
import main.java.utils.Node;

public interface HistoryManager {

	// добавить задачу в историю
	Integer addToHistory(Task task) throws Exception;

	// получить историю задач (без огрничений)
	Map<Integer, Node<Task>> getHistory() throws Exception;

	// получить Linked-список задач из истории
	List<Task> getTasks() throws Exception;

	// получить историю задач в обратном порядке (без ограничений)
	List<Task> getTasksReverse() throws Exception;

	// удадить задачу из истории задач
	Integer remove(int id);
}
