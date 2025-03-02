package main.java.model;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import main.java.interfaces.HasSubTask;

public class MainTask extends Task implements HasSubTask {

	private Map<Integer, SubTask> subtaskMap = new HashMap<>();

	/*
	 * конструктор для созлания главной задачи
	 */
	public MainTask(String name, String discription) {
		super(name, discription, TaskProgress.NEW);
	}

	/*
	 * конструктор для обновления главной задачи
	 */
	public MainTask(int id, String name, String discription) {
		super(id, name, discription, TaskProgress.NEW);
	}
	
	/*
	 * возврат типа класса через перечисление
	 */
	public TaskType getType() {
		return TaskType.MAINTASK;
	}

	/*
	 * очистить хранилище подзадач
	 */
	@Override
	public void clearSubTaskMap() {
		if (subtaskMap != null) {
			subtaskMap.clear();
		}
	}

	/*
	 * удалить подзадачу из хранилища
	 */
	@Override
	public void removeSubTask(int subTaskId) {
		if (subTaskId > 0 && subtaskMap.containsKey(subTaskId)) {
			subtaskMap.remove(subTaskId);
		}
	}

	/*
	 * добавить подзадачу в хранилище
	 */
	@Override
	public void addSubTaskToDepo(SubTask subTask) {
		subtaskMap.put(subTask.getId(), subTask);
	}

	/*
	 * получить хранилище подзадач
	 */
	@Override
	public Map<Integer, SubTask> getSubTaskMap() {
		return subtaskMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(subtaskMap);
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

}
