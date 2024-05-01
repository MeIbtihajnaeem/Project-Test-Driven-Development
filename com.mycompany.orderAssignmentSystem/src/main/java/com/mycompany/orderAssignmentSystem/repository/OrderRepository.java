package com.mycompany.orderAssignmentSystem.repository;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;

public interface OrderRepository {
	public List<CustomerOrder> findAll();

	public CustomerOrder save(CustomerOrder order);

	public CustomerOrder modify(CustomerOrder order);

	public void delete(CustomerOrder order);

	public CustomerOrder findById(long orderId);

	public List<CustomerOrder> findByCustomerName(String name);

	public List<CustomerOrder> findByCustomerPhoneNumber(String phoneNumber);

	public List<CustomerOrder> findByDate(LocalDate date);

	public List<CustomerOrder> findByOrderCategory(OrderCategory category);

	public List<CustomerOrder> findByOrderStatus(OrderStatus status);

}
