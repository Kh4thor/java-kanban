package main.java.service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import main.java.model.Task;
import main.java.model.SubTask;
import main.java.model.MainTask;
import main.java.model.TaskProgress;
import main.java.interfaces.TaskManager;
import main.java.interfaces.HistoryManager;

import java.util.List;
import java.util.HashMap;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class InMemoryTaskManager implements TaskManager, Cloneable {

	// единый счетчик для всех типов задач
	protected int id = 0;

	protected HistoryManager historyManager = new InMemoryHistoryManager();

	// хранилище приоритетных задач и подзадач
	protected Set<Task> prioritetMap = new TreeSet<>((Task t1, Task t2) -> comparare(t1, t2));
	// хранилище задач
	protected Map<Integer, Task> taskMap = new HashMap<>();
	// хранилище главных задач
	protected Map<Integer, MainTask> mainTaskMap = new HashMap<>();
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
	 * добавить задачу в хранилище
	 */
	@Override
	public Integer addTask(Task task) {
		if (task != null && task.getClass() == Task.class && !taskMap.containsKey(task.getId()) && task.getId() == 0
				&& !task.getName().isBlank() && !task.getName().isEmpty() && task.getDuration() != null) {

			try {
				// клонирование задачи
				Task cloneTask = task.clone();

				// установить id для задачи
				cloneTask.setId(++id);

				// если валидация на добавление задачи в хранилище приоритетных задач
				// пройдена...
				if (isValidate(cloneTask)) {

					// ...положить задачу в хранилище приоритетов
					prioritetMap.add(cloneTask);
				}
				// положить задачу в хранилище задач
				int id = cloneTask.getId();
				taskMap.put(id, cloneTask);
				return id;
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/*
	 * добавить главную задачу в хранилище
	 */
	@Override
	public Integer addMainTask(MainTask mainTask) {
		if (mainTask != null && mainTask.getId() == 0 && !mainTaskMap.containsKey(mainTask.getId())
				&& !mainTask.getName().isBlank() && !mainTask.getName().isEmpty()) {

			try {
				// клонирование главной задачи
				MainTask cloneMaintask = mainTask.clone();

				// установить id для главной задачи
				cloneMaintask.setId(++id);

				// положить главную задау в хранилище главных задач
				mainTaskMap.put(cloneMaintask.getId(), cloneMaintask);

				return cloneMaintask.getId();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/*
	 * добавить подзадачу в хранилище
	 */
	@Override
	public Integer addSubTask(SubTask subtask) {
		if (subtask != null && subtask.getId() == 0 && !subtask.getName().isBlank() && !subtask.getName().isEmpty()
				&& mainTaskMap.containsKey(subtask.getMaintaskId())) {

			try {
				// поиск главной задачи подзадачи
				MainTask mainTask = mainTaskMap.get(subtask.getMaintaskId());

				// клонирование подзадачи
				SubTask cloneSubtask = subtask.clone();

				// установить id для подзадачи
				cloneSubtask.setId(++id);

				// если валидация на добавление подзадачи в хранилище приоритетных задач
				// пройдена...
				if (isValidate(subtask)) {
					// ...добавить подзадачу в хранилище приоритетных задач
					prioritetMap.add(cloneSubtask);
				}
				// добавить подзадачу в хранилище подзадач...
				mainTask.addSubTaskToDepo(cloneSubtask);

				// если валидация на добавление подзадачи в хранилище приоритетных задач
				// пройдена...
				if (isValidate(cloneSubtask)) {
					prioritetMap.add(cloneSubtask);
				}

				// ...отследить статус главной задачи
				checkMainTaskStatus(mainTask);
				return cloneSubtask.getId();

			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/*
	 * обновить задачу
	 */
	@Override
	public Integer updateTask(Task task) {
		if (task != null && task.getClass() == Task.class && task.getId() > 0 && taskMap.containsKey(task.getId())
				&& !task.getName().isEmpty() && !task.getName().isBlank() && task.getTaskProgress() != null) {

			try {
				// клонирование задачи
				Task cloneTask = task.clone();

				// если валидация на добавление задачи в хранилище приоритетных задач
				// пройдена...
				if (isValidate(task) && prioritetMap.stream().anyMatch(t -> t.getId() == task.getId())) {

					// удалить обновляемую задачу из хранилища приоритетных задач
					prioritetMap.removeIf(t -> t.getId() == cloneTask.getId());

					// добавить в хранилище приоритеных задач обновленную задачу
					prioritetMap.add(cloneTask);
				}
				// обновить задачу в хранилище задач
				taskMap.put(cloneTask.getId(), cloneTask);
				return cloneTask.getId();

			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/*
	 * обновить главную задачу
	 */
	@Override
	public Integer updateMainTask(MainTask maintask) {
		if (maintask != null && mainTaskMap.containsKey(maintask.getId()) && !maintask.getName().isEmpty()
				&& !maintask.getName().isBlank()) {

			try {
				// обновить задачу в хранилище задач
				MainTask cloneMaintask = maintask.clone();
				return cloneMaintask.getId();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/*
	 * обновить подзадачу
	 */
	@Override
	public Integer updateSubTask(SubTask subtask) {
		if (subtask != null) {
			// поиск id-главной задачи для подзадачи
			int mainTaskId = subtask.getMaintaskId();
			if (mainTaskMap.containsKey(mainTaskId)) {

				// поиск главной задачи подзадачи
				MainTask mainTask = mainTaskMap.get(mainTaskId);
				if (mainTask != null) {

					// поиск хранилища подзадач главной задачи
					Map<Integer, SubTask> subTaskMap = mainTask.getSubTaskMap();

					if (subTaskMap != null && subTaskMap.containsKey(subtask.getId()) && !subtask.getName().isBlank()
							&& !subtask.getName().isEmpty()) {

						try {
							// клонирование подзадачи
							SubTask cloneSubtask = subtask.clone();

							if (isValidate(cloneSubtask)
									&& prioritetMap.stream().anyMatch(t -> t.getId() == cloneSubtask.getId())) {

								// удалить обновляемую подзадачу из хранилища приоритетных задач
								prioritetMap.removeIf(t -> t.getId() == cloneSubtask.getId());

								// добавить в хранилище приоритеных задач обновленную подзадачу
								prioritetMap.add(cloneSubtask);
							}
							// добавить подзадачу в хранилище подзадач
							subTaskMap.put(cloneSubtask.getId(), cloneSubtask);

							// обновить статус главной задачи
							checkMainTaskStatus(mainTask);
							return cloneSubtask.getId();

						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
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
	public Optional<Task> getTask(int taskId) {
		if (taskId > 0 && taskMap.containsKey(taskId)) {
			Task task = taskMap.get(taskId);
			historyManager.addToHistory(taskMap.get(taskId));
			return Optional.of(task);
		}
		return Optional.empty();
	}

	/*
	 * получить главную задачу по id
	 */
	@Override
	public Optional<MainTask> getMainTask(int maintaskId) {

		if (maintaskId > 0 && mainTaskMap.containsKey(maintaskId)) {
			Optional<MainTask> optionalMainTask = Optional.ofNullable(mainTaskMap.get(maintaskId));
			// если значение не null, задача добавляется в историю
			if (optionalMainTask.isPresent()) {
				historyManager.addToHistory(optionalMainTask.get());
				return optionalMainTask;
			}
		}
		return Optional.empty();
	}

	/*
	 * получить подзадачу по id-подзадачи
	 */
	@Override
	public Optional<SubTask> getSubTask(int id) {
		if (id > 0) {
			Optional<SubTask> optionalSubtask = mainTaskMap.values().stream().map(mainTask -> mainTask.getSubTaskMap())
					.filter(subTaskMap -> subTaskMap.containsKey(id)).map(subTaskMap -> subTaskMap.get(id)).findFirst();

			if (optionalSubtask.isPresent()) {
				historyManager.addToHistory(optionalSubtask.get());
				return optionalSubtask;
			}
		}
		return Optional.empty();
	}

	/*
	 * получить список всех задач
	 */
	@Override
	public List<Task> getTasksList() {
		if (taskMap != null) {
			return taskMap.values().stream().toList();
		}
		return new ArrayList<Task>();
	}

	/*
	 * полчучить список всех главных задач
	 */
	@Override
	public List<MainTask> getMainTasksList() {
		if (mainTaskMap != null) {
			return mainTaskMap.values().stream().toList();
		}
		return new ArrayList<MainTask>();
	}

	/*
	 * получить список всех подзадач
	 */
	@Override
	public List<SubTask> getSubTasksList() {
		if (mainTaskMap != null) {

			return mainTaskMap.values().stream().map(mainTask -> mainTask.getSubTaskMap())
					.flatMap(subTaskMap -> subTaskMap.values().stream()).toList();
		}
		return new ArrayList<SubTask>();
	}

	/*
	 * получить список подзадач по главной задаче
	 */
	@Override
	public List<SubTask> getSubTaskListByMainTask(int mainTaskId) {

		if (mainTaskMap.containsKey(mainTaskId) && mainTaskId > 0) {
			MainTask mainTask = mainTaskMap.get(mainTaskId);
			Map<Integer, SubTask> subTaskMap = mainTask.getSubTaskMap();
			return subTaskMap.values().stream().toList();
		}
		return new ArrayList<SubTask>();
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
		return maintaskId;
	}

	/*
	 * удалить подзадачу по id
	 */
	@Override
	public Integer deleteSubTaskById(int sTaskId) {
		if (mainTaskMap != null) {
			for (int mainTaskId : mainTaskMap.keySet()) {
				MainTask mainTask = mainTaskMap.get(mainTaskId);
				Map<Integer, SubTask> subTaskMap = mainTask.getSubTaskMap();
				if (subTaskMap != null) {
					for (int subTaskId : subTaskMap.keySet()) {
						if (subTaskId == sTaskId) {
							subTaskMap.remove(subTaskId);
							historyManager.remove(sTaskId);
							checkMainTaskStatus(mainTask);
							return sTaskId;
						}
					}
				}
			}
		}
		return -1;
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
				checkMainTaskStatus(mainTask);
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
	 * получить список приоритетных задач
	 */
	public List<Task> getPrioritizedTasks() {
		return prioritetMap.stream().toList();
	}

	/*
	 * метод проверки пересечения задач по времени выполнения
	 */
	private boolean isTasksIntersectInTime(Task t1, Task t2) throws IllegalArgumentException {
		if (t1.getStartTime() != null && t2.getStartTime() != null && t1.getEndTime() != null
				&& t2.getEndTime() != null) {

			// время и дата начала задачи-1
			LocalDateTime s1 = t1.getStartTime();
			// время и дата начала задачи-2
			LocalDateTime s2 = t2.getStartTime();
			// время и дата окончания задачи-1
			LocalDateTime e1 = t1.getEndTime();
			// время и дата окончания задачи-2
			LocalDateTime e2 = t2.getEndTime();

			// если задача-2 начаинается во время выполнения задачи-1
			if (s2.isAfter(s1) && s2.isBefore(e1)) {
				return true;
			}
			// если задача-2 заканчивается во время выполнения задачи-1
			if (e2.isAfter(s1) && e2.isBefore(e1)) {
				return true;
			}
			// если задача-1 начаинается во время выполнения задачи-2
			if (s1.isAfter(s2) && s1.isBefore(e2)) {
				return true;
			}
			// если задача-1 заканчивается во время выполнения задачи-2
			if (e1.isAfter(s2) && e1.isBefore(e2)) {
				return true;
			}
			return false;
		}
		return false;
	}

	/*
	 * проверка на пересечение входящей задачи с задачами из приоритетного списка
	 */
	protected boolean isValidate(Task task) {
		if (task.getStartTime() != null && task.getDuration() != null) {
			return (!prioritetMap.stream().anyMatch(t -> isTasksIntersectInTime(t, task)));
		}
		return false;
	}

	/*
	 * отслеживание статуса главной задачи
	 */
	protected void checkMainTaskStatus(MainTask mainTask) {
		// отслеживание статуса главной задачи
		checkMainTaskProgress(mainTask);
		// отслеживание времени начала выполнения главной задачи
		checkMainTaskStartTime(mainTask);
		// остлеживание продолжительности выполнения главной задачи
		checkMainTaskDuration(mainTask);
	}

	/*
	 * отслеживание статуса главной задачи
	 */
	private void checkMainTaskProgress(MainTask mainTask) {
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
		if (subTaskCountNew > 0 && subTaskCountNew == subTaskMap.size()) {
			mainTask.setTaskProgress(TaskProgress.NEW);
		}
		if (subTaskCountDone > 0 && subTaskCountDone == subTaskMap.size()) {
			mainTask.setTaskProgress(TaskProgress.DONE);
		}
	}

	/*
	 * отслеживание времени начала выполнения главной задачи
	 */
	private void checkMainTaskStartTime(MainTask maintask) {
		Map<Integer, SubTask> subTaskMap = maintask.getSubTaskMap();

		// сортировка подзадач по времени начала выполнения и поиск самого раннего
		Optional<SubTask> optionalSubTask = subTaskMap.values().stream()
				.min(Comparator.comparing(SubTask::getStartTime));

		// присваивание главной задаче времени самого раннего начала выполнения
		// подзадачи
		if (optionalSubTask.isPresent()) {
			LocalDateTime localDateTime = optionalSubTask.get().getStartTime();
			maintask.setStartTime(localDateTime);
		}
	}

	/*
	 * остлеживание продолжительности выполнения главной задачи
	 */
	private void checkMainTaskDuration(MainTask maintask) {
		Map<Integer, SubTask> subTaskMap = maintask.getSubTaskMap();

		// суммарное время выполнения подзадач одной главной задачи
		Long minutes = subTaskMap.values().stream().map(subtask -> subtask.getDuration().toMinutes())
				.mapToLong(Long::longValue).sum();

		// присваивание главной задаче
		maintask.setDuration(Duration.ofMinutes(minutes));
	}

	// метод сравнения задач для сортирвки хранилища приоритеных задач
	private int comparare(Task t1, Task t2) {
		if (isTasksIntersectInTime(t1, t2)) {
			return 0;
		}
		return 1;
	}
}