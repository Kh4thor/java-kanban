Ирек, спасибо за терпение! )


*****************************************************************************************************************************************

Изменения 21.03.2025

1.	Добавлен фильтр в stream() метода isValidate(Task task), сравнивающий id-задачи на случай, если такая задача есть в листе приоритетных
	задач;
2.	В тело метода clearAllDepos() добавлен методы удаления всех задач prioritetSet.removeAll(getTasksList()) и подзадач 
	prioritetSet.removeAll(getSubTasksList()) из списка приоритетных задач;
3.	В целях улучшения читаемости кода методов добавления и обновления всех типов задач, условия для работы основной логики вынесены в 
	отдельные методы isValidateToAdd...(T t) и isValidateToUpdate...(T t)
4.	Дополнена логика checkMainTaskEndTime(MainTask maintask) на случай, если !optionalSubTask.isPresent()

*****************************************************************************************************************************************

Изменения 19.03.2025

InMemoryTaskManager (src\main\java\service\)
1.	Из метода deleteAllMainTasks() убран метод удаления всех главных задач из приоритеного списка задач.
2.	В методе deleteAllSubTasks() из цикла перебора главных задач убран метод удаления всех подзадач из списка приоритетных задач;
3.	В тело метода clearAllDepos() добавлены методы удаления всех задач и подзадач из списка приоритетных задач;
4.	В методе isValidate() добавлен фильтр по id входящей задачи;
5.	Возвращена логика метода обновления updateMainTask (MainTask maintask) главной задачи в хранилище главных задач.
6.	В методе checkMainTaskStartTime(MainTask maintask) дополнена логика на случай, если у главной задачи не задано поле startTime.

*****************************************************************************************************************************************

Изменения 19.03.2025

Task (src\main\java\model\)
1.	Убрано полe endTime;
2.	Добавлены конструкторы для создания и обновления задач в случае, если время начала выполения и продолжительность задачи не заданы;

MainTask (src\main\java\model\)
1.	Добавлено полe endTime с модификатором доступа protected;
2.	Добавлены getter и setter для поля endTime;

InMemoryTaskManager (src\main\java\service\)
1.	Изменено название списка приоритетных задач с prioritetMap на prioritetSet;
2.	Добавлен метод checkMainTaskEndTime(), отслеживающий время окончания выполнения главной задачи по подзадачам;
3.	Обновлен метод checkMainTaskStatus(MainTask maintask). В тело метода добавлен метод  checkMainTaskEndTime();
4.	Изменена логика работы метода addTask(Task task). Теперь, если задача пересекается с другими, то ей вообще не присваивается id и она
	не пишется ни в одно хранилище;
5.	Изменена логика работы метода addSubTask(SubTask subTask). Теперь, если задача пересекается с другими, то ей вообще не присваивается
	id и она не пишется ни в одно хранилище;
6.	В методе addSubTask(SubTask subTask) восстановлена проверка на существование главной задачи для добавляемой подзадачи;
7.	В методе addSubTask(SubTask subTask) убрано задвоение кода на валидацию;
8.	В методе updateTak(Task task) убрана лишняя проверка на наличие обновляемой задачи в prioritetSet;
9.	В методе updateSubTak(SubTask subTask) убрана лишняя проверка на наличие обновляемой задачи в prioritetSet;
10.	Исправлена ошибка логики метода updateMainTask(MainTask maintask), связанная с остутствием метода добавления обновленной главной
	задачи в хранилище;
11.	Обновлен метод deleteTaskById(int id). Добавлен метод удаления задачи из списка приоритетных задач;
12.	Обновлен метод deleteMainTaskById(int id). Добавлены методы удаления главной задачи и ее подзадач из списка приоритетных задач;
13.	Обновлен метод deleteSubTaskById(int id). Добавлен метод удаления подзадачи из списка приоритетных задач;
14.	Обновлен метод deleteAllTasks(). Добавлены методы удаления всех задач, главных задач и подзадач из списка приоритетных задач;
15.	Обновлен метод deleteAllMainTasks(). Добавлены методы удаления всех главных задач и подзадач из списка приоритетных задач;
16.	Обновлен метод deleteAllSubTasks(). Добавлен метод удаления всех подзадач из списка приоритетных задач;
17.	Изменен синтаксис внутри метода isValidate(Task task). Метод (!anyMatch) заменен на (noneMatch);
18.	Исправлена ошибка логики метода comparare(Task t1, Task t2), использующая метод isValidate(). Теперь метод имеет свою логику
	сравнения задач по полю startTime;
18.	Название метода comparare(Task t1, Task t2) исправлено на compare(Task t1, Task t2);

*****************************************************************************************************************************************

Изменения 18.03.2025

InMemoryTaskManager (src\main\java\service\)
1.	Добалено хранилище приоритетных задач, отсортированных по полю startTime;
2. 	Добавлен метод вычисления времени начала выполнения главной задачи checkMainTaskStartTime(Maintask maintask)
3. 	Добавлен метод вычисления продолжительности главной задачи checkMainTaskDuration(Maintask maintask)
4.	Добавлен метод checkMainTaskStatus, отслеживающий статус прогресса выполнения главной задачи, ее времени начала и продолжительности
	выполнения; 
5.	Добавлен метод валидации isValidete(Task task), сравнивающий task со всеми задачами коллекции приоритетных задач prioritetMap
	на их пересечение во времени по выполнению;
6.	Добавлен метод isTasksIntersectInTime() для проверки пересечения задач во времени по их выполнению;
7.	Добавлен метод getPrioritizedTasks() для получения списка приоритеных задач;
8.	Переписан метод getSubTasksList(). В теле метода переписан цикл forEach() на stream();
9.	Переписан метод getMainTasksList(). В теле метода переписан цикл forEach() на stream();
10.	Переписан метод getTasksList(). В теле метода переписан цикл forEach() на stream();
11.	В методе getSubTask(int id) изменен тип возвращаемой перепеменной с Task на Optional<Task>. В теле метода переписан цикл forEach()
	на stream();
12.	В методе getMainTask(int id) изменен тип возвращаемой перепеменной с Task на Optional<Task>. В теле метода переписан цикл forEach()
	на stream();
13.	В методе getTask(int id) изменен тип возвращаемой перееменной с Task на Optional<Task>. В теле метода переписан цикл forEach()
	на stream();
14.	Метод updateSubTask(SubTask subtask) определяет валидацию и обновляет subtask в хранилище приоритетных задач;
15.	Метод updateTask(Task task) определяет валидацию и добавляет mainTask в хранилище приоритетных задач;
16.	Метод addSubTask(SubTask subtask) определяет валидацию и обновляет subtask в хранилище приоритетных задач;
17.	Метод addTask(Task task) определяет валидацию и добавляет mainTask в хранилище приоритетных задач;
18.	Добавлен метод getPrioritizedTasks(), возвращающий список приоритетных задач в качестве List<Task>;

Task (src\main\java\model\)
1. 	Добавлен метод getEndTime() для вычисления времени окончания задачи;
2.	Добавлен и переопределен метод клонирования Object.clone() для объекта типа Task;
3.	Метод toString() дополнен новыми полями объекта - startTime, duration, endTime;
4.	Добавлен новый метод getEndTime() для расчета времени окончания задачи;

MainTask (src\main\java\model\)
1. 	Переопределен метод getEndTime() класса Tak для вычисления времени окончания главной задачи;
2.	Добавлен и переопределен метод клонирования Object.clone() для объекта типа MainTask;
3.	Переопределен новый метод getEndTime() класса Task для расчета времени окончания задачи;

SubTask (src\main\java\model\)
1.	Добавлен и переопределен метод клонирования Object.clone() для объекта типа SubTask;
2.	Метод toString() дополнен новыми полями объекта - startTime, duration, endTime;
3.	Добавлен метод вычисления 

FileBackedTaskManager (src\main\java\service\)
1.	В строку хэдера чтения и записи в файл добавлены новые поля задачи startTime, duration и endTime;

FileBackedTaskManagerTEST (test\)
1. Добавлена проверка корректности присваивания главной задаче полей startTime, duration и getTtime;

*****************************************************************************************************************************************

Изменения 04.03.2025
FileBackedTaskManager (src\main\java\service\)
1.	Изменен принцип присвоения внешенего фала к файлу по умолчанию. Теперь присвоение производится не только по имени, а самого файла.
2.	При записи данных в файл добавляется хидер ("id,type,name,status,description,epic");
3.	Переопределены методы родительского класса (deleteTaskById(), deleteMainTaskById(), deleteSubTaskById(), clearAllDepos());

InMemoryTaskManager (src\main\java\service\)
1.	Модификатор доступа полей (id, taskMap, mainTaskMap) изменены на protected;
2.	Изменен индекс возращаемого значения Integer deleteMainTaskById(int id). Теперь, в случае успешного выполнения метода, возвращается
	id удаленной главной задачи;

FileBackedTaskManagerTEST (test\)
1.	Добавлены проверки новых переодпределенных методов (deleteTaskById(), deleteMainTaskById(), deleteSubTaskById(), clearAllDepos());

*****************************************************************************************************************************************

Описание функционала:
-	FileBackedTaskManager по умолчанию хранит свой рабочий файл, с которым можно работать "из коробки".
-	Рабочая папка для работы имеет адрес src\sources\data
-	В случае отсутствия дефолтного файла, FileBackedTaskManager автоматически его создает, о чем выводит сообщение в консоль.
-	После считывания файла обновляется счетчик задач, который позволяет без ошибок дописывать данные в файл.

Изменения 01.03.2025

InMemoryTaskManager (src\main\java\service\)
1. 	Изменены принципы добавления всех типов задач (методы addTask(Task t), addMainTask(MainTask t) и addSubTask(SubTask t)).
	Теперь для избежания измения полей задачи добавленной при их изменении в исходнике, в хранилище добавляется snapshot входящей задачи;
2.	Изменен модификатор доступа поля private int id. Для обеспечения доступа к данному полю из класса FileBackedTaskManager, для  
	корректной работы класса после считывания данных из файла, поле приобрело статус protected static id;
3.	Исправлена ошибка, возникающая при записи главной задачи в хранилище после чтения данных из файла классом FileBackedTaskManager.
	Теперь, чтобы присвоить главной задаче статус IN_PROGRESS или DONE, в методе checkTaskProgress(Maintask t) значение счетчика статуса
	подзадач должно быть строго больше нуля. 

Task (src\main\java\model\)
1.	Для создания новых задач добавлен конструктор public Task(String name, String description).
2.	Конструктор public Task(String name, String description, TaskProgress taskProgres) теперь работает только на прием полей дочерних
	классов. В связи с этим получил модификатор доступа protected;
3.	Для корректного добавления новых задач FileBackedTaskManager после считывания данных из файла, изменена логика присваиваня id любого
	типа задачи (Task t, MainTask t, SubTask t);
	
InMemoryTaskManagerTEST (test\)
1.	Обновлены методы тестирования в соответствии с новым принципом добавления всех типов задач через snapshot;

InMemoryHistoryManagerTEST (test\)
1.	Обновлены методы тестирования в соответствии с новым принципом добавления всех типов задач через snapshot;
	
FileBackedTaskManager (src\main\java\service\)
1.	Добавлен класс FileBackedTaskManager, обеспечивающий запись и чтение состояния класса InMemoryTaskManager из ПЗУ посредством файла
	формата ".csv";
	
FileBackedTaskManagerTEST (test\)
1.	Добавлен тестовый класс FileBackedTaskManagerTEST. В процессе тестирования создается тестовый файл, в котором произведено тестирование
	всего функционала класса  FileBackedTaskManager. После окончания тестов, файл удаляется.

Managers (src\main\java\interfaces\)
1.	Добавлен метод вызова менеджера задач getDefaultFileBackedTaskManager() для работы с дефолтным файлом DataFBTM.csv;
2.	Добавлен метод вызова менеджера задач getDefaultFileBackedTaskManager(File file) для работы со сторонними файлом;

*****************************************************************************************************************************************

Изменения 17.02.2025

InMemoryHistoryManager (src\main\java\service\)
1.	Добавлен метод remove(), который удаляет задачу из таблицы истории задач и обновляет связи двусвязного списка.
2.	Добавлен метод removeAll, который удаляет задачи из истории и двусвязного списка по входящему параметру - таблице;
3.	Убран метод getTask(). Теперь функцию по получению листа истории задач выполняет метод getHistory();

InMemoryTaskManager (src\main\java\service\)
1.	Обновлены методы удаления задач, главных задач и подзадач типа delete(className)ById.
	Удаление производится с учетом новой реализации хранилища истории задач.
	В телах методов добавлены методы класса InMemoryHistoryManager - remove() и removeAll();
2. 	Убрано выбрасывание исключений в метододах типа get(className). Теперь метод в случае неудачи возвращает шаблон
	объекта с id=-1 и полями, указывающими на данные методы;
3. 	Исправлена ошибка в логике метода removeAllSubTasks();
4. 	Обновлена логика метода removeAllMainTasks(). В тело метода добавлен метод removeAllSubTasks();

Managers (src\main\java\interfaces\)
1.	Добавлен дефолтный метод getDefaultHistoryReverse() для получения истории задач в обратном порядке;
2.	Обновлен метод remove(). Теперь метод удаляет задачу не только из коллекции, но и из двусвязного списка;

InMemoryHistoryManagerTEST (test\)
1. 	Добавлен тест проверки метода remove(), удаляющий задачу из истории задач и двусвязного списка;
2.	Добавлен тест проверки метода removeAll(), удаляющий список задач из истории и двусвязного списка;
3.	Исправлены тесты методов get(className) в соответствии с изменениями возвращаемого параметра в случае неудачи;

InMemoryHistoryManagerTEST (test\)
1. Добавлен  метод deleteTaskById_FromHistory_succes(), тестирующий методы deleteTaskById(), deleteMainTaskById(),deleteSubTaskById();
2. Добавлен метод, тестирующий удаление всех задач, главных задач и подзадач; в частности - удаление подзадач при удалении главной задач;

*****************************************************************************************************************************************