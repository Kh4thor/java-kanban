package main.java.model;

import java.util.Objects;

public class Task {

	protected int id;
	protected String name;
	protected String description;
	protected TaskProgress taskProgress;

	// конструктор для создания задачи
	public Task(String name, String description) {
		this.name = name;
		this.description = description;
		this.taskProgress = TaskProgress.NEW;
	}

	// конструктор для создания задачи
	protected Task(String name, String description, TaskProgress taskProgres) {
		this.name = name;
		this.description = description;
		this.taskProgress = taskProgres;
	}

	// конструктор для обновленной задачи
	public Task(int id, String name, String description, TaskProgress taskProgress) {
		this.id = id;
		this.name = name;
		this.taskProgress = taskProgress;
		this.description = description;
	}

	/*
	 * возврат типа класса через перечисление
	 */
	public TaskType getType() {
		return TaskType.TASK;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TaskProgress getTaskProgress() {
		return taskProgress;
	}

	public void setTaskProgress(TaskProgress taskProgress) {
		this.taskProgress = taskProgress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, name, taskProgress);
	}

	@Override
	public String toString() {
		return "[id=" + id + ", class=" + this.getClass().getSimpleName() + ", name=" + name + ", taskProgress="
				+ taskProgress + ", description=" + description + "]";
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
		return Objects.equals(description, other.description) && id == other.id && Objects.equals(name, other.name)
				&& taskProgress == other.taskProgress;
	}
}