package test.java;

import org.junit.jupiter.api.Test;

import main.java.model.Task;
import main.java.model.MainTask;
import main.java.model.SubTask;
import main.java.model.TaskProgress;
import main.java.service.InMemoryHistoryManager;
import main.java.service.InMemoryTaskManager;
import main.java.utils.Node;

import java.util.List;
import java.util.Map;

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

		List<Task> arr = tm.getTasks();
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

		List<Task> arr = tm.getTasks();

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
		List<Task> arr = tm.getTasks();

		Assertions.assertTrue(arr.size() == 3);
		Assertions.assertEquals(task1, arr.get(0));
		Assertions.assertEquals(maintask1, arr.get(1));
		Assertions.assertEquals(subtask1, arr.get(2));

		// вывод параметов в обратном порядке
		arr = tm.getTasksReverse();

		Assertions.assertTrue(arr.size() == 3);
		Assertions.assertEquals(task1, arr.get(2));
		Assertions.assertEquals(maintask1, arr.get(1));
		Assertions.assertEquals(subtask1, arr.get(0));
	}

	/*
	 * удаление узла из связки
	 */
	@Test
	void removeNode_nodeNotNull_succes() throws Exception {
		Task task1 = new Task(1, "Task-1", "Discription", TaskProgress.NEW);
		hm.addToHistory(task1);
		int id = task1.getId();
		Map<Integer, Node<Task>> nodeMap = hm.getHistory();
		Node<Task> node = nodeMap.get(id);
		System.out.println(node.data);

		Assertions.assertEquals(task1, node.data);
		Assertions.assertEquals(1, hm.remove(id));
	}

	/*
	 * удаление всех подзадач главной задачи после ее удаления
	 */
	@Test
	void removeNode_deleteSubtasksWhenMainTaskDeleted_succes() throws Exception {
		Task task1 = new Task("Task-1", "Discription", TaskProgress.NEW);
		tm.addTask(task1);
		MainTask maintask1 = new MainTask("Maintask-1", "Discription");
		tm.addMainTask(maintask1);
		SubTask subtask1 = new SubTask("Subtask-1", "Discription", maintask1.getId(), TaskProgress.NEW);
		tm.addSubTask(subtask1);

		hm.addToHistory(task1);
		hm.addToHistory(maintask1);
		hm.addToHistory(subtask1);

		Assertions.assertEquals(1, hm.remove(maintask1.getId()));

		Map<Integer, Node<Task>> arr = tm.getHistory();
		Assertions.assertTrue(arr.size() == 0);
	}
}
