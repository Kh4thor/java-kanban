package main.java.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task implements Cloneable {

	private int mainTaskId;

	// конструктор для создания подзадачи с началом и временем выполнения подзадачи
	public SubTask(String name, String discription, int mainTaskId, LocalDateTime startTime, Duration duration) {
		super(name, discription, TaskProgress.NEW, startTime, duration);
		this.mainTaskId = mainTaskId;
	}

	// конструктор для обновления подзадачи без начала и времени выполнения
	// подзадачи
	public SubTask(int subtaskId, String name, String discription, int mainTaskId, TaskProgress taskProgress,
			LocalDateTime startTime, Duration duration) {
		super(subtaskId, name, discription, taskProgress, startTime, duration);
		this.mainTaskId = mainTaskId;
	}
	

	/*
	 * возврат типа класса через перечисление
	 */
	public TaskType getType() {
		return TaskType.SUBTASK;
	}

	public int getMaintaskId() {
		return mainTaskId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(mainTaskId);
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
		SubTask other = (SubTask) obj;
		return Objects.equals(description, other.description) && id == other.id && Objects.equals(name, other.name)
				&& taskProgress == other.taskProgress;
	}

	@Override
	public String toString() {
		return "[id=" + id
				+ ", class=" + this.getClass().getSimpleName()
				+ ", name=" + name
				+ ", taskProgress=" + taskProgress
				+ ", description=" + description
				+ ", maintask_id=" + getMaintaskId()
				+ ", startTime=" + getStartTime()
				+ ", duration=" + getDuration()
				+ ", endTime=" + getEndTime()
				+ "]";
	}
	
	/*
	 * метод клонирования подзадачи
	 */
	@Override
	public SubTask clone () throws CloneNotSupportedException {
		return (SubTask) super.clone();
	}
	
}
