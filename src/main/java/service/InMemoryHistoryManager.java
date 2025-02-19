package main.java.service;

import java.util.Map;

import main.java.interfaces.HistoryManager;
import main.java.model.MainTask;
import main.java.model.SubTask;
import main.java.model.Task;
import main.java.model.TaskProgress;
import main.java.utils.Node;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

	private Node<Task> head;
	private Node<Task> tail;

	Map<Integer, Node<Task>> nodeMap = new HashMap<>();

	/*
	 * добавить задачу в историю
	 */
	@Override
	public Integer addToHistory(Task task) {
		if (task != null) {
			// получить параметры
			int id = task.getId();
			String name = task.getName();
			String discription = task.getDiscription();
			TaskProgress progress = task.getTaskProgress();
			Task snapShotTask = task;

			// сделать слепок объекта класса типа Task
			if (task.getClass() == Task.class) {
				snapShotTask = new Task(id, name, discription, progress);

				// сделать слепок объекта класса типа MainTask
			} else if (task.getClass() == MainTask.class) {
				snapShotTask = new MainTask(id, name, discription);

				// сделать слепок объекта класса типа SubTask
			} else if (task.getClass() == SubTask.class) {
				int maintaskId = ((SubTask) task).getMaintaskId();
				snapShotTask = new SubTask(id, name, discription, maintaskId, progress);
			}
			// записать слепок в качестве последнего узла в двусвязный список
			Node<Task> lastNode = linkLast(snapShotTask);

			// добавить узел в хранилище
			nodeMap.put(id, lastNode);
			return 1;
		}
		return -1;
	}

	/*
	 * получить историю задач (без ограничений)
	 */
	@Override
	public List<Task> getHistory() {
		List<Task> arr = new ArrayList<>();
		// если двусвязный список не null
		if (head != null) {
			Node<Task> currentNode = head;
			// итерация по двусвязному списку и запись каждого значения узла в список
			// истории задач
			while (currentNode != null) {
				arr.add(currentNode.data);
				currentNode = currentNode.next;
			}
		}
		return arr;
	}

	/*
	 * получить историю задач (без огрничений) в обратном порядке
	 */
	public List<Task> getHistoryReverse() {
		List<Task> arr = new ArrayList<>();
		// если двусвязный список не null
		if (tail != null) {
			Node<Task> currentNode = tail;
			// итерация по двусвязному списку и запись каждого значения узла в список
			// истории задач
			while (currentNode != null) {
				arr.add(currentNode.data);
				currentNode = currentNode.prev;
			}

		}
		return arr;
	}

	/*
	 * удадить задачу из истории задач
	 */
	@Override
	public Integer remove(int id) {
		if (id < 1) {
			return -1;
		}
		Node<Task> node = nodeMap.remove(id);
		return removeNode(node);
	}

	/*
	 * удалить задачи из исторрии по списку
	 */
	@Override
	public <T extends Task> Integer removeAll(Map<Integer, T> taskMap) {
		if (taskMap == null) {
			return -1;
		}
		for (int id : taskMap.keySet()) {
			remove(id);
		}
		return 1;
	}

	/*
	 * добавить узел в конец двусвязного списка
	 */
	private Node<Task> linkLast(Task newTask) {
		// если newTask уже есть в истории...
		if (nodeMap.containsKey(newTask.getId())) {
			// ... удаляем узел из хранилища истории

			// удаление объекта из коллекции
			Node<Task> node = nodeMap.remove(newTask.getId());
			// удаление объекта из коллекции
			removeNode(node);
		}
		// привязка узла в конец двусвязного списка
		int id = newTask.getId();
		Node<Task> oldTail = tail;
		Node<Task> lastNode = new Node<Task>(tail, newTask, null);
		// запись нового узла в хранилище историй
		nodeMap.put(id, lastNode);
		tail = lastNode;
		// если двусвязный список пустая
		if (oldTail == null) {
			head = lastNode;
			// если двусвязный список не пустой
		} else {
			oldTail.next = lastNode;
		}
		return lastNode;
	}

	/*
	 * удаление узла из двусвязного списка
	 */
	private Integer removeNode(Node<Task> node) {
		if (node == null) {
			return -1;
		}
		if (head == node && tail == node) {
			head = null;
			tail = null;
		} else {
			if (node.prev != null) {
				node.prev.next = node.next;
			} else {
				head = node.next;
			}
			if (node.next != null) {
				node.next.prev = node.prev;
			} else {
				tail = node.prev;
			}
		}
		return 1;
	}
}
