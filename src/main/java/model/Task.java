package main.java.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Cloneable {

	protected int id; // уникальный идентификационный номер задачи
	protected String name; // название задачи
	protected String description; // описание задачи
	protected TaskProgress taskProgress; // статус выполнения задачи
	protected LocalDateTime startTime; // время начала выполнения задачи
	protected Duration duration; // продолжительность выполнения задачи
	protected LocalDateTime endTime; // время окончания выполнения задачи

	// конструктор для создания задачи с указанием начала и времени выполнения
	// задачи
	public Task(String name, String description, LocalDateTime startTime, Duration duration) {
		this.name = name;
		this.description = description;
		this.taskProgress = TaskProgress.NEW;
		this.startTime = startTime;
		this.duration = duration;
	}

	// конструктор для обновления задачи с указанием начала и времени выполнения
	// задачи
	public Task(int id, String name, String description, TaskProgress taskProgress, LocalDateTime startTime,
			Duration duration) {
		this.id = id;
		this.name = name;
		this.taskProgress = taskProgress;
		this.description = description;
		this.startTime = startTime;
		this.duration = duration;
	}

	// конструктор для обновления задачи с указанием начала и времени выполнения
	// задачи для MainTask
	protected Task(String name, String description, TaskProgress taskProgress, LocalDateTime startTime,
			Duration duration) {
		this.name = name;
		this.taskProgress = taskProgress;
		this.description = description;
		this.startTime = startTime;
		this.duration = duration;
	}
	
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	
	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	/*
	 * возврат типа класса через перечисление
	 */
	public TaskType getType() {
		return TaskType.TASK;
	}
	
	/*
	 * получить id задачи
	 */
	public int getId() {
		return id;
	}
	
	/*
	 * установить id задачи
	 */
	public void setId(int id) {
		this.id = id;
	}

	/*
	 * получить имя задачи
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * установить имя задачи
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * получить статус прогресса выполения задачи
	 */
	public TaskProgress getTaskProgress() {
		return taskProgress;
	}
	
	/*
	 * установить статус прогресса выполения задачи
	 */
	public void setTaskProgress(TaskProgress taskProgress) {
		this.taskProgress = taskProgress;
	}
	
	/*
	 * получить описание задачи
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * установить описание задачи
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 *  получить время окончания выполнения задачи
	 */
	public LocalDateTime getEndTime() {
		if (startTime != null) {
			return startTime.plus(duration);
		}
		return null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, name, taskProgress);
	}

	@Override
	public String toString() {
		return "[id=" + id 
				+ ", class=" + this.getClass().getSimpleName() 
				+ ", name=" + name 
				+ ", taskProgress="	+ taskProgress 
				+ ", description=" + description
				+ ", startTime=" + getStartTime()
				+ ", duration=" + getDuration()
				+ ", endTime=" + getEndTime()
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		return Objects.equals(description, other.description) 
				&& id == other.id 
				&& Objects.equals(name, other.name)
				&& taskProgress == other.taskProgress;
	}
	/*
	 * метод клонирования задачи
	 */
	@Override
	public Task clone () throws CloneNotSupportedException {
		return (Task) super.clone();
	}
}