package main.java.interfaces;

import java.util.Map;

import main.java.model.SubTask;

public interface HasSubTask {

	// получить хранилище подзадач
	public Map<Integer, SubTask> getSubTaskMap();

	// очистить хранилище подзадач
	public void clearSubTaskMap();

	// удалить подзадачу
	public void removeSubTask(int subTaskId);

	// добавить подзадачу в хранилище
	public void addSubTaskToDepo(SubTask subTask);
}
