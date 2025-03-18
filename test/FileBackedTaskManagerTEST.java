package test;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import main.java.model.Task;
import main.java.model.SubTask;
import main.java.model.MainTask;
import main.java.utils.Managers;
import main.java.model.TaskProgress;
import main.java.interfaces.TaskManager;
import main.java.service.FileBackedTaskManager;

import org.junit.jupiter.api.Assertions;

public class FileBackedTaskManagerTEST {

	@Test
	void saveToFileAndLoadFromFile_defaultMethods_succes() throws Exception {

		/*
		 * организация работы с файлом
		 */

		// тестовый файл с указанным именем не существует
		File file = new File("src\\sources\\data", "TestDataFBTM.csv");

		// менеджером задач создатся новый тестовый файл
		TaskManager fbtm = Managers.getDefaultFileBackedTaskManager(file);

//		// тестовый файл задан в качестве файла по умолчанию
		Assertions.assertTrue(file.exists());
		Assertions.assertTrue(file.getName().equals("TestDataFBTM.csv"));
		Assertions.assertTrue(file.getPath().equals("src\\sources\\data\\TestDataFBTM.csv"));

		/*
		 * запись задач в хранлища и файл
		 */

		LocalDateTime localDateTime = LocalDateTime.now();
		Duration duration = Duration.ofMinutes(10);

		// запись новой задачи в хранилище и тестовый файл
		Task task1 = new Task("Task-1", "Task1-description", localDateTime, duration);
		int idtask1 = fbtm.addTask(task1);
		task1 = fbtm.getTask(idtask1).get();
		Assertions.assertTrue(idtask1 > 0);
		Assertions.assertTrue(fbtm.getTasksList().contains(task1));
		Assertions.assertTrue(file.length() > 0);

		// измнение прогресса задачи
		task1.setTaskProgress(TaskProgress.DONE);
		fbtm.updateTask(task1);

		// запись новой главной задачи в хранилище и тестовый файл
		MainTask maintask1 = new MainTask("MainTask-1", "MainTask1-description");
		int idmaintask1 = fbtm.addMainTask(maintask1);
		maintask1 = fbtm.getMainTask(idmaintask1).get();

		Assertions.assertTrue(fbtm.getMainTasksList().contains(maintask1));

		// запись новой подзадачи в хранилище и тестовый файл
		SubTask subtask1 = new SubTask("SubTask-1", "SubTask1-description", idmaintask1, localDateTime, duration);
		int idsubtask1 = fbtm.addSubTask(subtask1);
		subtask1 = fbtm.getSubTask(idsubtask1).get();

		// измнение прогресса подзадачи
		subtask1.setTaskProgress(TaskProgress.IN_PROGRESS);
		fbtm.updateSubTask(subtask1);

		// статус главной задачи изменен и обновлен в файле
		Assertions.assertEquals(TaskProgress.IN_PROGRESS, maintask1.getTaskProgress());
		Assertions.assertTrue(fbtm.getSubTasksList().contains(subtask1));

		/*
		 * удаление задач из хранилищ и файла
		 */

		// удаление задачи из хранилища и файла
		Assertions.assertEquals(1, fbtm.deleteTaskById(idtask1));
		Assertions.assertTrue(!fbtm.getTasksList().contains(task1));

		// удаление подзадачи из хранилища и файла
		Assertions.assertEquals(3, fbtm.deleteSubTaskById(idsubtask1));
		Assertions.assertTrue(!fbtm.getSubTasksList().contains(subtask1));

		// удаление главной задачи из хранилища и файла
		Assertions.assertEquals(2, fbtm.deleteMainTaskById(idmaintask1));
		Assertions.assertTrue(!fbtm.getMainTasksList().contains(maintask1));

		// все хранилища очищены
		Assertions.assertTrue(fbtm.getTasksList().isEmpty());
		Assertions.assertTrue(fbtm.getMainTasksList().isEmpty());
		Assertions.assertTrue(fbtm.getSubTasksList().isEmpty());

		/*
		 * чтение задач из cуществующего тестового файла
		 */

		task1 = new Task("Task-1", "Task1-description", localDateTime, duration);
		idtask1 = fbtm.addTask(task1);
		task1 = fbtm.getTask(idtask1).get();

		maintask1 = new MainTask("MainTask-1", "MainTask1-description");
		idmaintask1 = fbtm.addMainTask(maintask1);
		maintask1 = fbtm.getMainTask(idmaintask1).get();

		subtask1 = new SubTask("SubTask-1", "SubTask1-description", idmaintask1, localDateTime, duration);
		idsubtask1 = fbtm.addSubTask(subtask1);
		subtask1 = fbtm.getSubTask(idsubtask1).get();

		fbtm = FileBackedTaskManager.loadFromFile(file);
		Assertions.assertTrue(fbtm.getTasksList().contains(task1));
		Assertions.assertTrue(fbtm.getMainTasksList().contains(maintask1));
		Assertions.assertTrue(fbtm.getSubTasksList().contains(subtask1));

		// добавление новых задач в хранилище и тестовый файл
		Task task2 = new Task("Task-2", "Task2-description", localDateTime, duration);
		int idtask2 = fbtm.addTask(task2);
		task2 = fbtm.getTask(idtask2).get();

		Task task3 = new Task("Task-3", "Task2-description", localDateTime, duration);
		int idtask3 = fbtm.addTask(task3);
		task3 = fbtm.getTask(idtask3).get();

		// добавление новых главных задач в хранилище и тестовый файл
		MainTask maintask2 = new MainTask("MainTask-2", "MainTask2-description");
		int idmaintask2 = fbtm.addMainTask(maintask2);
		maintask2 = fbtm.getMainTask(idmaintask2).get();

		MainTask maintask3 = new MainTask("MainTask-3", "MainTask2-description");
		int idmaintask3 = fbtm.addMainTask(maintask3);
		maintask3 = fbtm.getMainTask(idmaintask3).get();

		// добавление новых подзадач в хранилища и тестовый файл
		SubTask subtask2 = new SubTask("SubTask-2", "SubTask2-description", idmaintask2, localDateTime, duration);
		int idsubtask2 = fbtm.addSubTask(subtask2);
		subtask2 = fbtm.getSubTask(idsubtask2).get();

		SubTask subtask3 = new SubTask("SubTask-3", "SubTask3-description", idmaintask2, localDateTime, duration);
		int idsubtask3 = fbtm.addSubTask(subtask3);
		subtask3 = fbtm.getSubTask(idsubtask3).get();
		
		// время начала выполнения главной задачи равно началу выполнения самой ранней подзадачи
		Assertions.assertTrue(maintask2.getStartTime()==subtask2.getStartTime());
		
		// время окончания выполнения главной задачи равно началу выполнения самой ранней подзадачи
		Assertions.assertEquals(maintask2.getEndTime(), subtask3.getEndTime());
		
		// продолжительность выполнения главной задачи равно продолжительности выполнения всех подзадач
		Assertions.assertEquals(maintask2.getDuration(), subtask2.getDuration().plus(subtask3.getDuration()));
		

		// новая задача, главная задача и подзадачи добавлены
		Assertions.assertTrue(fbtm.getTasksList().contains(task1));
		Assertions.assertTrue(fbtm.getTasksList().contains(task2));
		Assertions.assertTrue(fbtm.getTasksList().contains(task3));
		Assertions.assertTrue(fbtm.getMainTasksList().contains(maintask1));
		Assertions.assertTrue(fbtm.getMainTasksList().contains(maintask2));
		Assertions.assertTrue(fbtm.getMainTasksList().contains(maintask3));
		Assertions.assertTrue(fbtm.getSubTasksList().contains(subtask1));
		Assertions.assertTrue(fbtm.getSubTasksList().contains(subtask2));
		Assertions.assertTrue(fbtm.getSubTasksList().contains(subtask3));

		// после считывания, счетчик продолжил отсчет от макимального id в файле
		Assertions.assertEquals((subtask1.getId() + 1), task2.getId());

		// тестовый файл очищен
		fbtm.clearAllDepos();
		Assertions.assertTrue(file.exists());
		Assertions.assertTrue(file.length() == 0);

		// тестовый файл удален
		Assertions.assertTrue(file.delete());
		Assertions.assertTrue(!file.exists());
	}
}