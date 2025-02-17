package main.java.interfaces;

import java.util.Map;
import java.util.List;

import main.java.utils.Node;
import main.java.model.Task;
import main.java.model.SubTask;
import main.java.model.MainTask;

public interface TaskManager {

	// получить список узлов задач
	Map<Integer, Node<Task>> getHistory() throws Exception;

	// создать задачу
	Integer addTask(Task task);

	// создать главную задачу
	Integer addMainTask(MainTask maintask);

	// создать подзадачу
	Integer addSubTask(SubTask subtask);

	// обновить задачу
	Integer updateTask(Task newTask);

	// обновить главную задачу
	Integer updateMainTask(MainTask newMaintask);

	// обновить подзадачу
	Integer updateSubTask(SubTask newSubtask);

	// получить задачу по id
	Task getTask(int taskId) throws Exception;

	// получить главную задачу по id
	MainTask getMainTask(int maintaskId) throws Exception;

	// получить подзадачу по id-подзадачи
	SubTask getSubTask(int id) throws Exception;

	// получить список всех задач
	List<Task> getTasksList();

	// полчучить список всех главных задач
	List<MainTask> getMainTasksList();

	// получить список всех подзадач
	List<SubTask> getSubTasksList();

	// получить список подзадач по главной задаче
	List<SubTask> getSubTaskListByMainTask(int maintaskId);

	// удалить задачу по id
	Integer deleteTaskById(int taskId);

	// удалить главную задачу по id
	Integer deleteMainTaskById(int maintaskId);

	// удалить подзадачу по id
	Integer deleteSubTaskById(int sTaskId);

	// удалить все задачи из taskMap
	Integer deleteAllTasks() throws Exception;

	// удалить все главные задачи
	Integer deleteAllMainTasks() throws Exception;

	// удалить все подзадачи для всех главных задач
	Integer deleteAllSubTasks() throws Exception;

	// очистить все хранилища
	Integer clearAllDepos() throws Exception;

	// получить список из истории задач в прямом порядке
	List<Task> getTasks() throws Exception;

	// получить список из истории задач в обратном порядке
	List<Task> getTasksReverse() throws Exception;

}