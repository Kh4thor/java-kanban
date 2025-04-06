package test;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import main.java.model.Task;
import main.java.model.SubTask;
import main.java.model.MainTask;
import main.java.model.TaskProgress;
import main.java.service.InMemoryTaskManager;
import main.java.service.InMemoryHistoryManager;

import org.junit.jupiter.api.Assertions;

public class InMemoryHistoryManagerTEST {

	InMemoryTaskManager tm = new InMemoryTaskManager();
	InMemoryHistoryManager hm = new InMemoryHistoryManager();

	/*
	 * добавить задачу в историю
	 */
	@Test
	void addToHistory_tasksNotNull_succes() throws Exception {
		Task task1 = new Task("Task-1", "Description", LocalDateTime.now(), Duration.ZERO);
		Assertions.assertNotNull(hm.addToHistory(task1));
	}

	@Test
	void addToHistory_tasksIsNull_failure() throws Exception {
		Task task1 = null;
		Assertions.assertTrue(hm.addToHistory(task1) == -1);
	}

	/*
	 * получить историю задач (последние 10 задач)
	 */
	@Test
	void getHistory_elevenTasksAdded_succes() throws Exception {
		Task task1 = new Task(1, "Task-1", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Task task2 = new Task(2, "Task-2", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Task task3 = new Task(3, "Task-3", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Task task4 = new Task(4, "Task-4", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Task task5 = new Task(5, "Task-5", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);

		hm.addToHistory(task1);
		hm.addToHistory(task2);
		hm.addToHistory(task3);
		hm.addToHistory(task4);
		hm.addToHistory(task5);

		MainTask maintask1 = new MainTask(6, "Maintask-1", "Description");
		MainTask maintask2 = new MainTask(7, "Maintask-2", "Description");
		MainTask maintask3 = new MainTask(8, "Maintask-3", "Description");

		hm.addToHistory(maintask1);
		hm.addToHistory(maintask2);
		hm.addToHistory(maintask3);

		SubTask subtask1 = new SubTask(9, "Subtask-1", "Description", 6, TaskProgress.NEW, LocalDateTime.now(),
				Duration.ZERO);
		SubTask subtask2 = new SubTask(10, "Subtask-2", "Description", 7, TaskProgress.NEW, LocalDateTime.now(),
				Duration.ZERO);
		SubTask subtask3 = new SubTask(11, "Subtask-3", "Description", 8, TaskProgress.NEW, LocalDateTime.now(),
				Duration.ZERO);

		hm.addToHistory(subtask1);
		hm.addToHistory(subtask2);
		hm.addToHistory(subtask3);

		List<Task> arr = hm.getHistory();
		Assertions.assertEquals(11, arr.size());
	}

	@Test
	void getHistory_sixDifferentTasksAdded_succes() throws Exception {
		Task task1 = new Task(1, "Task-1", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Task task2 = new Task(2, "Task-2", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);

		MainTask maintask1 = new MainTask(3, "Maintask-1", "Description");
		MainTask maintask2 = new MainTask(4, "Maintask-2", "Description");

		SubTask subtask1 = new SubTask(5, "Subtask-1", "Description", 3, TaskProgress.NEW, LocalDateTime.now(),
				Duration.ZERO);
		SubTask subtask2 = new SubTask(6, "Subtask-2", "Description", 4, TaskProgress.NEW, LocalDateTime.now(),
				Duration.ZERO);

		hm.addToHistory(task1);
		hm.addToHistory(task2);
		hm.addToHistory(maintask1);
		hm.addToHistory(maintask2);
		hm.addToHistory(subtask1);
		hm.addToHistory(subtask2);

		List<Task> arr = hm.getHistory();
		Assertions.assertTrue(arr.size() == 6);
	}

	/*
	 * добавить задачу в список исотрии задач (дублирование)
	 */
	@Test
	void addToHistory_removeOldDoubledTaskFromHistoryAndAddLast_succes() throws Exception {

		Task task1 = new Task(1, "Task-1", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		MainTask maintask1 = new MainTask(2, "Maintask-1", "Description");
		SubTask subtask1 = new SubTask(3, "Subtask-1", "Description", 2, TaskProgress.NEW, LocalDateTime.now(),
				Duration.ZERO);

		// дублирование задач в начале истории
		hm.addToHistory(task1);
		hm.addToHistory(task1);

		// дублирование задач в середине истории
		hm.addToHistory(maintask1);
		hm.addToHistory(maintask1);

		// дублирование задач в конце истории
		hm.addToHistory(subtask1);
		hm.addToHistory(subtask1);

		/*
		 * ПРЯМОЙ ПОРЯДОК
		 */

		// вывод параметов в прямом порядке
		List<Task> arr = hm.getHistory();

		// проверка листа на содержание задач в прямом порядке
		Assertions.assertTrue(arr.size() == 3);
		Assertions.assertEquals(task1, arr.get(0));
		Assertions.assertEquals(maintask1, arr.get(1));
		Assertions.assertEquals(subtask1, arr.get(2));

		/*
		 * ОБРАТНЫЙ ПОРЯДОК
		 */

		// вывод параметов в обратном порядке
		arr = hm.getHistoryReverse();

		// проверка листа на содержание задач в обратном порядке
		Assertions.assertEquals(3, arr.size());
		Assertions.assertEquals(task1, arr.get(2));
		Assertions.assertEquals(maintask1, arr.get(1));
		Assertions.assertEquals(subtask1, arr.get(0));

		// проверка удаления задач из списка обратного порядка
		Assertions.assertEquals(1, hm.remove(task1.getId()));
		Assertions.assertEquals(1, hm.remove(maintask1.getId()));
		Assertions.assertEquals(1, hm.remove(subtask1.getId()));
		// история задач очищена
		Assertions.assertEquals(0, hm.getHistory().size());

	}

	/*
	 * удаление задач из истории и двусвязного списка
	 */
	@Test
	void removeAll_deleteTasksFromHistoryByHashMap_succes() {
		Map<Integer, Task> map = new HashMap<>();

		Task task1 = new Task(1, "Task-1", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		MainTask maintask1 = new MainTask(2, "Maintask-1", "Description");
		SubTask subtask1 = new SubTask(3, "Subtask-1", "Description", 2, TaskProgress.NEW, LocalDateTime.now(),
				Duration.ZERO);

		// история задач пуста
		Assertions.assertEquals(0, hm.getHistory().size());

		map.put(task1.getId(), task1);
		map.put(maintask1.getId(), maintask1);
		map.put(subtask1.getId(), subtask1);

		hm.addToHistory(task1);
		hm.addToHistory(maintask1);
		hm.addToHistory(subtask1);

		// история задач заполнена
		Assertions.assertEquals(3, hm.getHistory().size());
		Assertions.assertEquals(task1, hm.getHistory().get(0));
		Assertions.assertEquals(maintask1, hm.getHistory().get(1));
		Assertions.assertEquals(subtask1, hm.getHistory().get(2));

		// удаление задач из истории по таблице
		hm.removeAll(map);

		// история задача очищена
		Assertions.assertEquals(0, hm.getHistory().size());
	}
}
