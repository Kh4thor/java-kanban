package main.java.model;

import java.util.Objects;

public class SubTask extends Task {

	private int mainTaskId;

	/*
	 * конструктор для создания подзадачи
	 */
	public SubTask(String name, String discription, int mainTaskId) {
		super(name, discription, TaskProgress.NEW);
		this.mainTaskId = mainTaskId;
	}

	/*
	 * конструкторо для обновления подзадачи
	 */
	public SubTask(int subtaskId, String name, String discription, int mainTaskId, TaskProgress taskProgress) {
		super(subtaskId, name, discription, taskProgress);
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
		return "[id=" + id + ", class=" + this.getClass().getSimpleName() + ", name=" + name + ", taskProgress="
				+ taskProgress + ", description=" + description + ", maintask_id=" + getMaintaskId() + "]";
	}
}
