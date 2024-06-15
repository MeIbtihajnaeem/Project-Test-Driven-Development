@OrderView
Feature: Order Application Frame Specification of the behavior of the Order Application Frame
	@ShowOrders
	Scenario: The initial state of the Order view
	Given The database contains the order with the following values
		| 1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape | Leo | 3401372671 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape | Bob | 3401372672 | PLUMBER |
	When The Order View is shown
	Then The order view list contains an element with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape | Leo | 3401372671 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape | Bob | 3401372672 | PLUMBER |
	@AddNewOrder
	Scenario: Add a new Order
	Given The database contains worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
		| 2 | Bob | 3401372679 | PLUMBER |
	When The Order View is shown
	Then The user enters the following values in the order view
		| CustomerName | CustomerPhone | OrderCategory | OrderStatus |  CustomerAddress | SelectedDate	| OrderDescription| worker_name | worker_phone | worker_category |
		| Jhon 				 | 3401372678 	 | PLUMBER 			 | PENDING 		 | Piazza Luigi 		| 12-12-2024 		| Bring Tape Please 			| Leo 				 | 3401372678 	| PLUMBER |
	When The user clicks the order view "Add" button
	Then The order view list contains an element with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
	@AddNewOrderForError
	Scenario: Add a new Order without worker
	Given The database contains worker with the following values
		| 1 | Leo | 3401372678 | PLUMBER |
		| 2 | Bob | 3401372679 | PLUMBER |
	When The Order View is shown
	Then The user enters the following values in the order view
		| CustomerName | CustomerPhone | OrderCategory | OrderStatus |  CustomerAddress | SelectedDate	| OrderDescription |  worker_name | worker_phone | worker_category |
		| Jhon 				 | 4401372678 	 | PLUMBER 			 | PENDING 		 | Piazza Luigi 		| 12-12-2024 		| Bring Tape Please|  Leo 				 | 3401372678 	| PLUMBER |
	When The user clicks the order view "Add" button
	Then An error is shown in order view containing the following values 
    | Jhon | 4401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please |  Leo 				 | 3401372678 	| PLUMBER |
	@UpdateOrder
	Scenario: Update an existing Order
	Given The database contains the order with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
	When The Order View is shown	
	Then The user enters the following values in the order view
		|OrderId| CustomerName | CustomerPhone | OrderCategory | OrderStatus |  CustomerAddress | SelectedDate	| OrderDescription| worker_name | worker_phone | worker_category |
		| 1 | Jhon | 3401372678 | PLUMBER | COMPLETED | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
	When The user clicks the order view "Update" button
	Then The order view list contains an element with the following values
		| 1 | Jhon | 3401372678 | PLUMBER | COMPLETED | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
	@UpdateOrderForError
	Scenario: Update an order with invalid phone number
	Given The database contains the order with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
	When The Order View is shown	
	Then The user enters the following values in the order view
		|OrderId| CustomerName | CustomerPhone | OrderCategory | OrderStatus |  CustomerAddress | SelectedDate	| OrderDescription| worker_name | worker_phone | worker_category |
		| 1 | Jhon | 4401372678 | PLUMBER | COMPLETED | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
	When The user clicks the order view "Update" button
	Then An error is shown in order view containing the following values
		| 1 | Jhon | 4401372678 | PLUMBER | COMPLETED | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
	@FetchOrder
	Scenario: Fetch an existing Order with its id
	Given The database contains the order with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
	When The Order View is shown
	Then The user enters the following values in the order view
		|OrderId|
		| 1 |
	When The user clicks the order view "Fetch" button
	Then The order view fields contains an element with the following values
		|OrderId| CustomerName | CustomerPhone | OrderCategory | OrderStatus |  CustomerAddress | SelectedDate	| OrderDescription| worker_name | worker_phone | worker_category |
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
	@FetchOrderForError
	Scenario: Fetch an existing Order with its id
	Given The database contains the order with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
	When The Order View is shown
	Then The user enters the following values in the order view
		|OrderId|
		| 2 |
	When The user clicks the order view "Fetch" button
	Then An no entry found error is shown in order view containing the following values
		| 2 |
	@DeleteOrder
	Scenario: Delete an existing order
	Given The database contains the order with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372671 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Bob | 3401372672 | PLUMBER |
	When The Order View is shown
	Then The user select order from the list
		| 3401372679 |
	When The user clicks the order view "Delete" button
	Then The order view list contains an element with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372671 | PLUMBER |
	@DeleteError
	Scenario: Delete an existing order with error
	Given The database contains the order with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372671 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Bob | 3401372672 | PLUMBER |
	When The Order View is shown
	Then The user select order from the list
		| 3401372679 |
	And The database deletes the order with the following values
		| 2 | Alic | 3401372679 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Bob | 3401372672 | PLUMBER |
	When The user clicks the order view "Delete" button
	Then An error is shown in order view containing the following values
		|	3401372679 |
	@SearchOrder
	Scenario: Search order by customer name
	Given The database contains the order with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Bob | 3401372679 | PLUMBER |
	When The Order View is shown
	Then The user enters the following values in the order view
		| SearchOrder | SearchBy | 
		| Alic | CUSTOMER_NAME |
	When The user clicks the order view "Search" button
	Then The order view list contains an element with the following values
		| 2 | Alic | 3401372679 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Bob | 3401372679 | PLUMBER |
	@SearchOrderError
	Scenario: Search order by customer name when order does not exists
	Given The database contains the order with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Bob | 3401372679 | PLUMBER |
	When The Order View is shown
	Then The user enters the following values in the order view
		| SearchOrder | SearchBy | 
		| Leo | CUSTOMER_NAME |
	When The user clicks the order view "Search" button
	Then An seach error is shown in order view containing the following values
		| Leo |
	@ClearSearch
	Scenario: Search order then fetch order and verify every field is clear
	Given The database contains the order with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Bob | 3401372679 | PLUMBER |
	When The Order View is shown
	Then The user enters the following values in the order view
		| SearchOrder | SearchBy | 
		| Alic | CUSTOMER_NAME |
	When The user clicks the order view "Search" button
	Then The order view list contains an element with the following values
		| 2 | Alic | 3401372679 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Bob | 3401372679 | PLUMBER |
	And The user enters the following values in the order view
		|OrderId|
		| 1 |
	When The user clicks the order view "Fetch" button
	Then The order view fields contains an element with the following values
		|OrderId| CustomerName | CustomerPhone | OrderCategory | OrderStatus |  CustomerAddress | SelectedDate	| OrderDescription| worker_name | worker_phone | worker_category |
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
	When The user clicks the order view "Clear" button
	Then The order view list contains an element with the following values
		|	1 | Jhon | 3401372678 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Leo | 3401372678 | PLUMBER |
		| 2 | Alic | 3401372679 | PLUMBER | PENDING | Piazza Luigi | 12-12-2024 | Bring Tape Please | Bob | 3401372679 | PLUMBER |
	And The order view fields are reset		
		
