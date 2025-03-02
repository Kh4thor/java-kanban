package test;

import org.junit.jupiter.api.Test;

import java.io.File;

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
		File file = new File("TestDataFBTM.csv");

		// менеджером задач создатся новый тестовый файл
		TaskManager fbtm = Managers.getDefaultFileBackedTaskManager(file);

		// тестовый файл задан в качестве файла по умолчанию
		file = FileBackedTaskManager.defaultFile;
		Assertions.assertTrue(file.exists());
		Assertions.assertTrue(file.getName().equals("TestDataFBTM.csv"));
		Assertions.assertTrue(file.getPath().equals("src\\sources\\data\\TestDataFBTM.csv"));

		/*
		 * запись задач в файл
		 */

		// запись новой задачи в хранилище и тестовый файл
		Task task1 = new Task("Task-1", "Task1-description");
		int idtask1 = fbtm.addTask(task1);
		task1 = fbtm.getTask(idtask1);
		Assertions.assertTrue(idtask1 > 0);
		Assertions.assertTrue(fbtm.getTasksList().contains(task1));
		Assertions.assertTrue(file.length() > 0);

		// измнение прогресса задачи
		task1.setTaskProgress(TaskProgress.DONE);
		fbtm.updateTask(task1);

		// запись новой главной задачи в хранилище и тестовый файл
		MainTask maintask1 = new MainTask("MainTask-1", "MainTask1-description");
		int idmaintask1 = fbtm.addMainTask(maintask1);
		maintask1 = fbtm.getMainTask(idmaintask1);
		Assertions.assertTrue(idmaintask1 == ++idtask1);
		Assertions.assertTrue(fbtm.getMainTasksList().contains(maintask1));

		// запись новой подзадачи в хранилище и тестовый файл
		SubTask subtask1 = new SubTask("SubTask-1", "SubTask1-description", idmaintask1);
		int idsubtask1 = fbtm.addSubTask(subtask1);
		subtask1 = fbtm.getSubTask(idsubtask1);

		// измнение прогресса подзадачи
		subtask1.setTaskProgress(TaskProgress.IN_PROGRESS);
		fbtm.updateSubTask(subtask1);

		// статус главной задачи изменен и внесен в файл
		Assertions.assertEquals(TaskProgress.IN_PROGRESS, maintask1.getTaskProgress());
		Assertions.assertTrue(fbtm.getSubTasksList().contains(subtask1));

		// все хранилища очищены
		Assertions.assertEquals(1, fbtm.clearAllDepos());
		Assertions.assertTrue(fbtm.getTasksList().isEmpty());
		Assertions.assertTrue(fbtm.getMainTasksList().isEmpty());
		Assertions.assertTrue(fbtm.getSubTasksList().isEmpty());

		/*
		 * чтение задач из cуществующего тестового файла
		 */
		fbtm = FileBackedTaskManager.loadFromFile(file);
		Assertions.assertTrue(fbtm.getTasksList().contains(task1));
		Assertions.assertTrue(fbtm.getMainTasksList().contains(maintask1));
		Assertions.assertTrue(fbtm.getSubTasksList().contains(subtask1));

		// запись новой задачи в хранилище и тестовый файл
		Task task2 = new Task("Task-2", "Task2-description");
		int idtask2 = fbtm.addTask(task2);
		task2 = fbtm.getTask(idtask2);

		// запись новой главной задачи в хранилище и тестовый файл
		MainTask maintask2 = new MainTask("MainTask-2", "MainTask2-description");
		int idmaintask2 = fbtm.addMainTask(maintask2);
		maintask2 = fbtm.getMainTask(idmaintask2);

		// запись новой подзадачи в хранилище и тестовый файл
		SubTask subtask2 = new SubTask("SubTask-2", "SubTask2-description", idmaintask2);
		int idsubtask2 = fbtm.addSubTask(subtask2);
		subtask2 = fbtm.getSubTask(idsubtask2);

		// новая задача, главная задача и подзадачи добавлены
		Assertions.assertTrue(fbtm.getTasksList().contains(task1));
		Assertions.assertTrue(fbtm.getTasksList().contains(task2));
		Assertions.assertTrue(fbtm.getMainTasksList().contains(maintask1));
		Assertions.assertTrue(fbtm.getMainTasksList().contains(maintask2));
		Assertions.assertTrue(fbtm.getSubTasksList().contains(subtask1));
		Assertions.assertTrue(fbtm.getSubTasksList().contains(subtask2));

		// после считывания, счетчик продолжил отсчет от макимального id в файле
		Assertions.assertEquals((subtask1.getId() + 1), task2.getId());

		// тестовый файл очищен
		FileBackedTaskManager.clear();
		Assertions.assertTrue(file.exists());
		Assertions.assertTrue(file.length() == 0);

		// тестовый файл удален
		Assertions.assertTrue(file.delete());
		Assertions.assertTrue(!file.exists());
	}
}