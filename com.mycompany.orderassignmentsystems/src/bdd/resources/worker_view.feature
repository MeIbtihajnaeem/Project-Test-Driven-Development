@WorkerView
Feature: Worker Application Frame Specification of the behavior of the Worker Application Frame
	@ShowWorkers
	Scenario: The initial state of the Worker view
	Given The database contains worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
		| 2 | Bob | 3401372679 | PLUMBER |
	When The Worker View is shown
	Then The worker view list contains an element with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
		| 2 | Bob | 3401372679 | PLUMBER |
	@AddNewWorker
	Scenario: Add a new Worker
	Given The database contains worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
	When The Worker View is shown
	Then The user enters the following values in the worker view
		| WorkerName | WorkerPhone | WorkerCategory |
		|Bob | 3401372679 | PLUMBER |
	When The user clicks the worker view "Add" button
	Then The worker view list contains an element with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
		| 2 | Bob | 3401372679 | PLUMBER |
	@AddNewWorkerForError
	Scenario: Add a new Worker with wrong phone number
	Given The database contains worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
	When The Worker View is shown
	Then The user enters the following values in the worker view
		| WorkerName | WorkerPhone | WorkerCategory |
		| Bob | 4401372678 | PLUMBER |
	When The user clicks the worker view "Add" button
	Then An error is shown in worker view containing the following values 
    | Bob | 4401372678 | PLUMBER |
	@UpdateWorker
	Scenario: Update an existing worker
	Given The database contains worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
	When The Worker View is shown	
	Then The user enters the following values in the worker view
		| WorkerId| WorkerName | WorkerPhone | WorkerCategory |
		| 1 | Bob | 3401372678 | PLUMBER |
	When The user clicks the worker view "Update" button
	Then The worker view list contains an element with the following values
		| 1 | Bob | 3401372678 | PLUMBER |
	@UpdateWorkerForError
	Scenario: Update an existing worker
	Given The database contains worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
	When The Worker View is shown	
	Then The user enters the following values in the worker view
		| WorkerId| WorkerName | WorkerPhone | WorkerCategory |
		| 1 | Leo | 4401372678 | PLUMBER |
	When The user clicks the worker view "Update" button
	Then An error is shown in worker view containing the following values
		| 1 | Leo | 4401372678 | PLUMBER |
	@FetchWorker
	Scenario: Fetch an existing Worker with its id
	Given The database contains worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
	When The Worker View is shown
	Then The user enters the following values in the worker view
		|WorkerId|
		| 1 |
	When The user clicks the worker view "Fetch" button
	Then The worker view fields contains an element with the following values
		| WorkerId| WorkerName | WorkerPhone | WorkerCategory |
		| 1 | Leo | 3401372678 | PLUMBER |
	@FetchWorkerForError
	Scenario: Fetch an existing Worker with its id
	Given The database contains worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
	When The Worker View is shown
	Then The user enters the following values in the worker view
		|WorkerId|
		| 2 |
	When The user clicks the worker view "Fetch" button
	Then An no entry found error is shown in worker view containing the following values
		| 2 |
	@DeleteWorker
	Scenario: Delete an existing worker
	Given The database contains worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
		| 2 | Bob | 3401372672 | PLUMBER |
	When The Worker View is shown
	Then The user select worker from the list
		| 3401372678 |
	When The user clicks the worker view "Delete" button
	Then The worker view list contains an element with the following values
		| 2 | Bob | 3401372672 | PLUMBER |
	@DeleteError
	Scenario: Delete an existing worker with error
	Given The database contains worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
		| 2 | Bob | 3401372672 | PLUMBER |
	When The Worker View is shown
	Then The user select worker from the list
		| Leo |
	And The database deletes the worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
	When The user clicks the worker view "Delete" button
	Then An no entry found error is shown in worker view containing the following values
		|	Leo |
	@SearchWorker
	Scenario: Search order by worker name
	Given The database contains worker with the following values
		|	1 | Jhon | 3401372678 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER |
	When The Worker View is shown
	Then The user enters the following values in the worker view
		| SearchWorker | SearchByOptions | 
		| Alic | WORKER_NAME |
	When The user clicks the worker view "Search Worker" button
	Then The worker view list contains an element with the following values
		| 2 | Alic | 3401372679 | PLUMBER |
	@SearchWorkerError
	Scenario: Search worker by worker name when worker does not exists
	Given The database contains worker with the following values
		|	1 | Jhon | 3401372678 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER |
	When The Worker View is shown
	Then The user enters the following values in the worker view
		| SearchWorker | SearchByOptions | 
		| Leo | WORKER_NAME |
	When The user clicks the worker view "Search Worker" button
	Then An search error is shown in worker view containing the following values
		| Leo |
	@ClearSearch
	Scenario: Search worker then verify search fields are clear
	Given The database contains worker with the following values
		|	1 | Jhon | 3401372678 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER |
	When The Worker View is shown
	Then The user enters the following values in the worker view
		| SearchWorker | SearchByOptions | 
		| Alic | WORKER_NAME |
	When The user clicks the worker view "Search Worker" button
	Then The worker view list contains an element with the following values
		| 2 | Alic | 3401372679 | PLUMBER |
	When The user clicks the worker view "Clear" button
	Then The worker view list contains an element with the following values
		|	1 | Jhon | 3401372678 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER |
	And The worker view search filter are reset
	
	
	
	
	
	