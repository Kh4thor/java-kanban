package main.java;

import main.java.model.MainTask;
import main.java.model.SubTask;
import main.java.model.Task;
import main.java.service.FileBackedTaskManager;
import main.java.utils.Managers;
import main.java.interfaces.TaskManager;

import java.io.File;

import org.junit.jupiter.api.Assertions;

public class Main {

	public static void main(String[] args) throws Exception {

		/*
		 * организация работы с файлом
		 */

		// тестовый файл с указанным именем не существует
		File file = new File("TestDataFBTM.csv");

		// менеджером задач создатся новый тестовый файл
		TaskManager fbtm = Managers.getDefaultFileBackedTaskManager(file);

		// тестовый файл задан в качестве файла по умолчанию
		file = FileBackedTaskManager.defaultFile;

		// запись новой главной задачи в хранилище и тестовый файл
		MainTask maintask1 = new MainTask("MainTask-1", "MainTask1-description");
		int id_maintask1 = fbtm.addMainTask(maintask1);
		maintask1 = fbtm.getMainTask(id_maintask1);

		System.out.println("***1" + maintask1);

		// все хранилища очищены
		fbtm.clearAllDepos();

		/*
		 * чтение задач из cуществующего тестового файла
		 */
		fbtm = FileBackedTaskManager.loadFromFile(file);

		System.out.println("***2" + maintask1);

		System.out.println(fbtm.getMainTask(2));
		fbtm.getMainTasksList().forEach(e -> System.out.println(e));

		// тестовый файл очищен
		FileBackedTaskManager.clear();

//		// тестовый файл удален
//		Assertions.assertTrue(file.delete());
//		Assertions.assertTrue(!file.exists());

	}
}
