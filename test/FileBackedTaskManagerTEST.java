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
		Assertions.assertTrue(fbtm.getMainTasksList().contains(maintask1));

		// запись новой подзадачи в хранилище и тестовый файл
		SubTask subtask1 = new SubTask("SubTask-1", "SubTask1-description", idmaintask1);
		int idsubtask1 = fbtm.addSubTask(subtask1);
		subtask1 = fbtm.getSubTask(idsubtask1);

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

		task1 = new Task("Task-1", "Task1-description");
		idtask1 = fbtm.addTask(task1);
		task1 = fbtm.getTask(idtask1);

		maintask1 = new MainTask("MainTask-1", "MainTask1-description");
		idmaintask1 = fbtm.addMainTask(maintask1);
		maintask1 = fbtm.getMainTask(idmaintask1);

		subtask1 = new SubTask("SubTask-1", "SubTask1-description", idmaintask1);
		idsubtask1 = fbtm.addSubTask(subtask1);
		subtask1 = fbtm.getSubTask(idsubtask1);

		fbtm = FileBackedTaskManager.loadFromFile(file);
		Assertions.assertTrue(fbtm.getTasksList().contains(task1));
		Assertions.assertTrue(fbtm.getMainTasksList().contains(maintask1));
		Assertions.assertTrue(fbtm.getSubTasksList().contains(subtask1));

		// добавление новых задач в хранилище и тестовый файл
		Task task2 = new Task("Task-2", "Task2-description");
		int idtask2 = fbtm.addTask(task2);
		task2 = fbtm.getTask(idtask2);

		Task task3 = new Task("Task-3", "Task2-description");
		int idtask3 = fbtm.addTask(task3);
		task3 = fbtm.getTask(idtask3);

		// добавление новых главных задач в хранилище и тестовый файл
		MainTask maintask2 = new MainTask("MainTask-2", "MainTask2-description");
		int idmaintask2 = fbtm.addMainTask(maintask2);
		maintask2 = fbtm.getMainTask(idmaintask2);

		MainTask maintask3 = new MainTask("MainTask-3", "MainTask2-description");
		int idmaintask3 = fbtm.addMainTask(maintask3);
		maintask3 = fbtm.getMainTask(idmaintask3);

		// добавление новых подзадач в хранилища и тестовый файл
		SubTask subtask2 = new SubTask("SubTask-2", "SubTask2-description", idmaintask2);
		int idsubtask2 = fbtm.addSubTask(subtask2);
		subtask2 = fbtm.getSubTask(idsubtask2);

		SubTask subtask3 = new SubTask("SubTask-2", "SubTask2-description", idmaintask2);
		int idsubtask3 = fbtm.addSubTask(subtask3);
		subtask3 = fbtm.getSubTask(idsubtask3);

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