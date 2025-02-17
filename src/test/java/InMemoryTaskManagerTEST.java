package test.java;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import main.java.model.Task;
import main.java.model.SubTask;
import main.java.model.MainTask;
import main.java.model.TaskProgress;
import main.java.service.InMemoryTaskManager;
import main.java.service.InMemoryHistoryManager;

class InMemoryTaskManagerTEST {

	InMemoryTaskManager tm = new InMemoryTaskManager();
	InMemoryHistoryManager hm = new InMemoryHistoryManager();

	/*
	 * добавить задачу
	 */
	@Test
	void addTask_notNullTask_success() {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		Assertions.assertTrue(tm.addTask(task) == task.getId());
	}

	@Test
	void addTask_nullTask_failure() {
		Assertions.assertTrue(tm.addTask(null) == -1);
	}

	@Test
	void addTask_nameIsEmpty_failure() {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		task.setName("");
		Assertions.assertTrue(tm.addTask(task) == -1);
	}

	@Test
	void addTask_nameIsBlank_failure() {
		Task task = new Task("   ", "Discription", TaskProgress.NEW);
		Assertions.assertTrue(tm.addTask(task) == -1);
	}

	@Test
	void addTask_taskIdIsMoreThanZero_failure() {
		Task task = new Task(1, "Task", "Discription", TaskProgress.NEW);
		Assertions.assertTrue(tm.addTask(task) == -1);
	}

	@Test
	void addTask_taskMapContainsKeyTaskId_failure() {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		int id = tm.addTask(task);
		Task newTask = new Task(++id, "newTask", "newDiscription", TaskProgress.NEW);
		Assertions.assertTrue(tm.addTask(newTask) == -1);
	}

	@Test
	void addTask_taskHasWrongClass_failure() {
		MainTask maintask = new MainTask("MainTask", "discription");
		Assertions.assertTrue(tm.addTask(maintask) == -1);
	}

	/*
	 * добавить главную задачу
	 */
	@Test
	void addMainTask_maintaskIsntNull_succes() {
		MainTask maintask = new MainTask("MainTask", "discription");
		Assertions.assertTrue(tm.addMainTask(maintask) == maintask.getId());
	}

	@Test
	void addMainTask_maintaskIsNull_failure() {
		Assertions.assertTrue(tm.addMainTask(null) == -1);
	}

	@Test

	void addMainTask_nameIsEmpty_failure() {
		MainTask maintask = new MainTask("", "discription");
		Assertions.assertTrue(tm.addMainTask(maintask) == -1);
	}

	@Test
	void addMainTask_nameIsBlank_failure() {
		MainTask maintask = new MainTask("   ", "discription");
		Assertions.assertTrue(tm.addMainTask(maintask) == -1);
	}

	@Test
	void addMainTask_maintaskIdIsMoreThanZero_failure() {
		MainTask task = new MainTask("MainTask", "Discription");
		task.setId(2);
		Assertions.assertTrue(tm.addMainTask(task) == -1);
	}

	@Test
	void addTask_maintaskMapContainsKeyTaskId_failure() {
		MainTask task = new MainTask("MainTask", "Discription");
		tm.addTask(task);
		MainTask newTask = new MainTask(1, "newMainTask", "newDiscription");
		Assertions.assertTrue(tm.addMainTask(newTask) == -1);
	}

	/*
	 * добавить подзадачу
	 */
	@Test
	void addSubTask_notNullSubTask_success() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		Assertions.assertTrue(tm.addSubTask(subtask) == subtask.getId());
	}

	@Test
	void addSubTask_subtaskIsNull_failure() {
		Assertions.assertTrue(tm.addSubTask(null) == -1);
	}

	@Test
	void addSubTask_nameIsEmpty_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("", "Discription", maintaskId, TaskProgress.NEW);
		Assertions.assertTrue(tm.addSubTask(subtask) == -1);
	}

	@Test
	void addSubTask_nameIsBlank_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("   ", "Discription", maintaskId, TaskProgress.NEW);
		Assertions.assertTrue(tm.addSubTask(subtask) == -1);
	}

	@Test
	void addSubTask_subtaskIdIsMoreThanZero_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		subtask.setId(1);
		Assertions.assertTrue(tm.addSubTask(subtask) == -1);
	}

	@Test
	void addSubTask_subtaskMapContainsSubTaskId_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask(1, "SubTask", "Discription", maintaskId, TaskProgress.NEW);
		Assertions.assertTrue(tm.addSubTask(subtask) == -1);
	}

	/*
	 * обновить задачу
	 */
	@Test
	void updateTask_notNullTask_succes() {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Task newTask = new Task(1, "newTask", "newDiscription", TaskProgress.NEW);
		Assertions.assertTrue(tm.updateTask(newTask) == newTask.getId());
	}

	@Test
	void updateTask_nullTask_failure() {
		Assertions.assertTrue(tm.updateTask(null) == -1);
	}

	@Test
	void updateTask_nameIsEmpty_failure() {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Task newTask = new Task(1, "", "newDiscription", TaskProgress.NEW);
		Assertions.assertTrue(tm.updateTask(newTask) == -1);
	}

	@Test
	void updateTask_nameIsBlank_failure() {
		Task newTask = new Task(1, "   ", "Discription", TaskProgress.NEW);
		Assertions.assertTrue(tm.updateTask(newTask) == -1);
	}

	@Test
	void updateTask_taskIdIsEqualsZero_failure() {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Task newTask = new Task(0, "Task", "newDiscription", TaskProgress.NEW);
		Assertions.assertTrue(tm.updateTask(newTask) == -1);
	}

	@Test
	void updateTask_taskMapDontContainsTaskId_failure() {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Task newTask = new Task(2, "Task", "newDiscription", TaskProgress.NEW);
		Assertions.assertTrue(tm.updateTask(newTask) == -1);
	}

	/*
	 * обновить главную задачу
	 */
	@Test
	void updateMainTask_notNullMainTask_succes() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int id = tm.addMainTask(maintask);
		MainTask newMainTask = new MainTask(id, "MainTask", "Discription");
		Assertions.assertTrue(tm.updateMainTask(newMainTask) == 1);
	}

	@Test
	void updateMainTask_nullMainTask_failure() {
		MainTask maintask = null;
		Assertions.assertTrue(tm.addMainTask(maintask) == -1);
	}

	@Test
	void updateMainTask_nameIsEmpty_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int id = tm.addMainTask(maintask);
		MainTask newMainTask = new MainTask(id, "", "Discription");
		Assertions.assertTrue(tm.updateMainTask(newMainTask) == -1);
	}

	@Test
	void updateMainTask_nameIsBlank_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int id = tm.addMainTask(maintask);
		MainTask newMainTask = new MainTask(id, "   ", "Discription");
		Assertions.assertTrue(tm.updateMainTask(newMainTask) == -1);
	}

	@Test
	void updateMainTask_idIsNotEqualsZer0_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		tm.addMainTask(maintask);
		MainTask newMainTask = new MainTask(0, "MainTask", "Discription");
		Assertions.assertTrue(tm.updateMainTask(newMainTask) == -1);
	}

	@Test
	void updateMainTask_maintaskMapDontContainsTaskId_failure() {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Task newTask = new Task(0, "Task", "newDiscription", TaskProgress.NEW);
		Assertions.assertTrue(tm.updateTask(newTask) == -1);
	}

	/*
	 * обновить подзадачу
	 */
	@Test
	void updateSubTask_notNullSubTask_succes() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "newSubTask", "newDiscription", maintaskId, TaskProgress.NEW);
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == subtaskId);
	}

	@Test
	void updateSubTask_nullSubTask_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		tm.addSubTask(subtask);
		SubTask newSubTask = null;
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == -1);
	}

	@Test
	void updateSubTask_nameIsEmpty_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "", "newDiscription", maintaskId, TaskProgress.NEW);
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == -1);
	}

	@Test
	void updateSubTask_nameIsBlank_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "   ", "newDiscription", maintaskId, TaskProgress.NEW);
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == -1);
	}

	void updateSubTask_subtaskIdIsEqualsZero_succes() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(0, "newSubTask", "newDiscription", maintaskId, TaskProgress.NEW);
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == -1);
	}

	@Test
	void updateSubTask_subtaskMapDontContainsSubTaskId_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(++subtaskId, "newSubTask", "newDiscription", maintaskId, TaskProgress.NEW);
		Assertions.assertTrue(tm.updateSubTask(newSubTask) == -1);
	}

	/*
	 * получить задачу по id
	 */
	@Test
	void getTask_taskMapContainsTaskId_succes() throws Exception {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Assertions.assertTrue(tm.getTask(1).getId() == 1);
	}

	@Test
	void getTask_taskMapDontContainsTaskId_failure() {
		Assertions.assertThrows(Exception.class, () -> tm.getTask(1));
	}

	/*
	 * получить главную задачу по id
	 */
	@Test
	void getMainTask_maintaskMapContainsTaskId_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		Assertions.assertTrue(tm.getMainTask(maintaskId).getId() == maintaskId);
	}

	@Test
	void getMainTask_maintaskMapDontContainsTaskId_failure() {
		Assertions.assertThrows(Exception.class, () -> tm.getMainTask(1));
	}

	/*
	 * получить подзадачу по id
	 */
	@Test
	void getSubTask_subtaskMapContainsTaskId_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		int subtaskId = tm.addSubTask(subtask);
		Assertions.assertTrue(tm.getSubTask(subtaskId).getId() == subtaskId);
	}

	@Test
	void getSubTask_subtaskDoNotMapContainsTaskId_failure() {
		Assertions.assertThrows(Exception.class, () -> tm.getSubTask(1));
	}

	/*
	 * получить список всех задач
	 */
	@Test
	void getTasksList_tasksListIsNotEmpty_success() {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Assertions.assertTrue(!tm.getTasksList().isEmpty());
	}

	@Test
	void getTasksList_tasksListIsEmptyOrNull_success() {
		Assertions.assertTrue(tm.getTasksList().isEmpty());
	}

	/*
	 * получить список всех главных задач
	 */
	@Test
	void getMainTasksList_maintaskMapIsNotEmpty_succes() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		tm.addMainTask(maintask);
		Assertions.assertTrue(!tm.getMainTasksList().isEmpty());
	}

	@Test
	void getMainTasksList_maintaskMapIsNullOrEmpty_failure() {
		Assertions.assertTrue(tm.getMainTasksList().isEmpty());
	}

	/*
	 * получить список всех подзадач
	 */
	@Test
	void getSubTasksList_subtaskMapIsNotEmpty_succes() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		tm.addSubTask(subtask);
		Assertions.assertTrue(!tm.getSubTasksList().isEmpty());
	}

	@Test
	void getSubTasksList_subtaskMapIsEmptyOrNull_failure() {
		Assertions.assertTrue(tm.getSubTasksList().isEmpty());
	}

	/*
	 * получить список подзадач по главной задаче
	 */
	@Test
	void getSubTasksListByMainTask_subtaskMapIsNotEmpty_succes() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		tm.addSubTask(subtask);
		Assertions.assertTrue(!tm.getSubTaskListByMainTask(maintaskId).isEmpty());
	}

	/*
	 * удалить задачу по id
	 */
	@Test
	void deleteTaskById_taskMapContainsTaskId_succes() {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Assertions.assertTrue(tm.deleteTaskById(1) == 1);
	}

	@Test
	void deleteTaskById_taskMapContainsTaskId_failure() {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Assertions.assertTrue(tm.deleteTaskById(1) == 1);
	}

	/*
	 * удалить главную задачу по id
	 */
	@Test
	void deleteMainTaskById_maintaskMapContainsMainTaskId_succes() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		Assertions.assertTrue(tm.deleteMainTaskById(maintaskId) == maintaskId);
	}

	@Test
	void deleteMainTaskById_maintaskMapDontContainsMainTaskId_failure() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		tm.addMainTask(maintask);
		Assertions.assertTrue(tm.deleteMainTaskById(2) == -1);
	}

	/*
	 * удалить подзадачу по id
	 */
	@Test
	void deleteSubTaskById_subtaskMapContainsSubTaskId() {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		tm.addSubTask(subtask);
		Assertions.assertTrue(tm.deleteSubTaskById(2) == 2);
	}

	@Test
	void deleteSubTaskById_subtaskMapDontContainsSubTaskId_failure() {
		Assertions.assertTrue(tm.deleteMainTaskById(1) == -1);
	}

	/*
	 * удалить все задачи из taskMap
	 */
	@Test
	void deleteAllTasks_taskMapIsNotNull_success() throws Exception {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Assertions.assertTrue(tm.deleteAllTasks() == 1);
	}

	/*
	 * удалить все главные задачи
	 */
	@Test
	void deleteAllMainTasks_maintaskIsNotNull_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Discription");
		tm.addMainTask(maintask);
		Assertions.assertTrue(tm.deleteAllMainTasks() == 1);
	}

	/*
	 * удалить все подзадачи для всех главных задач
	 */
	@Test
	void deleteAllSubTasks_subtaskMapIsNotNull_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
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
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Task newTask = new Task(1, "Task", "Discription", TaskProgress.IN_PROGRESS);
		tm.updateTask(newTask);
		Assertions.assertTrue(tm.getTask(1).getTaskProgress() == TaskProgress.IN_PROGRESS);
	}

	@Test
	void checkTaskProgress_changeTaskProgressFromNEWToDone_succes() throws Exception {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Task newTask = new Task(1, "Task", "Discription", TaskProgress.DONE);
		tm.updateTask(newTask);
		Assertions.assertTrue(tm.getTask(1).getTaskProgress() == TaskProgress.DONE);
	}

	@Test
	void checkTaskProgress_changeTaskprogressFromDONEToNEW_succes() throws Exception {
		Task task = new Task("Task", "Discription", TaskProgress.NEW);
		tm.addTask(task);
		Task newTask = new Task(1, "Task", "Discription", TaskProgress.DONE);
		tm.updateTask(newTask);
		Task newTask2 = new Task(1, "Task", "Discription", TaskProgress.NEW);
		tm.updateTask(newTask2);
		Assertions.assertTrue(tm.getTask(1).getTaskProgress() == TaskProgress.NEW);
	}

	/*
	 * отслеживание статуса главной задачи
	 */
	@Test
	void checkTaskProgress_changeMainTaskprogressFromNEWToINPROGRESS_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "newSubTask", "newDiscription", maintaskId,
				TaskProgress.IN_PROGRESS);
		tm.updateSubTask(newSubTask);
		Assertions.assertTrue(tm.getMainTask(maintaskId).getTaskProgress() == TaskProgress.IN_PROGRESS);
	}

	void checkTaskProgress_changeMainTaskprogressFromNEWToDONE_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "newSubTask", "newDiscription", maintaskId, TaskProgress.DONE);
		tm.updateSubTask(newSubTask);
		Assertions.assertTrue(tm.getMainTask(maintaskId).getTaskProgress() == TaskProgress.DONE);
	}

	void checkTaskProgress_changeMainTaskprogressFromDONEToNEW_succes() throws Exception {
		MainTask maintask = new MainTask("MainTask", "Discription");
		int maintaskId = tm.addMainTask(maintask);
		SubTask subtask = new SubTask("SubTask", "Discription", maintaskId, TaskProgress.NEW);
		int subtaskId = tm.addSubTask(subtask);
		SubTask newSubTask = new SubTask(subtaskId, "newSubTask", "newDiscription", maintaskId, TaskProgress.DONE);
		tm.updateSubTask(newSubTask);
		SubTask newSubTask2 = new SubTask(subtaskId, "newSubTask", "newDiscription", maintaskId, TaskProgress.NEW);
		tm.updateSubTask(newSubTask2);
		Assertions.assertTrue(tm.getMainTask(maintaskId).getTaskProgress() == TaskProgress.NEW);
	}
}