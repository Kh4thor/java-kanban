package test;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import main.java.model.Task;
import main.java.model.SubTask;
import main.java.model.MainTask;
import main.java.model.TaskProgress;
import main.java.interfaces.TaskManager;
import main.java.service.InMemoryTaskManager;

import org.junit.jupiter.api.Assertions;

class InMemoryTaskManagerTEST {

	TaskManager tm = new InMemoryTaskManager();

	/*
	 * добавить задачу
	 */
	@Test
	void addTask_notNullTask_success() {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		int task_id = tm.addTask(task);
		task = tm.getTask(task_id).get();
		Assertions.assertEquals(task_id, task.getId());
	}

	@Test
	void addTask_nullTask_failure() {
		Assertions.assertTrue(tm.addTask(null) == -1);
	}

	@Test
	void addTask_nameIsEmpty_failure() {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		task.setName("");
		Assertions.assertTrue(tm.addTask(task) == -1);
	}

	@Test
	void addTask_nameIsBlank_failure() {
		Task task = new Task("   ", "Description", LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.addTask(task) == -1);
	}

	@Test
	void addTask_taskIdIsMoreThanZero_failure() {
		Task task = new Task(1, "Task", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.addTask(task) == -1);
	}

	@Test
	void addTask_taskMapContainsKeyTaskId_failure() {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		int id = tm.addTask(task);
		Task newTask = new Task(++id, "newTask", "newDescription", TaskProgress.NEW, LocalDateTime.now(),
				Duration.ZERO);
		Assertions.assertTrue(tm.addTask(newTask) == -1);
	}

	@Test
	void addTask_taskHasWrongClass_failure() {
		MainTask maintask = new MainTask("MainTask", "description");
		Assertions.assertTrue(tm.addTask(maintask) == -1);
	}

	/*
	 * добавить главную задачу
	 */
	@Test
	void addMainTask_maintaskIsntNull_succes() {
		MainTask task = new MainTask("MainTask", "description");
		int taskId = tm.addMainTask(task);
		task = tm.getMainTask(taskId).get();

		Assertions.assertEquals(taskId, task.getId());
	}

	@Test
	void addMainTask_maintaskIsNull_failure() {
		Assertions.assertTrue(tm.addMainTask(null) == -1);
	}

	@Test

	void addMainTask_nameIsEmpty_failure() {
		MainTask maintask = new MainTask("", "description");
		Assertions.assertTrue(tm.addMainTask(maintask) == -1);
	}

	@Test
	void addMainTask_nameIsBlank_failure() {
		MainTask maintask = new MainTask("   ", "description");
		Assertions.assertTrue(tm.addMainTask(maintask) == -1);
	}

	@Test
	void addMainTask_maintaskIdIsMoreThanZero_failure() {
		MainTask task = new MainTask("MainTask", "Description");
		task.setId(2);
		Assertions.assertTrue(tm.addMainTask(task) == -1);
	}

	@Test
	void addTask_maintaskMapContainsKeyTaskId_failure() {
		MainTask task = new MainTask("MainTask", "Description");
		tm.addTask(task);
		MainTask newTask = new MainTask(1, "newMainTask", "newDescription");
		Assertions.assertTrue(tm.addMainTask(newTask) == -1);
	}

	/*
	 * добавить подзадачу
	 */
	@Test
	void addSubTask_notNullSubTask_success() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertEquals(maintaskId, subtask.getMaintaskId());
	}

	@Test
	void addSubTask_subtaskIsNull_failure() {
		Assertions.assertTrue(tm.addSubTask(null) == -1);
	}

	@Test
	void addSubTask_nameIsEmpty_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.addSubTask(subtask) == -1);
	}

	@Test
	void addSubTask_nameIsBlank_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("   ", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.addSubTask(subtask) == -1);
	}

	@Test
	void addSubTask_subtaskIdIsMoreThanZero_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		subtask.setId(1);
		Assertions.assertTrue(tm.addSubTask(subtask) == -1);
	}

	@Test
	void addSubTask_subtaskMapContainsSubTaskId_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask(1, "SubTask", "Description", maintaskId, TaskProgress.NEW, LocalDateTime.now(),
				Duration.ZERO);
		Assertions.assertTrue(tm.addSubTask(subtask) == -1);
	}

	/*
	 * обновить задачу
	 */
	@Test
	void updateTask_notNullTask_succes() {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Task newTask = new Task(1, "newTask", "newDescription", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.updateTask(newTask) == newTask.getId());
	}

	@Test
	void updateTask_nullTask_failure() {
		Assertions.assertTrue(tm.updateTask(null) == -1);
	}

	@Test
	void updateTask_nameIsEmpty_failure() {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Task newTask = new Task(1, "", "newDescription", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.updateTask(newTask) == -1);
	}

	@Test
	void updateTask_nameIsBlank_failure() {
		Task newTask = new Task(1, "   ", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.updateTask(newTask) == -1);
	}

	@Test
	void updateTask_taskIdIsEqualsZero_failure() {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Task newTask = new Task(0, "Task", "newDescription", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.updateTask(newTask) == -1);
	}

	@Test
	void updateTask_taskMapDontContainsTaskId_failure() throws Exception {
		tm.clearAllDepos();
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Task newTask = new Task(2, "Task", "newDescription", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.updateTask(newTask) == -1);
	}

	/*
	 * обновить главную задачу
	 */
	@Test
	void updateMainTask_notNullMainTask_succes() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int id = tm.addMainTask(maintask);
		MainTask newMainTask = new MainTask(id, "MainTask", "Description");
		Assertions.assertTrue(tm.updateMainTask(newMainTask) == 1);
	}

	@Test
	void updateMainTask_nullMainTask_failure() {
		MainTask maintask = null;
		Assertions.assertTrue(tm.addMainTask(maintask) == -1);
	}

	@Test
	void updateMainTask_nameIsEmpty_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int id = tm.addMainTask(maintask);
		MainTask newMainTask = new MainTask(id, "", "Description");
		Assertions.assertTrue(tm.updateMainTask(newMainTask) == -1);
	}

	@Test
	void updateMainTask_nameIsBlank_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int id = tm.addMainTask(maintask);
		MainTask newMainTask = new MainTask(id, "   ", "Description");
		Assertions.assertTrue(tm.updateMainTask(newMainTask) == -1);
	}

	@Test
	void updateMainTask_idIsNotEqualsZer0_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		tm.addMainTask(maintask);
		MainTask newMainTask = new MainTask(0, "MainTask", "Description");
		Assertions.assertTrue(tm.updateMainTask(newMainTask) == -1);
	}

	@Test
	void updateMainTask_maintaskMapDontContainsTaskId_failure() {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Task newTask = new Task(0, "Task", "newDescription", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.updateTask(newTask) == -1);
	}

	/*
	 * обновить подзадачу
	 */
	@Test
	void updateSubTask_notNullSubTask_succes() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "newSubTask", "newDescription", maintaskId, TaskProgress.NEW,
				LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == subtaskId);
	}

	@Test
	void updateSubTask_nullSubTask_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		tm.addSubTask(subtask);
		SubTask newSubTask = null;
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == -1);
	}

	@Test
	void updateSubTask_nameIsEmpty_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "", "newDescription", maintaskId, TaskProgress.NEW,
				LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == -1);
	}

	@Test
	void updateSubTask_nameIsBlank_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "   ", "newDescription", maintaskId, TaskProgress.NEW,
				LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == -1);
	}

	void updateSubTask_subtaskIdIsEqualsZero_succes() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(0, "newSubTask", "newDescription", maintaskId, TaskProgress.NEW,
				LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == -1);
	}

	@Test
	void updateSubTask_subtaskMapDontContainsSubTaskId_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(++subtaskId, "newSubTask", "newDescription", maintaskId, TaskProgress.NEW,
				LocalDateTime.now(), Duration.ZERO);
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == -1);
	}

	/*
	 * получить задачу по id
	 */
	@Test
	void getTask_taskMapContainsTaskId_succes() throws Exception {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Assertions.assertTrue(tm.getTask(1).get().getId() == 1);
	}

	@Test
	void getTask_taskMapDontContainsTaskId_failure() {
		new Task(-1, "null", "from_getTask", TaskProgress.DONE, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertEquals(Optional.empty(), tm.getTask(1));
	}

	/*
	 * получить главную задачу по id
	 */
	@Test
	void getMainTask_maintaskMapContainsTaskId_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Description");
		int id = tm.addMainTask(maintask);
		maintask = tm.getMainTask(id).get();
		Assertions.assertTrue(maintask.getId() == id);
	}

	@Test
	void getMainTask_maintaskMapDontContainsTaskId_failure() {
		new MainTask(-1, "null", "from_getMainTask");
		Assertions.assertEquals(Optional.empty(), tm.getMainTask(1));
	}

	/*
	 * получить подзадачу по id
	 */
	@Test
	void getSubTask_subtaskMapContainsTaskId_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		int subtaskId = tm.addSubTask(subtask);
		Assertions.assertTrue(tm.getSubTask(subtaskId).get().getId() == subtaskId);
	}

	@Test
	void getSubTask_subtasMapkDoNotContainsTaskId_failure() {
		new SubTask(-1, "null", "from_getSubTask", -1, TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		Assertions.assertEquals(Optional.empty(), tm.getSubTask(1));
	}

	/*
	 * получить список всех задач
	 */
	@Test
	void getTasksList_tasksListIsNotEmpty_success() throws Exception {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		Task task2 = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		tm.addTask(task2);
		Assertions.assertTrue(!tm.getTasksList().isEmpty());

	}

	@Test
	void getTasksList_tasksListIsEmptyOrNull_success() throws Exception {
		tm.clearAllDepos();
		Assertions.assertTrue(tm.getTasksList().isEmpty());
	}

	/*
	 * получить список всех главных задач
	 */
	@Test
	void getMainTasksList_maintaskMapIsNotEmpty_succes() {
		MainTask maintask = new MainTask("MainTask", "Description");
		tm.addMainTask(maintask);
		Assertions.assertTrue(!tm.getMainTasksList().isEmpty());
	}

	@Test
	void getMainTasksList_maintaskMapIsNullOrEmpty_failure() throws Exception {
		tm.clearAllDepos();
		Assertions.assertTrue(tm.getMainTasksList().isEmpty());
	}

	/*
	 * получить список всех подзадач
	 */
	@Test
	void getSubTasksList_subtaskMapIsNotEmpty_succes() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		tm.addSubTask(subtask);
		Assertions.assertTrue(!tm.getSubTasksList().isEmpty());
	}

	@Test
	void getSubTasksList_subtaskMapIsEmptyOrNull_failure() throws Exception {
		Assertions.assertTrue(tm.getSubTasksList().isEmpty());
	}

	/*
	 * получить список подзадач по главной задаче
	 */
	@Test
	void getSubTasksListByMainTask_subtaskMapIsNotEmpty_succes() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		tm.addSubTask(subtask);
		Assertions.assertTrue(!tm.getSubTaskListByMainTask(maintaskId).isEmpty());
	}

	/*
	 * удалить задачу по id
	 */
	@Test
	void deleteTaskById_taskMapContainsTaskId_succes() {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Assertions.assertTrue(tm.deleteTaskById(1) == 1);
	}

	@Test
	void deleteTaskById_taskMapContainsTaskId_failure() {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Assertions.assertTrue(tm.deleteTaskById(1) == 1);
	}

	/*
	 * удалить главную задачу по id
	 */
	@Test
	void deleteMainTaskById_maintaskMapContainsMainTaskId_succes() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		Assertions.assertTrue(tm.deleteMainTaskById(maintaskId) == maintaskId);
	}

	@Test
	void deleteMainTaskById_maintaskMapDontContainsMainTaskId_failure() {
		MainTask maintask = new MainTask("MainTask", "Description");
		tm.addMainTask(maintask);
		Assertions.assertTrue(tm.deleteMainTaskById(2) == -1);
	}

	/*
	 * удалить подзадачу по id
	 */
	@Test
	void deleteSubTaskById_subtaskMapContainsSubTaskId() {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		int id = tm.addSubTask(subtask);

		Assertions.assertEquals(2, tm.deleteSubTaskById(id));
	}

	@Test
	void deleteSubTaskById_subtaskMapDontContainsSubTaskId_failure() throws Exception {
		tm.clearAllDepos();
		Assertions.assertEquals(-1, tm.deleteMainTaskById(1));
	}

	/*
	 * удалить все задачи из taskMap
	 */
	@Test
	void deleteAllTasks_taskMapIsNotNull_success() throws Exception {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Assertions.assertTrue(tm.deleteAllTasks() == 1);
	}

	/*
	 * удалить все главные задачи
	 */
	@Test
	void deleteAllMainTasks_maintaskIsNotNull_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Description");
		tm.addMainTask(maintask);
		Assertions.assertTrue(tm.deleteAllMainTasks() == 1);
	}

	/*
	 * удалить все подзадачи для всех главных задач
	 */
	@Test
	void deleteAllSubTasks_subtaskMapIsNotNull_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		tm.addSubTask(subtask);
		Assertions.assertTrue(tm.deleteAllMainTasks() == 1);
	}

	/*
	 * очистить все хранилища
	 */
	@Test
	void clearAllDepos_deposAreNotNull() throws Exception {
		Assertions.assertTrue(tm.clearAllDepos() == 1);
	}

	/*
	 * отслеживание статуса задачи
	 */
	@Test
	void checkTaskProgress_changeTaskProgressFromNEWToIN_PRPGRESS_succes() throws Exception {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Task newTask = new Task(1, "Task", "Description", TaskProgress.IN_PROGRESS, LocalDateTime.now(), Duration.ZERO);
		tm.updateTask(newTask);
		Assertions.assertTrue(tm.getTask(1).get().getTaskProgress() == TaskProgress.IN_PROGRESS);
	}

	@Test
	void checkTaskProgress_changeTaskProgressFromNEWToDone_succes() throws Exception {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Task newTask = new Task(1, "Task", "Description", TaskProgress.DONE, LocalDateTime.now(), Duration.ZERO);
		tm.updateTask(newTask);
		Assertions.assertTrue(tm.getTask(1).get().getTaskProgress() == TaskProgress.DONE);
	}

	@Test
	void checkTaskProgress_changeTaskprogressFromDONEToNEW_succes() throws Exception {
		Task task = new Task("Task", "Description", LocalDateTime.now(), Duration.ZERO);
		tm.addTask(task);
		Task newTask = new Task(1, "Task", "Description", TaskProgress.DONE, LocalDateTime.now(), Duration.ZERO);
		tm.updateTask(newTask);
		Task newTask2 = new Task(1, "Task", "Description", TaskProgress.NEW, LocalDateTime.now(), Duration.ZERO);
		tm.updateTask(newTask2);
		Assertions.assertTrue(tm.getTask(1).get().getTaskProgress() == TaskProgress.NEW);
	}

	/*
	 * отслеживание статуса главной задачи
	 */
	@Test
	void checkTaskProgress_changeMainTaskprogressFromNEWToINPROGRESS_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "newSubTask", "newDescription", maintaskId,
				TaskProgress.IN_PROGRESS, LocalDateTime.now(), Duration.ZERO);
		tm.updateSubTask(newSubTask);
		Assertions.assertTrue(tm.getMainTask(maintaskId).get().getTaskProgress() == TaskProgress.IN_PROGRESS);
	}

	@Test
	void checkTaskProgress_changeMainTaskprogressFromNEWToDONE_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "newSubTask", "newDescription", maintaskId, TaskProgress.DONE,
				LocalDateTime.now(), Duration.ZERO);
		tm.updateSubTask(newSubTask);
		Assertions.assertEquals(TaskProgress.DONE, tm.getMainTask(maintaskId).get().getTaskProgress());
	}

	@Test
	void checkTaskProgress_changeMainTaskprogressFromDONEToNEW_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Description");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Description", maintaskId, LocalDateTime.now(), Duration.ZERO);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "newSubTask", "newDescription", maintaskId, TaskProgress.DONE,
				LocalDateTime.now(), Duration.ZERO);
		tm.updateSubTask(newSubTask);
		SubTask newSubTask2 = new SubTask(subtaskId, "newSubTask", "newDescription", maintaskId, TaskProgress.NEW,
				LocalDateTime.now(), Duration.ZERO);
		tm.updateSubTask(newSubTask2);
		Assertions.assertTrue(tm.getMainTask(maintaskId).get().getTaskProgress() == TaskProgress.NEW);
	}

	@Test
	void deleteTaskById_FromHistory_succes() {
		Task task = new Task("Task-1", "Description", LocalDateTime.now(), Duration.ZERO);
		int task_id = tm.addTask(task);

		MainTask mainTask = new MainTask("MainTask-1", "Description");
		int mainTask_id = tm.addMainTask(mainTask);

		SubTask task3 = new SubTask("SubTask-1", "Description", mainTask_id, LocalDateTime.now(), Duration.ZERO);
		int subtask_id = tm.addSubTask(task3);

		// дублирование задач в начале истории
		tm.getTask(task_id);
		Task getTask2 = tm.getTask(task_id).get();

		// дублирование задач в середине истории
		tm.getMainTask(mainTask_id);
		Task getMainTask2 = tm.getMainTask(mainTask_id).get();

		// дублирование задач в конце истории
		tm.getSubTask(subtask_id);
		Task getSubtask2 = tm.getSubTask(subtask_id).get();

		Assertions.assertEquals(3, tm.getHistory().size());
		Assertions.assertEquals(getTask2, tm.getHistory().get(0));
		Assertions.assertEquals(getMainTask2, tm.getHistory().get(1));
		Assertions.assertEquals(getSubtask2, tm.getHistory().get(2));

		// удаление задач
		tm.deleteTaskById(task_id);
		tm.deleteSubTaskById(subtask_id);
		tm.deleteMainTaskById(mainTask_id);

		// история пуста
		Assertions.assertEquals(0, tm.getHistory().size());
	}

	@Test
	void asd() throws Exception {
		Task task1 = new Task("Task-1", "Description", LocalDateTime.now(), Duration.ZERO);
		Task task2 = new Task("Task-2", "Description", LocalDateTime.now(), Duration.ZERO);
		Task task3 = new Task("Task-3", "Description", LocalDateTime.now(), Duration.ZERO);
		int task1_id = tm.addTask(task1);
		int task2_id = tm.addTask(task2);
		int task3_id = tm.addTask(task3);

		MainTask maintask1 = new MainTask("Maintask-1", "Description");
		MainTask maintask2 = new MainTask("Maintask-2", "Description");
		MainTask maintask3 = new MainTask("Maintask-3", "Description");
		int maintask1_id = tm.addMainTask(maintask1);
		int maintask2_id = tm.addMainTask(maintask2);
		int maintask3_id = tm.addMainTask(maintask3);

		SubTask subtask1 = new SubTask("Subtask-1", "Description", maintask1_id, LocalDateTime.now(), Duration.ZERO);
		SubTask subtask2 = new SubTask("Subtask-2", "Description", maintask2_id, LocalDateTime.now(), Duration.ZERO);
		SubTask subtask3 = new SubTask("Subtask-3", "Description", maintask3_id, LocalDateTime.now(), Duration.ZERO);
		int subtask1_id = tm.addSubTask(subtask1);
		int subtask2_id = tm.addSubTask(subtask2);
		int subtask3_id = tm.addSubTask(subtask3);

		task1 = tm.getTask(task1_id).get();
		task2 = tm.getTask(task2_id).get();
		task3 = tm.getTask(task3_id).get();

		maintask1 = tm.getMainTask(maintask1_id).get();
		maintask2 = tm.getMainTask(maintask2_id).get();
		maintask3 = tm.getMainTask(maintask3_id).get();

		subtask1 = tm.getSubTask(subtask1_id).get();
		subtask2 = tm.getSubTask(subtask2_id).get();
		subtask3 = tm.getSubTask(subtask3_id).get();

		Assertions.assertEquals(9, tm.getHistory().size());
		Assertions.assertEquals(task1, tm.getHistory().get(0));
		Assertions.assertEquals(task2, tm.getHistory().get(1));
		Assertions.assertEquals(task3, tm.getHistory().get(2));

		Assertions.assertEquals(maintask1, tm.getHistory().get(3));
		Assertions.assertEquals(maintask2, tm.getHistory().get(4));
		Assertions.assertEquals(maintask3, tm.getHistory().get(5));

		Assertions.assertEquals(subtask1, tm.getHistory().get(6));
		Assertions.assertEquals(subtask2, tm.getHistory().get(7));
		Assertions.assertEquals(subtask3, tm.getHistory().get(8));

		Map<Integer, Task> tasks = new HashMap<>();
		tasks.put(task1.getId(), task1);
		tasks.put(task2.getId(), task2);
		tasks.put(task3.getId(), task3);

		Map<Integer, Task> mainTasks = new HashMap<>();
		mainTasks.put(maintask1.getId(), maintask1);
		mainTasks.put(maintask2.getId(), maintask2);
		mainTasks.put(maintask3.getId(), maintask3);

		Map<Integer, Task> subTasks = new HashMap<>();
		subTasks.put(subtask1.getId(), subtask1);
		subTasks.put(subtask2.getId(), subtask2);
		subTasks.put(subtask3.getId(), subtask3);

		// удаление задач списком
		tm.deleteAllTasks();

		// проверка удаления всех задач
		Assertions.assertEquals(6, tm.getHistory().size());
		Assertions.assertEquals(maintask1, tm.getHistory().get(0));
		Assertions.assertEquals(maintask2, tm.getHistory().get(1));
		Assertions.assertEquals(maintask3, tm.getHistory().get(2));
		Assertions.assertEquals(subtask1, tm.getHistory().get(3));
		Assertions.assertEquals(subtask2, tm.getHistory().get(4));
		Assertions.assertEquals(subtask3, tm.getHistory().get(5));

		// удаление списком всех главных задач
		tm.deleteAllMainTasks();

		// проверка удаления всех главных задач и их подзадач
		Assertions.assertEquals(0, tm.getHistory().size());
	}
}