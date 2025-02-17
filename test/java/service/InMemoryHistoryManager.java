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
	public Integer addToHistory(Task task) throws Exception {
		if (task != null) {
			// получить параметры
			int id = task.getId();
			String name = task.getName();
			String discription = task.getDiscription();
			TaskProgress progress = task.getTaskProgress();

			// если task - Task, делаем snapshot, добавляем его в связку и записываем в
			// хранилище историй
			if (task.getClass() == Task.class) {
				Task newTask = new Task(id, name, discription, progress);
				Node<Task> lastNode = linkLast(newTask);
				nodeMap.put(id, lastNode);
				return 1;

				// если task - Maintask, делаем snapshot, добавляем его в связку и записываем в
				// хранилище историй
			} else if (task.getClass() == MainTask.class) {
				MainTask newMaintask = new MainTask(id, name, discription);
				Node<Task> lastNode = linkLast(newMaintask);
				nodeMap.put(id, lastNode);
				return 1;

				// если task - Subtask, делаем snapshot, добавляем его в связку и записываем в
				// хранилище историй
			} else if (task.getClass() == SubTask.class) {
				int maintaskId = ((SubTask) task).getMaintaskId();
				SubTask newSubtask = new SubTask(id, name, discription, maintaskId, progress);
				Node<Task> lastNode = linkLast(newSubtask);
				nodeMap.put(id, lastNode);
				return 1;
			}
		}
		return -1;
	}

	/*
	 * получить хранилище узлов истории задач
	 */
	@Override
	public Map<Integer, Node<Task>> getHistory() {
		return new HashMap<Integer, Node<Task>>(nodeMap);
	}

	/*
	 * получить историю задач в прямом порядке
	 */
	@Override
	public List<Task> getTasks() throws Exception {
		List<Task> arr = new ArrayList<>();
		// если связка не null
		if (head != null) {
			Node<Task> currentNode = head;
			// итерация по связке и запись каждого значения узла в список
			while (currentNode != null) {
				arr.add(currentNode.data);
				currentNode = currentNode.next;
			}
			return arr;
		}
		throw new Exception("getTaskListNotFoundException");
	}

	/*
	 * получить историю задач в обратном порядке
	 */
	public List<Task> getTasksReverse() throws Exception {
		List<Task> arr = new ArrayList<>();
		// если связка не null
		if (tail != null) {
			Node<Task> currentNode = tail;
			// итерация по связке и запись каждого значения узла в список
			while (currentNode != null) {
				arr.add(currentNode.data);
				currentNode = currentNode.prev;
			}
			return arr;
		}
		throw new Exception("getTaskListReverseNotFoundException");
	}

	@Override
	public Integer remove(int id) {
		if (id > 0) {
			Node<Task> node = nodeMap.remove(id);
			return removeNode(node);
		}
		return -1;
	}

	/*
	 * добавить узел в конец связки
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
		// привязка узла в конец связки
		int id = newTask.getId();
		Node<Task> oldTail = tail;
		Node<Task> lastNode = new Node<Task>(tail, newTask, null);
		// запись нового узла в хранилище историй
		nodeMap.put(id, lastNode);
		tail = lastNode;
		tail.prev = oldTail;
		// если связка пустая
		if (oldTail == null) {
			head = lastNode;
			return lastNode;
			// если связка не пустая
		} else
			oldTail.next = lastNode;
		return lastNode;
	}

	/*
	 * удаление узла из связки и объекта из коллекции
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
