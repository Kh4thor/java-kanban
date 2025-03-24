package main.java.interfaces;

import java.util.List;

import main.java.model.Task;
import main.java.model.User;

public interface UserManager {

	int add (User user);
	
	void update (User user);
	
	User getById (int id);
	
	List <User> getAll();
	
	List<Task> getUserTasks(int id);
	
	void delete (int id);
	
	TaskManager getTaskManager();
	
}
