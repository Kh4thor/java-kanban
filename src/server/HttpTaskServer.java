package server;

import java.util.Optional;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import main.java.model.Task;
import main.java.model.MainTask;
import main.java.model.SubTask;
import main.java.utils.Managers;
import server.utils.Converter;
import main.java.interfaces.TaskManager;

public class HttpTaskServer {

	private Gson gson;
	private HttpServer server;
	private TaskManager taskmanager;
	Converter converter;

	private static final int PORT = 8080;

	public HttpTaskServer() {
		this(Managers.getDefault());
	}

	public HttpTaskServer(TaskManager taskmanager) {
		gson = Managers.getGson();
		converter = new Converter();
		this.taskmanager = taskmanager;
		try {
			server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
		} catch (IOException e) {
			System.out.println("Ошибка при создании сервера");
		}
		server.createContext("/tasks", this::commandHandler);
		server.createContext("/maintasks", this::commandHandler);
		server.createContext("/subtasks", this::commandHandler);
		server.createContext("/history", this::commandHandler);
		server.createContext("/prioritized", this::commandHandler);
	}

	public void start() {
		server.start();
		System.out.println("Сервер запущен на порту " + PORT);
	}

	public void stop() {
		server.stop(0);
		System.out.println("Сервер остановлен на порту " + PORT);
	}

	protected void commandHandler(HttpExchange httpExchange) throws IOException {

		String requestMethod = httpExchange.getRequestMethod();
		String requestPath = httpExchange.getRequestURI().getPath();

		Endpoint endpoint = getEndpoint(requestMethod, requestPath);

		switch (endpoint) {

		case POST_TASK:
			postTaskHandle(httpExchange, endpoint);
			break;
		case POST_MAINTASK:
			postTaskHandle(httpExchange, endpoint);
			break;
		case POST_SUBTASK:
			postTaskHandle(httpExchange, endpoint);
			break;
		case PUT_TASK:
			putTaskHandle(httpExchange, endpoint);
			break;
		case PUT_MAINTASK:
			putTaskHandle(httpExchange, endpoint);
			break;
		case PUT_SUBTASK:
			putTaskHandle(httpExchange, endpoint);
			break;
		case GET_TASKS:
			getTasksHandler(httpExchange, endpoint);
			break;
		case GET_MAINTASKS:
			getTasksHandler(httpExchange, endpoint);
			break;
		case GET_SUBTASKS:
			getTasksHandler(httpExchange, endpoint);
			break;
		case GET_TASK_BY_ID:
			getTaskByIdHandler(httpExchange, endpoint);
			break;
		case GET_MAINTASK_BY_ID:
			getTaskByIdHandler(httpExchange, endpoint);
			break;
		case GET_SUBTASK_BY_ID:
			getTaskByIdHandler(httpExchange, endpoint);
			break;
		case DELETE_TASKS:
			deleteTasksHandler(httpExchange, endpoint);
			break;
		case DELETE_MAINTASKS:
			deleteTasksHandler(httpExchange, endpoint);
			break;
		case DELETE_SUBTASKS:
			deleteTasksHandler(httpExchange, endpoint);
			break;
		case DELETE_TASK_BY_ID:
			deleteTaskByIdHandler(httpExchange, endpoint);
			break;
		case DELETE_MAINTASK_BY_ID:
			deleteTaskByIdHandler(httpExchange, endpoint);
			break;
		case DELETE_SUBTASK_BY_ID:
			deleteTaskByIdHandler(httpExchange, endpoint);
			break;
		case GET_SUBTASKS_BY_MAINTASK_ID:
			getSubtasksByMaintaskIdHandler(httpExchange);
			break;
		case GET_HISTORY:
			getHistoryHandler(httpExchange);
			break;
		case GET_PRIOTIZED_TASKS:
			getPrioritizedHandler(httpExchange);
			break;

		case UNDEFINED_PATH:
			String response = "Неверный адрес запроса: " + requestPath;
			writeResponse(httpExchange, response, 400);

		default:
			response = "Метод " + requestMethod + " не поддерживается";
			writeResponse(httpExchange, response, 405);
		}
	}

	private void postTaskHandle(HttpExchange httpExchange, Endpoint endpoint) {

		String requestBody = readRequestBody(httpExchange);

		int id = -1;
		int code = 406;
		String response = "Задача пересекается с существующими";

		switch (endpoint) {
		case POST_TASK:
			Task task = gson.fromJson(requestBody, Task.class);

			id = taskmanager.addTask(task);
			code = 200;
			if (taskmanager.isValidateToAddTaskOrSubTaskToPrioritetSet(task)) {
				if (id > 0) {
					response = "Задача добавлена";
				} else {
					response = "Задача не прошла валидацию и добавлена не была";
				}
			}
			code = 406;
			break;

		case POST_MAINTASK:
			MainTask maintask = gson.fromJson(requestBody, MainTask.class);
			id = taskmanager.addMainTask(maintask);
			code = 200;
			if (id > 0) {
				response = "Задача добавлена";
			} else {
				response = "Задача не прошла валидацию и добавлена не была";
			}
			break;

		case POST_SUBTASK:
			SubTask subtask = gson.fromJson(requestBody, SubTask.class);
			id = taskmanager.addSubTask(subtask);
			code = 200;
			if (taskmanager.isValidateToAddTaskOrSubTaskToPrioritetSet(subtask)) {
				if (id > 0) {
					response = "Задача добавлена";
				} else {
					response = "Задача не прошла валидацию и добавлена не была";
				}
			}
			break;

		default:
			response = "Неверный запрос " + endpoint;
			code = 500;
			break;
		}

		writeResponse(httpExchange, response, code);
	}

	private void putTaskHandle(HttpExchange httpExchange, Endpoint endpoint) {
		String requestBody = readRequestBody(httpExchange);

		int id = -1;
		int code = 406;
		String response = "Задача пересекается с существующими";

		switch (endpoint) {

		case PUT_TASK:
			Task task = gson.fromJson(requestBody, Task.class);

			id = taskmanager.updateTask(task);
			code = 200;
			if (taskmanager.isValidateToAddTaskOrSubTaskToPrioritetSet(task)) {
				if (id > 0) {
					code = 200;
					response = "Задача обновлена";
				} else {
					code = 200;
					response = "Задача не прошла валидацию и не была обновлена";
				}
			}
			break;

		case PUT_MAINTASK:
			MainTask maintask = gson.fromJson(requestBody, MainTask.class);
			id = taskmanager.updateMainTask(maintask);
			code = 200;
			if (id > 0) {
				code = 200;
				response = "Задача обновлена";
			} else {
				code = 200;
				response = "Задача не прошла валидацию и не была обновлена";
			}
			break;

		case PUT_SUBTASK:
			SubTask subtask = gson.fromJson(requestBody, SubTask.class);
			id = taskmanager.updateSubTask(subtask);
			code = 200;
			if (taskmanager.isValidateToAddTaskOrSubTaskToPrioritetSet(subtask)) {
				if (id > 0) {
					code = 200;
					response = "Задача обновлена";
				} else {
					code = 200;
					response = "Задача не прошла валидацию и не была обновлена";
				}
			}
			break;

		default:
			response = "Неверный запрос " + endpoint;
			code = 500;
			break;
		}

		writeResponse(httpExchange, response, code);
	}

	private void getTasksHandler(HttpExchange httpExchange, Endpoint endpoint) {
		try {
			String response = "";
			int code = 200;

			switch (endpoint) {
			case GET_TASKS:
				response = taskmanager.getTasksList().stream().map(Task::toString).collect(Collectors.joining(","));
				break;

			case GET_MAINTASKS:
				response = taskmanager.getMainTasksList().stream().map(MainTask::toString)
						.collect(Collectors.joining(","));
				break;

			case GET_SUBTASKS:
				response = taskmanager.getSubTasksList().stream().map(SubTask::toString)
						.collect(Collectors.joining(","));
				break;

			default:
				response = "Неверный запрос " + endpoint;
				code = 500;
				break;
			}

			if (response.isEmpty()) {
				response = "Список задач пуст";
				code = 404;
			}
			writeResponse(httpExchange, response, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getTaskByIdHandler(HttpExchange httpExchange, Endpoint endpoint) {
		try {
			int id = getIdfromPath(httpExchange);
			int code = 404;
			String response = "Задача с id=" + id + " не найдена";

			switch (endpoint) {
			case GET_TASK_BY_ID:
				Optional<Task> taskOpt = taskmanager.getTask(id);
				if (taskOpt.isPresent()) {
					response = taskOpt.get().toString();
					code = 200;
					break;
				}

			case GET_MAINTASK_BY_ID:
				Optional<MainTask> maintaskOpt = taskmanager.getMainTask(id);
				if (maintaskOpt.isPresent()) {
					response = maintaskOpt.get().toString();
					code = 200;
				}
				break;

			case GET_SUBTASK_BY_ID:
				Optional<SubTask> subtaskOpt = taskmanager.getSubTask(id);
				if (subtaskOpt.isPresent()) {
					response = subtaskOpt.get().toString();
					code = 200;
				}
				break;

			default:
				response = "Неверный запрос " + endpoint;
				code = 400;
				break;
			}

			writeResponse(httpExchange, response, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteTasksHandler(HttpExchange httpExchange, Endpoint endpoint) {
		try {
			int id = -1;
			int code = 200;
			String response = "Все задачи удалены";

			switch (endpoint) {
			case DELETE_TASKS:
				id = taskmanager.deleteAllTasks();
				break;

			case DELETE_MAINTASKS:
				id = taskmanager.deleteAllMainTasks();
				break;

			case DELETE_SUBTASKS:
				id = taskmanager.deleteAllSubTasks();
				break;

			default:
				response = "Неверный запрос " + endpoint;
				code = 500;
				break;
			}

			if (id == -1) {
				code = 500;
				response = "Ошибка удаления.";
			}
			writeResponse(httpExchange, response, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteTaskByIdHandler(HttpExchange httpExchange, Endpoint endpoint) {
		try {
			int id = getIdfromPath(httpExchange);
			int taskId = -1;
			String response = "Задача с id= " + id + " удалена";
			int code = 200;

			switch (endpoint) {

			case DELETE_TASK_BY_ID:
				taskId = taskmanager.deleteTaskById(id);
				break;

			case DELETE_MAINTASK_BY_ID:
				taskId = taskmanager.deleteMainTaskById(id);
				break;

			case DELETE_SUBTASK_BY_ID:
				taskId = taskmanager.deleteSubTaskById(id);
				break;

			default:
				response = "Неверный запрос " + endpoint;
				code = 400;
				break;
			}

			if (taskId == -1) {
				response = "Ошибка удаления. Задача с id=" + id + " не найдена";
			}
			writeResponse(httpExchange, response, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getSubtasksByMaintaskIdHandler(HttpExchange httpExchange) {
		try {
			int code = 200;
			int id = getIdfromPath(httpExchange);
			String response = taskmanager.getSubTaskListByMainTask(id).stream().map(SubTask::toString)
					.collect(Collectors.joining(","));

			if (response.isEmpty()) {
				response = "Список подзадач пуст";
				code = 404;
			}
			writeResponse(httpExchange, response, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getHistoryHandler(HttpExchange httpExchange) {
		try {
			int code = 200;
			String response = taskmanager.getHistory().stream().map(task -> task.toString())
					.collect(Collectors.joining(","));

			if (response.isEmpty()) {
				response = "Список пуст";
				code = 404;
			}
			writeResponse(httpExchange, response, code);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getPrioritizedHandler(HttpExchange httpExchange) {
		try {
			int code = 200;
			String response = taskmanager.getPrioritizedTasks().stream().map(task -> task.toString())
					.collect(Collectors.joining(","));

			if (response.isEmpty()) {
				response = "Список пуст";
				code = 404;
			}
			writeResponse(httpExchange, response, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getIdfromPath(HttpExchange httpExchange) {
		String stringId = null;
		try {
			String requestPath = httpExchange.getRequestURI().getPath();
			String[] requestPathSplit = requestPath.split("/");
			stringId = requestPathSplit[2];
			int id = Integer.parseInt(stringId);
			return id;
		} catch (NumberFormatException e) {
			String response = ("Неверно указан id задачи. Ожидалось число, а передается: " + stringId);
			writeResponse(httpExchange, response, 400);
		}
		return -1;
	}

	private void writeResponse(HttpExchange httpExchange, String response, int responseCode) {
		try {
			String responseJson = gson.toJson(response);
			byte[] responseBytes = responseJson.getBytes();
			Headers headers = httpExchange.getResponseHeaders();
			headers.set("Content-Type", "application/json;charset=utf-8");

			httpExchange.sendResponseHeaders(responseCode, responseBytes.length);
			try (OutputStream outputStream = httpExchange.getResponseBody()) {
				outputStream.write(responseBytes);
			}
		} catch (IOException e) {
			System.err.println("Ошибка отправки ответа: " + e.getMessage());
		} finally {
			httpExchange.close();
		}
	}

	private String readRequestBody(HttpExchange exchange) {
		try (InputStream inputStream = exchange.getRequestBody()) {
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			while (bufferedReader.ready()) {
				String string = bufferedReader.readLine();
				stringBuilder.append(string);
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Endpoint getEndpoint(String requestMetod, String requestPath) {

		switch (requestMetod) {
		case "GET":
			if (Pattern.matches("^/tasks", requestPath)) {
				return Endpoint.GET_TASKS;
			}
			if (Pattern.matches("^/tasks/\\d+", requestPath)) {
				return Endpoint.GET_TASK_BY_ID;
			}
			if (Pattern.matches("^/maintasks", requestPath)) {
				return Endpoint.GET_MAINTASKS;
			}
			if (Pattern.matches("^/maintasks/\\d+", requestPath)) {
				return Endpoint.GET_MAINTASK_BY_ID;
			}
			if (Pattern.matches("^/subtasks", requestPath)) {
				return Endpoint.GET_SUBTASKS;
			}
			if (Pattern.matches("^/subtasks/\\d+", requestPath)) {
				return Endpoint.GET_SUBTASK_BY_ID;
			}
			if (Pattern.matches("^/prioritized", requestPath)) {
				return Endpoint.GET_PRIOTIZED_TASKS;
			}
			if (Pattern.matches("^/history", requestPath)) {
				return Endpoint.GET_PRIOTIZED_TASKS;
			}
			return Endpoint.UNDEFINED_PATH;

		case "POST":
			if (Pattern.matches("^/tasks", requestPath)) {
				return Endpoint.POST_TASK;
			}
			if (Pattern.matches("^/maintasks", requestPath)) {
				return Endpoint.POST_MAINTASK;
			}
			if (Pattern.matches("^/subtasks", requestPath)) {
				return Endpoint.POST_SUBTASK;
			}
			return Endpoint.UNDEFINED_PATH;

		case "PUT":
			if (Pattern.matches("^/tasks", requestPath)) {
				return Endpoint.PUT_TASK;
			}
			if (Pattern.matches("^/maintasks", requestPath)) {
				return Endpoint.PUT_MAINTASK;
			}
			if (Pattern.matches("^/subtasks", requestPath)) {
				return Endpoint.PUT_SUBTASK;
			}
			return Endpoint.UNDEFINED_PATH;

		case "DELETE":
			if (Pattern.matches("^/tasks", requestPath)) {
				return Endpoint.DELETE_TASKS;
			}
			if (Pattern.matches("^/maintasks", requestPath)) {
				return Endpoint.DELETE_MAINTASKS;
			}
			if (Pattern.matches("^/subtasks", requestPath)) {
				return Endpoint.DELETE_SUBTASKS;
			}
			if (Pattern.matches("^/tasks/\\d+", requestPath)) {
				return Endpoint.DELETE_TASK_BY_ID;
			}
			if (Pattern.matches("^/subtasks/\\d+", requestPath)) {
				return Endpoint.DELETE_SUBTASK_BY_ID;
			}
			if (Pattern.matches("^/subtasks/\\d+", requestPath)) {
				return Endpoint.DELETE_SUBTASK_BY_ID;
			}
			return Endpoint.UNDEFINED_PATH;
		}
		return Endpoint.UNDEFINED_METHOD;
	}
}
