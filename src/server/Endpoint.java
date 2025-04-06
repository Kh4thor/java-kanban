package server;

public enum Endpoint {
	// /tasks
	GET_TASKS,

	// /tasks/{id}
	GET_TASK_BY_ID,

	// /tasks
	POST_TASK,

	// /tasks
	PUT_TASK,

	// /tasks/{id}
	DELETE_TASK_BY_ID,

	// /subtasks
	GET_SUBTASKS,

	// /tasks/{id}
	GET_SUBTASK_BY_ID,

	// /maintasks/{id}/subtasks
	GET_SUBTASKS_BY_MAINTASK_ID,

	// /subtasks
	POST_SUBTASK,

	// /subtasks
	PUT_SUBTASK,

	// /subtasks/{id}
	DELETE_SUBTASK_BY_ID,

	// /maintasks
	GET_MAINTASKS,

	// /maintasks/{id}
	GET_MAINTASK_BY_ID,

	// /maintasks
	POST_MAINTASK,

	// /maintasks
	PUT_MAINTASK,

	// /maintasks/{id}
	DELETE_MAINTASK_BY_ID,

	// /history
	GET_HISTORY,

	// /prioritized
	GET_PRIOTIZED_TASKS,

	// /tasks
	DELETE_TASKS,

	// /maintasks
	DELETE_MAINTASKS,

	// /subtasks
	DELETE_SUBTASKS,

	UNDEFINED_METHOD,

	UNDEFINED_PATH,
}
