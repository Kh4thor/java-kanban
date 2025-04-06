package server.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import main.java.model.TaskProgress;

// утилитарный класс-помощник класса Parser. Нужен он или нет?
public class Converter {

	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	public Duration convertStringToDuration(String durationStr) {
		if (durationStr == null) {
			return null;
		}
		int intDuration = convertStringToInt(durationStr);
		try {
			return Duration.ofMinutes(intDuration);
		} catch (NumberFormatException e) {
			System.out.println("Неверно задан формат поля duration: " + durationStr);
		}
		return null;
	}

	public LocalDateTime convertStringToLocalDateTime(String startTimeString) {
		if (startTimeString == null) {
			return null;
		}
		try {
			return LocalDateTime.parse(startTimeString, timeFormatter);
		} catch (NumberFormatException e) {
			System.out.println("Неверно задан формат поля startTime: " + startTimeString);
		}
		return null;
	}

	public int convertStringToInt(String intString) {
		if (intString == null) {
			return 0;
		}
		try {
			return Integer.parseInt(intString);
		} catch (NumberFormatException e) {
			System.out.println("Ожидалось число, а задано " + intString);
		}
		return -1;
	}

	public TaskProgress convertStringToTaskProgress(String status) {
		if (status == null) {
			return null;
		}
		if (status.equals("NEW")) {
			return TaskProgress.NEW;
		} else if (status.equals("IN_PROGRESS")) {
			return TaskProgress.IN_PROGRESS;
		} else if (status.equals("DONE")) {
			return TaskProgress.DONE;
		}
		return TaskProgress.UNDEFINED;
	}

}
