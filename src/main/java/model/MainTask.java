package main.java.model;

import java.util.Map;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import main.java.interfaces.HasSubTask;

public class MainTask extends Task implements HasSubTask, Cloneable {

	private Map<Integer, SubTask> subTaskMap = new HashMap<>();

	// конструктор для создания главной задачи
	public MainTask(String name, String discription) {
		super(name, discription, TaskProgress.NEW, null, null);
	}

	// конструктор для обновления главной задачи
	public MainTask(int id, String name, String discription) {
		super(id, name, discription, TaskProgress.NEW, null, null);
	}

	// возврат типа класса через перечисление
	public TaskType getType() {
		return TaskType.MAINTASK;
	}

	
	/*
	 * 	получить время окончания выполнения задачи
	 */
	@Override
	public LocalDateTime getEndTime() {
		if (subTaskMap != null && !subTaskMap.isEmpty()) {

			Optional<SubTask> optionalSubTask = subTaskMap.values().stream()
					.max(Comparator.comparing(SubTask::getEndTime));

			if (optionalSubTask.isPresent()) {
				return optionalSubTask.get().getEndTime();
			}
		}
		return null;
	}

	/*
	 * очистить хранилище подзадач
	 */
	@Override
	public void clearSubTaskMap() {
		if (subTaskMap != null) {
			subTaskMap.clear();
		}
	}

	/*
	 * удалить подзадачу из хранилища
	 */
	@Override
	public void removeSubTask(int subTaskId) {
		if (subTaskId > 0 && subTaskMap.containsKey(subTaskId)) {
			subTaskMap.remove(subTaskId);
		}
	}

	/*
	 * добавить подзадачу в хранилище
	 */
	@Override
	public void addSubTaskToDepo(SubTask subTask) {
		subTaskMap.put(subTask.getId(), subTask);
	}

	/*
	 * получить хранилище подзадач
	 */
	@Override
	public Map<Integer, SubTask> getSubTaskMap() {
		return subTaskMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(subTaskMap);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MainTask other = (MainTask) obj;
		return Objects.equals(description, other.description) && id == other.id && Objects.equals(name, other.name)
				&& taskProgress == other.taskProgress;
	}
	
	/*
	 * метод клонирования главной задачи
	 */
	@Override
	public MainTask clone() throws CloneNotSupportedException {
		return (MainTask) super.clone();
	}
}
