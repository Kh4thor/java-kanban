package test;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.List;
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
		Task task1 = new Task("Task-1", "Discription", TaskProgress.NEW);
//		Node<Task> node = hm.addToHistory(task1);

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
		Task task1 = new Task("Task-1", "Discription", TaskProgress.NEW);
		Task task2 = new Task("Task-2", "Discription", TaskProgress.NEW);
		Task task3 = new Task("Task-3", "Discription", TaskProgress.NEW);
		Task task4 = new Task("Task-4", "Discription", TaskProgress.NEW);
		Task task5 = new Task("Task-5", "Discription", TaskProgress.NEW);
		tm.addTask(task1);
		tm.addTask(task2);
		tm.addTask(task3);
		tm.addTask(task4);
		tm.addTask(task5);

		MainTask maintask1 = new MainTask("Maintask-1", "Discription");
		MainTask maintask2 = new MainTask("Maintask-2", "Discription");
		MainTask maintask3 = new MainTask("Maintask-3", "Discription");
		int maintaskId1 = tm.addMainTask(maintask1);
		int maintaskId2 = tm.addMainTask(maintask2);
		int maintaskId3 = tm.addMainTask(maintask3);

		SubTask subtask1 = new SubTask("Subtask-1", "Discription", maintaskId1, TaskProgress.NEW);
		SubTask subtask2 = new SubTask("Subtask-2", "Discription", maintaskId2, TaskProgress.NEW);
		SubTask subtask3 = new SubTask("Subtask-3", "Discription", maintaskId3, TaskProgress.NEW);
		tm.addSubTask(subtask1);
		tm.addSubTask(subtask2);
		tm.addSubTask(subtask3);

		tm.getTask(task1.getId());
		tm.getTask(task2.getId());
		tm.getTask(task3.getId());
		tm.getTask(task4.getId());
		tm.getTask(task5.getId());
		tm.getMainTask(maintask1.getId());
		tm.getMainTask(maintask2.getId());
		tm.getMainTask(maintask3.getId());
		tm.getSubTask(subtask1.getId());
		tm.getSubTask(subtask2.getId());
		tm.getSubTask(subtask3.getId());

		List<Task> arr = tm.getHistory();
		Assertions.assertEquals(11, arr.size());
	}

	@Test
	void getHistory_sixDifferentTasksAdded_succes() throws Exception {
		Task task1 = new Task("Task-1", "Discription", TaskProgress.NEW);
		Task task2 = new Task("Task-2", "Discription", TaskProgress.NEW);
		tm.addTask(task1);
		tm.addTask(task2);

		MainTask maintask1 = new MainTask("Maintask-1", "Discription");
		MainTask maintask2 = new MainTask("Maintask-2", "Discription");
		int maintaskId1 = tm.addMainTask(maintask1);
		int maintaskId2 = tm.addMainTask(maintask2);

		SubTask subtask1 = new SubTask("Subtask-1", "Discription", maintaskId1, TaskProgress.NEW);
		SubTask subtask2 = new SubTask("Subtask-2", "Discription", maintaskId2, TaskProgress.NEW);
		tm.addSubTask(subtask1);
		tm.addSubTask(subtask2);

		tm.getTask(task1.getId());
		tm.getTask(task2.getId());
		tm.getMainTask(maintask1.getId());
		tm.getMainTask(maintask2.getId());
		tm.getSubTask(subtask1.getId());
		tm.getSubTask(subtask2.getId());

		List<Task> arr = tm.getHistory();

		Assertions.assertTrue(arr.size() == 6);
	}

	/*
	 * добавить задачу в список исотрии задач (дублирование)
	 */
	@Test
	void addToHistory_removeOldDoubledTaskFromHistoryAndAddLast_succes() throws Exception {

		Task task1 = new Task("Task-1", "Discription", TaskProgress.NEW);
		tm.addTask(task1);

		MainTask maintask1 = new MainTask("Maintask-1", "Discription");
		tm.addMainTask(maintask1);

		SubTask subtask1 = new SubTask("Subtask-1", "Discription", maintask1.getId(), TaskProgress.NEW);
		tm.addSubTask(subtask1);

		// дублирование задач в начале истории
		tm.getTask(task1.getId());
		tm.getTask(task1.getId());

		// дублирование задач в середине истории
		tm.getMainTask(maintask1.getId());
		tm.getMainTask(maintask1.getId());

		// дублирование задач в конце истории
		tm.getSubTask(subtask1.getId());
		tm.getSubTask(subtask1.getId());

		// вывод параметов в прямом порядке
		List<Task> arr = tm.getHistory();

		Assertions.assertTrue(arr.size() == 3);
		Assertions.assertEquals(task1, arr.get(0));
		Assertions.assertEquals(maintask1, arr.get(1));
		Assertions.assertEquals(subtask1, arr.get(2));

		// вывод параметов в обратном порядке
		arr = tm.getHistoryReverse();

		// проверка листа на содержание задач в обратном порядке
		Assertions.assertEquals(3, arr.size());
		Assertions.assertEquals(task1, arr.get(2));
		Assertions.assertEquals(maintask1, arr.get(1));
		Assertions.assertEquals(subtask1, arr.get(0));
	}

	/*
	 * удаление задачи, главной задачи и подзадачи из истории и двусвязного списка
	 */
	@Test
	void remove_deleteTaskFromHistory_succes() throws Exception {
		Task task1 = new Task("Task-1", "Discription", TaskProgress.NEW);
		tm.addTask(task1);
		MainTask maintask1 = new MainTask("Maintask-1", "Discription");
		tm.addMainTask(maintask1);
		SubTask subtask1 = new SubTask("Subtask-1", "Discription", maintask1.getId(), TaskProgress.NEW);
		tm.addSubTask(subtask1);

		tm.getTask(task1.getId());
		tm.getMainTask(maintask1.getId());
		tm.getSubTask(subtask1.getId());

		List<Task> arr = tm.getHistory();
		Assertions.assertTrue(arr.size() == 3);

		// история задач пуста
		Assertions.assertEquals(0, hm.getHistory().size());

		// добавление задач в историю

		// дублирование в начале списка
		hm.addToHistory(task1);
		hm.addToHistory(task1);
		// дублирование в середине списка
		hm.addToHistory(maintask1);
		hm.addToHistory(maintask1);

		// дублирование в конце списка
		hm.addToHistory(subtask1);
		hm.addToHistory(subtask1);

		// история задач заполенена, дубли не записаны
		Assertions.assertEquals(3, hm.getHistory().size());
		Assertions.assertEquals(task1, hm.getHistory().get(0));
		Assertions.assertEquals(maintask1, hm.getHistory().get(1));
		Assertions.assertEquals(subtask1, hm.getHistory().get(2));

		// проверка удаления задачи
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

		Task task1 = new Task("Task-1", "Discription", TaskProgress.NEW);
		tm.addTask(task1);
		MainTask maintask1 = new MainTask("Maintask-1", "Discription");
		tm.addMainTask(maintask1);
		SubTask subtask1 = new SubTask("Subtask-1", "Discription", maintask1.getId(), TaskProgress.NEW);
		tm.addSubTask(subtask1);

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
