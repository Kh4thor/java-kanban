package server.utils;

import java.net.URI;
import java.util.Map;
import java.util.HashMap;
import java.time.Duration;
import java.util.Optional;
import java.time.LocalDateTime;

import com.sun.net.httpserver.HttpExchange;

import main.java.model.TaskProgress;

// класс для считывания полей залачи из запроса. Нужен он или нет?
public class Parser {

	Converter converter = new Converter();

	private Map<String, Optional<String>> queryMap = new HashMap<>();

	public Map<String, Optional<String>> parseRequestStringToParameters(HttpExchange httpExchange) {

		queryMap.clear();

		URI uri = httpExchange.getRequestURI();
		String query = uri.getQuery();
		if (query != null) {

			String[] querySplit = query.split("&");

			String id = null;
			String name = null;
			String type = null;
			String description = null;
			String taskProgress = null;
			String mainTaskId = null;
			String startTime = null;
			String duration = null;

			for (int i = 0; i < querySplit.length; i++) {
				String substring = querySplit[i].substring(0, 1 + querySplit[i].indexOf("="));

				// считывание параметров задачи вне зависимости от последовательности их записи
				switch (substring) {
				case "id=":
					id = querySplit[i].substring(3);
					continue;
				case "type=":
					type = querySplit[i].substring(5);
					continue;
				case "name=":
					name = querySplit[i].substring(5);
					continue;
				case "description=":
					description = querySplit[i].substring(12);
					continue;
				case "taskProgress=":
					taskProgress = querySplit[i].substring(13);
					continue;
				case "mainTaskId=":
					mainTaskId = querySplit[i].substring(5);
					continue;
				case "startTime=":
					startTime = querySplit[i].substring(10);
					continue;
				case "duration=":
					duration = querySplit[i].substring(9);
					continue;
				}
			}
			queryMap.put("id", Optional.ofNullable(id));
			queryMap.put("type", Optional.ofNullable(type));
			queryMap.put("name", Optional.ofNullable(name));
			queryMap.put("description", Optional.ofNullable(description));
			queryMap.put("taskProgress", Optional.ofNullable(taskProgress));
			queryMap.put("mainTaskId", Optional.ofNullable(mainTaskId));
			queryMap.put("startTime", Optional.ofNullable(startTime));
			queryMap.put("duration", Optional.ofNullable(duration));
		}
		return queryMap;
	}

	public int parseId(Map<String, Optional<String>> queryMap) {
		Optional<String> idOpt = queryMap.get("id");
		if (idOpt.isPresent()) {
			return converter.convertStringToInt(idOpt.get());
		}
		return 0;
	}

	public String parseName(Map<String, Optional<String>> queryMap) {
		Optional<String> nameOpt = queryMap.get("name");
		if (nameOpt.isPresent()) {
			return nameOpt.get();
		}
		return null;
	}

	public String parseDescription(Map<String, Optional<String>> queryMap) {
		Optional<String> descriptionOpt = queryMap.get("description");
		if (descriptionOpt.isPresent()) {
			return descriptionOpt.get();
		}
		return null;
	}

	public TaskProgress parseTaskProgress(Map<String, Optional<String>> queryMap) {
		Optional<String> taskProgressOpt = queryMap.get("taskProgress");
		if (taskProgressOpt.isPresent()) {
			return converter.convertStringToTaskProgress(taskProgressOpt.get());
		}
		return TaskProgress.UNDEFINED;
	}

	public LocalDateTime parseLocalDateTime(Map<String, Optional<String>> queryMap) {
		Optional<String> startTimeOpt = queryMap.get("startTime");
		if (startTimeOpt.isPresent()) {
			String stringStartTime = startTimeOpt.get();
			return converter.convertStringToLocalDateTime(stringStartTime);
		}
		return null;
	}

	public Duration parseDuration(Map<String, Optional<String>> queryMap) {
		Optional<String> durationOpt = queryMap.get("duration");
		if (durationOpt.isPresent()) {
			String stringDuration = durationOpt.get();
			return converter.convertStringToDuration(stringDuration);
		}
		return null;
	}
}
