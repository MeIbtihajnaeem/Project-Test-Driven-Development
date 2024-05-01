package com.mycompany.orderAssignmentSystem.repository;

import java.util.List;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.model.Worker;

public interface WorkerRepository {
	public List<Worker> findAll();

	public Worker findById(long workerId);

	public List<Worker> findByName(String workerName);

	public List<Worker> findByOrderCategory(OrderCategory category);

	public Worker findByPhoneNumber(String phoneNumber);

	public Worker save(Worker worker);

	public Worker modify(Worker worker);

	public void delete(Worker worker);

//	public List<Worker> searchByField(String searchText, WorkerSearchOption searchOption);

}
