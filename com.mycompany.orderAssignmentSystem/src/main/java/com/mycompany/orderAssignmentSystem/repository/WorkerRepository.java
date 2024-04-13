package com.mycompany.orderAssignmentSystem.repository;

import java.util.List;

import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;

public interface WorkerRepository {
	public List<Worker> findAll();

	public Worker findById(long workerId);

	public Worker save(Worker worker);

	public Worker modify(Worker worker);

	public Worker delete(long workerId);

	public List<Worker> searchByField(String searchText, String searchType);

	public List<CustomerOrder> findAllOrdersByWorkerId(long workerId);

}
