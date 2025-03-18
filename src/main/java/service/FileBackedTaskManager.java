package main.java.service;

import java.io.File;
import java.util.List;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import main.java.model.Task;
import main.java.model.SubTask;
import main.java.model.MainTask;
import main.java.model.TaskProgress;
import main.java.model.TaskType;
import main.java.utils.ManagerSaveException;

public class FileBackedTaskManager extends InMemoryTaskManager {

	/*
	 * рабочий файл по умолчанию
	 */
	public static File defaultFile = new File("src\\sources\\data\\", "DefaultDataFBTM.csv");

	/*
	 * метод восстановления состояния класса из рабочего файла
	 */
	public static FileBackedTaskManager loadFromFile() {

		final FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

		// востановить рабочий файл в случае его отсутствия
		if (!defaultFile.exists()) {
			try {
				restoreFile(fileBackedTaskManager);
			} catch (ManagerSaveException e) {
				e.getMessage();
			}
		}
		// считать файл и записать задачи в хранилища
		try {
			readFile(fileBackedTaskManager);
		} catch (ManagerSaveException e) {
			e.getMessage();
		}
		return fileBackedTaskManager;
	}

	/*
	 * метод восстановления состояния класса из стороннего файла
	 */
	public static FileBackedTaskManager loadFromFile(File file) {
		defaultFile = file;
		return loadFromFile();
	}

	/*
	 * метод считывания файла и записи задач в хранилища
	 */
	private static void readFile(FileBackedTaskManager fileBackedTaskManager) throws ManagerSaveException {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(defaultFile))) {
			while (bufferedReader.ready()) {

				// считывание файла построчно
				String line = bufferedReader.readLine();
				if (line.equals("id,type,name,status,description,epic,startTime,duration,endTime")) {
					continue;
				}

				// конвертация строки в задачу
				Task task = convertStringToTask(line, fileBackedTaskManager);

				// определение класса задачи
				TaskType taskType = task.getType();

				// запись конвертированой задачи в хранилище
				if (taskType.equals(TaskType.TASK)) {

					// проверка задачи на валидатцию добавления в список приоритетных задач
					if (fileBackedTaskManager.isValidate(task)) {
						fileBackedTaskManager.prioritetMap.add(task);
					}
					fileBackedTaskManager.taskMap.put(task.getId(), task);

					// запись конвертированой главной задачи в хранилище
				} else if (taskType.equals(TaskType.MAINTASK)) {
					fileBackedTaskManager.mainTaskMap.put(task.getId(), (MainTask) task);

					// запись конвертированой подзадачи в хранилище
				} else if (taskType.equals(TaskType.SUBTASK)) {
					SubTask subTask = (SubTask) task;
					int mainTaskId = subTask.getMaintaskId();

					// проверка задачи на валидатцию добавления в список приоритетных задач
					if (fileBackedTaskManager.isValidate(task)) {
						fileBackedTaskManager.prioritetMap.add(task);
					}
					// поиск главной задачи подзади и запись подзадачи в хранилище
					if (fileBackedTaskManager.mainTaskMap.containsKey(mainTaskId)) {
						MainTask mainTask = fileBackedTaskManager.mainTaskMap.get(mainTaskId);
						mainTask.addSubTaskToDepo(subTask);
					}
				}
			}
		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка чтения данных из файла.");
		}
	}

	// метод востановления рабочего файла в случае его отсутствия
	private static void restoreFile(FileBackedTaskManager f) throws ManagerSaveException {

		defaultFile = new File(defaultFile.getPath());

		try {
			if (defaultFile.createNewFile()) {
				System.out.println("Файл " + defaultFile.getName() + " не найден.");
				System.out.println("Файл " + defaultFile.getName() + " создан в каталоге " + defaultFile.getPath());
			} else {
				throw new ManagerSaveException("Файл уже существует.");
			}
		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка при создании файла.");
		}
	}

	/*
	 * метод сохранения состояния мененджера в файл
	 */
	private void saveToFile() throws ManagerSaveException {
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(defaultFile))) {

			// лист хранилищ всех типов задач (задачи, главные задачи, подзадачи)
			List<List<? extends Task>> lists = List.of(getTasksList(), getMainTasksList(), getSubTasksList());

			// доавление хидера в файл
			String header = "id,type,name,status,description,epic,startTime,duration,endTime";
			bufferedWriter.append(header + "\n");

			// перебор листа хранилищ всех типов задач
			for (int i = 0; i < lists.size(); i++) {
				List<? extends Task> taskList = (List<? extends Task>) lists.get(i);

				// перебор всех задач для последующей конвертации в строку
				for (int j = 0; j < taskList.size(); j++) {
					Task task = taskList.get(j);

					// конвертация задачи в строку
					String data = convertTaskToString(task);
					// добавление строки в файл
					bufferedWriter.append(data + "\n");
				}
			}
		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка записи данных в файл.");
		}
	}

	/*
	 * метод конвертации строки в задачу
	 */
	private static Task convertStringToTask(String value, FileBackedTaskManager fileBackedTaskManager) {
		if (value != null && (!value.isEmpty() || !value.isBlank())) {
			String[] values = value.split(",");

			try {

				// считывание строк задачи
				int id = Integer.parseInt(values[0]);
				String type = values[1]; // тип задачт
				String name = values[2]; // название задачи
				String tp = values[3]; // статус прогресса задачи
				String discription = values[4]; // описание задачи
				String mTaskId = "0"; // id-главной задачи для подзадачи
				String sTime; // время начала выполнения задачи
				String dur; // продолжительность выполнения задачи (мин.)
				String eTime; // время окончания выполнения задачи
				if (type.equals("SUBTASK")) {
					mTaskId = values[5];
					sTime = values[6];
					dur = values[7];
					eTime = values[8];
				} else {
					sTime = values[5];
					dur = values[6];
					eTime = values[7];
				}

				// преобразование необходимых строк в поля задачи
				int mainTaskid = Integer.parseInt(mTaskId); // id-главной задачи для подзадачи
				LocalDateTime startTime = LocalDateTime.parse(sTime); // время начала выполнения
				Duration duration = Duration.ofMinutes(Long.parseLong(dur)); // продолжительность выполнения (мин.)
				LocalDateTime endTime = LocalDateTime.parse(eTime); // время начала выполнения

				// поиск максимального id задачи в файле
				int maxid = 0;
				if (id > maxid) {
					maxid = id;
				}
				fileBackedTaskManager.id = maxid;

				// присваивание статуса прогресса задачи
				TaskProgress taskProgress = TaskProgress.UNDEFINED;
				if (tp.equals("NEW")) {
					taskProgress = TaskProgress.NEW;
				} else if (tp.equals("IN_PROGRESS")) {
					taskProgress = TaskProgress.IN_PROGRESS;
				} else if (tp.equals("DONE")) {
					taskProgress = TaskProgress.DONE;
				}

				// создание задачи
				if (type.equals("TASK")) {
					Task task = new Task(id, name, discription, taskProgress, startTime, duration);
					task.setEndTime(endTime);
					return task;

					// создание главной задачи
				} else if (type.equals("MAINTASK")) {
					MainTask mainTask = new MainTask(id, name, discription);
					mainTask.setTaskProgress(taskProgress);
					mainTask.setDuration(duration);
					mainTask.setStartTime(startTime);
					mainTask.setEndTime(endTime);
					return mainTask;

					// создание подзадачи
				} else if (type.equals("SUBTASK")) {
					SubTask subTask = new SubTask(id, name, discription, mainTaskid, taskProgress, startTime, duration);
					subTask.setEndTime(endTime);
					return subTask;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Ошибка конвертации. Неверный формат данных строки.");
			}
		}
		return new Task(-1, "null", "from_convertStringToTask", TaskProgress.UNDEFINED, LocalDateTime.now(),
				Duration.ZERO);
	}

	/*
	 * метод конвертации задачи в строку
	 */
	private static String convertTaskToString(Task task) {

		// считывание полей всех типов задач
		String id = String.valueOf(task.getId());
		TaskType taskType = task.getType();
		String name = task.getName();
		String taskProgress = String.valueOf(task.getTaskProgress());
		String discription = task.getDescription();
		String startTime = null;
		if (task.getStartTime() != null) {
			startTime = task.getStartTime().toString();
		}

		String duration = null;
		if (task.getDuration()!= null) {
			duration = Long.toString(task.getDuration().toMinutes());
		}
		
		String endTime = null;
		if (task.getEndTime() != null) {
			endTime = task.getEndTime().toString();
		}

		// считывание id-главной задачи для подзадачи
		if (taskType == TaskType.SUBTASK) {
			SubTask subTask = (SubTask) task;
			String mainTaskId = String.valueOf(subTask.getMaintaskId());

			// создание формата записи подзадачи в файл
			String taskToString = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", id, taskType, name, taskProgress,
					discription, mainTaskId, startTime, duration, endTime);
			return taskToString;

			// создание формата записи задачи/главной задачи в файл
		} else {
			String taskToString = String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, taskType, name, taskProgress,
					discription, startTime, duration, endTime);
			return taskToString;
		}
	}

	/*
	 * добавить задачу в хранилище и сделать запись в файл
	 */
	@Override
	public Integer addTask(Task task) {

		// запись задачи в хранилище родительского класса
		int testNumber = super.addTask(task);

		// запись задачи в файл
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * добавить главную задачу в хранилище и сделать запись в файл
	 */
	@Override
	public Integer addMainTask(MainTask mainTask) {

		// запись главной задачи в хранилище родительского класса
		int testNumber = super.addMainTask(mainTask);

		// запись главной задачив файл
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * добавить подзадачу в хранилище и сделать запись в файл
	 */
	@Override
	public Integer addSubTask(SubTask subTask) {

		// запись подзадачи в хранилище родительского класса
		int testNumber = super.addSubTask(subTask);

		// запись подзадачи в файл
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * обновить задачу
	 */
	@Override
	public Integer updateTask(Task task) {

		// обновление задачи в хранилище
		int testNumber = super.updateTask(task);

		// запись задачи в файл
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * обновить главную задачу
	 */
	@Override
	public Integer updateMainTask(MainTask task) {

		// обновление задачи в хранилище
		int testNumber = super.updateMainTask(task);

		// запись задачи в файл
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * обновить подзадачу
	 */
	@Override
	public Integer updateSubTask(SubTask task) {

		// обновление задачи в хранилище
		int testNumber = super.updateSubTask(task);

		// запись задачи в файл
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * удалить задачу по id
	 */
	@Override
	public Integer deleteTaskById(int id) {

		// удаление задачи из хранилища
		int testNumber = super.deleteTaskById(id);

		// удаление задачи из файла
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * удалить главную задачу по id
	 */
	@Override
	public Integer deleteMainTaskById(int id) {

		// удаление главной задачи из хранилища
		int testNumber = super.deleteMainTaskById(id);

		// удаление главной задачи из файла
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * удалить подзадачу по id
	 */
	@Override
	public Integer deleteSubTaskById(int id) {

		// удаление подзадачи из хранилища
		int testNumber = super.deleteSubTaskById(id);

		// удаление подзадачи из файла
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * удалить все задачи
	 */
	@Override
	public Integer deleteAllTasks() {
		int testNumber = -1;

		// удаление всех задач из хранилища
		try {
			testNumber = super.deleteAllTasks();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// удаление всех задач из файла
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * удалить все главные задачи
	 */
	@Override
	public Integer deleteAllMainTasks() {
		int testNumber = -1;

		// удаление всех главных задач из хранилища
		try {
			testNumber = super.deleteAllMainTasks();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// удаление всех задач из файла
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * удалить все подзадачи
	 */
	@Override
	public Integer deleteAllSubTasks() {
		int testNumber = -1;

		// удаление всех подзадач из хранилища
		try {
			testNumber = super.deleteAllSubTasks();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// удаление всех подзадач из файла
		try {
			saveToFile();
		} catch (ManagerSaveException e) {
			System.out.println(e.getMessage());
		}
		return testNumber;
	}

	/*
	 * очистить все хранилища
	 */
	@Override
	public Integer clearAllDepos() {
		int testNumber = -1;

		// удаление всех типов задач из хранилищ
		try {
			testNumber = super.clearAllDepos();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// удаление рабочего файла
		defaultFile.delete();

		// создание нового рабочего файла
		defaultFile = new File(defaultFile.getPath());
		try {
			if (defaultFile.createNewFile()) {
				System.out.println("Хранилища и рабочий файл очищены.");
			} else {
				System.out.println("Неудачная попытка очистить файл.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return testNumber;
	}
}
