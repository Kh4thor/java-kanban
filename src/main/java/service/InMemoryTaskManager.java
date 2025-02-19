package main.java.service;

import java.util.Map;

import main.java.model.Task;
import main.java.model.SubTask;
import main.java.model.MainTask;
import main.java.model.TaskProgress;
import main.java.interfaces.TaskManager;
import main.java.interfaces.HistoryManager;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {

	private int id = 1;

	private HistoryManager historyManager = new InMemoryHistoryManager();

	private Map<Integer, Task> taskMap = new HashMap<>();
	private Map<Integer, MainTask> mainTaskMap = new HashMap<>();
	// каждое хранилище с подзадачами хранится в своей главной задаче

	/*
	 * получить историю просмотра задач (без ограничений)
	 */

	@Override
	public List<Task> getHistory() {
		return new ArrayList<Task>(historyManager.getHistory());
	}

	/*
	 * получить историю просмотра задач в обратном порядке
	 */
	@Override
	public List<Task> getHistoryReverse() {
		return historyManager.getHistoryReverse();
	}

	/*
	 * добавить задачу
	 */
	@Override
	public Integer addTask(Task task) {
		boolean condition = task != null && task.getClass() == Task.class && task != null
				&& !taskMap.containsKey(task.getId()) && task.getId() == 0;
		if (condition) {
			task.setId(id++);
			int taskId = task.getId();
			String taskName = task.getName();
			if (!taskName.isBlank() && !taskName.isEmpty()) {
				taskMap.put(taskId, task);
				return taskId;
			}
		}
		return -1;
	}

	/*
	 * добавить главную задачу
	 */
	@Override
	public Integer addMainTask(MainTask mainTask) {
		if (mainTask != null && mainTask.getId() == 0 && !mainTaskMap.containsKey(mainTask.getId())) {
			mainTask.setId(id++);
			int mainTaskId = mainTask.getId();
			String mainTaskName = mainTask.getName();
			if (!mainTaskName.isBlank() && !mainTaskName.isEmpty()) {
				mainTaskMap.put(mainTask.getId(), mainTask);
				return mainTaskId;
			}
		}
		return -1;
	}

	/*
	 * добавить подзадачу
	 */
	@Override
	public Integer addSubTask(SubTask subtask) {
		if (subtask != null && subtask.getId() == 0) {
			int mainTaskId = subtask.getMaintaskId();
			if (mainTaskMap.containsKey(mainTaskId)) {
				subtask.setId(id++);
				int subTaskId = subtask.getId();
				MainTask mainTask = mainTaskMap.get(mainTaskId);
				String subtaskName = subtask.getName();
				if (!subtaskName.isBlank() && !subtaskName.isEmpty()) {
					mainTask.addSubTaskToDepo(subtask);
					checkTaskProgress(mainTask);
					return subTaskId;
				}
			}
		}
		return -1;
	}

	/*
	 * обновить задачу
	 */
	@Override
	public Integer updateTask(Task newTask) {
		if (newTask != null) {
			if (newTask.getClass() == Task.class && newTask.getId() > 0 && taskMap.containsKey(newTask.getId())) {
				if (!newTask.getName().isEmpty() && !newTask.getName().isBlank()) {
					if (newTask.getTaskProgress() != null)
						taskMap.put(newTask.getId(), newTask);
					return newTask.getId();
				}
			}
		}
		return -1;
	}

	/*
	 * обновить главную задачу
	 */
	@Override
	public Integer updateMainTask(MainTask newMainTask) {
		if (newMainTask != null && mainTaskMap.containsKey(newMainTask.getId())) {
			String newName = newMainTask.getName();
			if (!newName.isEmpty() && !newName.isBlank()) {
				int mainTaskId = newMainTask.getId();
				MainTask mainTask = mainTaskMap.get(mainTaskId);
				String newDiscription = newMainTask.getDiscription();
				mainTask.setName(newName);
				mainTask.setDiscription(newDiscription);
				return newMainTask.getId();
			}
		}
		return -1;
	}

	/*
	 * обновить подзадачу
	 */
	@Override
	public Integer updateSubTask(SubTask newSubtask) {
		if (newSubtask != null) {
			int mainTaskId = newSubtask.getMaintaskId();
			if (mainTaskMap.containsKey(mainTaskId)) {
				MainTask mainTask = mainTaskMap.get(mainTaskId);
				if (mainTask != null) {
					Map<Integer, SubTask> subTaskMap = mainTask.getSubTaskMap();
					if (subTaskMap != null && subTaskMap.containsKey(newSubtask.getId())) {
						if (!newSubtask.getName().isBlank() && !newSubtask.getName().isEmpty()) {
							subTaskMap.put(newSubtask.getId(), newSubtask);
							checkTaskProgress(mainTask);
							return newSubtask.getId();
						}
					}
				}
			}
		}
		return -1;
	}

	/*
	 * получить задачу по id
	 */
	@Override
	public Task getTask(int taskId) {
		if (taskId > 0 && taskMap.containsKey(taskId)) {
			Task task = taskMap.get(taskId);
			historyManager.addToHistory(task);
			return task;
		}
		return new Task(-1, "null", "fromGetTask", TaskProgress.DONE);
	}

	/*
	 * получить главную задачу по id
	 */
	@Override
	public MainTask getMainTask(int maintaskId) {
		if (maintaskId > 0 && mainTaskMap.containsKey(maintaskId)) {
			MainTask mainTask = mainTaskMap.get(maintaskId);
			historyManager.addToHistory(mainTask);
			return mainTask;
		}
		return new MainTask(-1, "null", "fromGetMainTask");
	}

	/*
	 * получить подзадачу по id-подзадачи
	 */
	@Override
	public SubTask getSubTask(int id) {
		if (id > 0) {
			for (int mainTaskId : mainTaskMap.keySet()) {
				MainTask mainTask = mainTaskMap.get(mainTaskId);
				Map<Integer, SubTask> subtaskMap = mainTask.getSubTaskMap();
				if (subtaskMap.containsKey(id)) {
					for (int subTaskId : subtaskMap.keySet()) {
						SubTask subTask = subtaskMap.get(subTaskId);
						if (subTaskId == id) {
							historyManager.addToHistory(subTask);
							return subTask;
						}
					}
				}
			}
		}
		return new SubTask(-1, "null", "fromGetSubTask", -1, TaskProgress.NEW);
	}

	/*
	 * получить список всех задач
	 */
	@Override
	public List<Task> getTasksList() {
		List<Task> allTaskList = new ArrayList<>();
		if (taskMap != null) {
			for (int taskId : taskMap.keySet()) {
				Task task = taskMap.get(taskId);
				allTaskList.add(task);
			}
		}
		return allTaskList;
	}

	/*
	 * полчучить список всех главных задач
	 */
	@Override
	public List<Task> getMainTasksList() {
		List<Task> allMainTasksList = new ArrayList<>();
		if (mainTaskMap != null) {
			for (int mainTaskId : mainTaskMap.keySet()) {
				MainTask mainTask = mainTaskMap.get(mainTaskId);
				allMainTasksList.add(mainTask);
			}
		}
		return allMainTasksList;
	}

	/*
	 * получить список всех подзадач
	 */
	@Override
	public List<SubTask> getSubTasksList() {
		List<SubTask> allSubtasksList = new ArrayList<>();
		for (int mainTaskId : mainTaskMap.keySet()) {
			MainTask mainTask = mainTaskMap.get(mainTaskId);
			Map<Integer, SubTask> subTaskMap = mainTask.getSubTaskMap();
			if (subTaskMap != null) {
				for (int subTaskId : subTaskMap.keySet()) {
					SubTask subTask = subTaskMap.get(subTaskId);
					allSubtasksList.add(subTask);
				}
			}
		}
		return allSubtasksList;
	}

	/*
	 * получить список подзадач по главной задаче
	 */
	@Override
	public List<SubTask> getSubTaskListByMainTask(int mainTaskId) {
		List<SubTask> subTasksListByMaintask = new ArrayList<SubTask>();
		MainTask mainTask = mainTaskMap.get(mainTaskId);
		if (mainTask != null) {
			Map<Integer, SubTask> subTaskMap = mainTask.getSubTaskMap();
			for (int subTaskId : subTaskMap.keySet()) {
				SubTask subTask = subTaskMap.get(subTaskId);
				subTasksListByMaintask.add(subTask);
			}
		}
		return subTasksListByMaintask;
	}

	/*
	 * удалить задачу по id
	 */
	@Override
	public Integer deleteTaskById(int taskId) {
		if (taskId > 0) {
			taskMap.remove(taskId);
			historyManager.remove(taskId);
			return taskId;
		}
		return -1;
	}

	/*
	 * удалить главную задачу по id
	 */
	@Override
	public Integer deleteMainTaskById(int maintaskId) {
		if (mainTaskMap == null || maintaskId < 1 || !mainTaskMap.containsKey(maintaskId)) {
			return -1;
		}
		MainTask mainTask = mainTaskMap.get(maintaskId);
		Map<Integer, SubTask> subTaskMap = mainTask.getSubTaskMap();
		historyManager.removeAll(subTaskMap);
		historyManager.remove(maintaskId);
		mainTaskMap.remove(maintaskId);
		return 1;
	}

	/*
	 * удалить подзадачу по id
	 */
	@Override
	public Integer deleteSubTaskById(int sTaskId) {
		if (mainTaskMap == null || sTaskId < 1) {
			return -1;
		}
		for (int mainTaskId : mainTaskMap.keySet()) {
			MainTask mainTask = mainTaskMap.get(mainTaskId);
			Map<Integer, SubTask> subTaskMap = mainTask.getSubTaskMap();
			if (subTaskMap == null) {
				return -1;
			}
			for (int subTaskId : subTaskMap.keySet()) {
				if (subTaskId == sTaskId) {
					subTaskMap.remove(subTaskId);
					historyManager.remove(subTaskId);
					checkTaskProgress(mainTask);
					return sTaskId;
				}
			}
		}
		return 0;
	}

	/*
	 * удалить все задачи из taskMap
	 */
	@Override
	public Integer deleteAllTasks() throws Exception {
		if (taskMap != null) {
			historyManager.removeAll(taskMap);
			taskMap.clear();
			return 1;
		}
		throw new Exception("TaskMapNotFoundException");
	}

	/*
	 * удалить все главные задачи
	 */
	@Override
	public Integer deleteAllMainTasks() throws Exception {
		if (mainTaskMap != null) {
			deleteAllSubTasks();
			historyManager.removeAll(mainTaskMap);
			mainTaskMap.clear();
			return 1;
		}
		throw new Exception("MaintaskMapNotFoundException");
	}

	/*
	 * удалить все подзадачи для всех главных задач
	 */
	@Override
	public Integer deleteAllSubTasks() throws Exception {
		if (mainTaskMap != null) {
			for (int mainTaskId : mainTaskMap.keySet()) {
				MainTask mainTask = mainTaskMap.get(mainTaskId);
				Map<Integer, SubTask> subTaskMap = mainTask.getSubTaskMap();
				historyManager.removeAll(subTaskMap);
				mainTask.clearSubTaskMap();
				checkTaskProgress(mainTask);
			}
			return 1;
		}
		throw new Exception("SubtaskMapNotFoundException");
	}

	/*
	 * очистить все хранилища
	 */
	@Override
	public Integer clearAllDepos() throws Exception {
		taskMap.clear();
		deleteAllMainTasks();
		return 1;
	}

	/*
	 * отслеживание статуса главной задачи
	 */
	private void checkTaskProgress(MainTask mainTask) {
		int subTaskCountNew = 0;
		int subTaskCountDone = 0;
		Map<Integer, SubTask> subTaskMap = mainTask.getSubTaskMap();
		for (int subTaskId : subTaskMap.keySet()) {
			SubTask subTask = subTaskMap.get(subTaskId);
			TaskProgress subTaskProgress = subTask.getTaskProgress();
			if (subTaskProgress.equals(TaskProgress.DONE)) {
				mainTask.setTaskProgress(TaskProgress.IN_PROGRESS);
				subTaskCountDone++;
			}
			if (subTaskProgress.equals(TaskProgress.NEW)) {
				subTaskCountNew++;
			}
			if (subTaskProgress.equals(TaskProgress.IN_PROGRESS)) {
				mainTask.setTaskProgress(TaskProgress.IN_PROGRESS);
			}
		}
		if (subTaskCountNew == subTaskMap.size()) {
			mainTask.setTaskProgress(TaskProgress.NEW);
		}
		if (subTaskCountDone == subTaskMap.size()) {
			mainTask.setTaskProgress(TaskProgress.DONE);
		}
	}
}