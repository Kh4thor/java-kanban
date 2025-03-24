package main.java.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.interfaces.TaskManager;
import main.java.interfaces.UserManager;
import main.java.model.Task;
import main.java.model.User;
import main.java.utils.Managers;

public class InMemoryUserManager implements UserManager {

	private final Map<Integer, User> users = new HashMap<>();

	private final TaskManager taskManager = Managers.getDefault();

	private int generatedId = 0;

	protected int generateId() {
		return ++generatedId;
	}

	/*
	 * добавить нового пользователя
	 */
	@Override
	public int add(User user) {
		int id = generatedId;
		user.setId(id);
		try {
			User userClone = user.clone();
			users.put(userClone.getId(), userClone);
			return userClone.getId();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void update(User user) {
		try {
			User userClone = user.clone();
			users.put(userClone.getId(), userClone);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User getById(int id) {
		return users.get(id);
	}

	@Override
	public List<User> getAll() {
		return new ArrayList<User>(users.values());
	}

	@Override
	public List<Task> getUserTasks(int id) {
		return taskManager.getTasksList().stream().filter(task -> task.getUser().getId() == id).toList();
	}

	@Override
	public void delete(int id) {
		users.remove(id);
	}

	@Override
	public TaskManager getTaskManager() {
		return taskManager;
	}

}
