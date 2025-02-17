package main.java;

import main.java.model.Task;
import main.java.model.SubTask;
import main.java.utils.Managers;
import main.java.model.MainTask;
import main.java.model.TaskProgress;
import main.java.interfaces.TaskManager;
import main.java.interfaces.HistoryManager;
import main.java.service.InMemoryHistoryManager;

public class Main {

	public static void main(String[] args) throws Exception {

		TaskManager tm = Managers.getDefault();
		HistoryManager hm = new InMemoryHistoryManager();

		System.out.println("-----------------------------------------------");
		System.out.println("ИСТОРИЯ ЗАДАЧ TASKMANAGER");
		System.out.println("-----------------------------------------------");

		Task task1 = new Task("Task-1", "Discription", TaskProgress.NEW);
		tm.addTask(task1);

		MainTask task2 = new MainTask("Task-2", "Discription");
		tm.addMainTask(task2);

		SubTask task3 = new SubTask("Task-3", "Discription", task2.getId(), TaskProgress.NEW);
		tm.addSubTask(task3);

		// дублирование задач в начале истории
		tm.getTask(task1.getId());
		tm.getTask(task1.getId());

		// дублирование задач в середине истории
		tm.getMainTask(task2.getId());
		tm.getMainTask(task2.getId());

		// дублирование задач в конце истории
		tm.getSubTask(task3.getId());
		tm.getSubTask(task3.getId());

		System.out.println("Прямая последовательность");
		System.out.println(tm.getTasks().size());
		tm.getTasks().forEach(task -> System.out.println(task));

		System.out.println("\nОбратная последовательность");
		System.out.println(tm.getTasks().size());
		tm.getTasksReverse().forEach(task -> System.out.println(task));

		System.out.println();
		System.out.println("-----------------------------------------------");
		System.out.println("ИСТОРИЯ ЗАДАЧ HISTORYMANGER");
		System.out.println("-----------------------------------------------");

		hm.addToHistory(task1);
		hm.addToHistory(task2);
		hm.addToHistory(task3);

		System.out.println("\nОбратная последовательность");
		System.out.println(hm.getTasks().size());
		hm.getTasks().forEach(task -> System.out.println(task));

	}
}
