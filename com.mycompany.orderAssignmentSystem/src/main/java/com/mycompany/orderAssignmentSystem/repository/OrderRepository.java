package com.mycompany.orderAssignmentSystem.repository;

import java.time.LocalDateTime;
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

	public CustomerOrder findByCustomerName(String name);

	public CustomerOrder findByCustomerAddress(String address);

	public CustomerOrder findByCustomerPhoneNumber(String phoneNumber);

	public CustomerOrder findByDate(LocalDateTime date);

	public CustomerOrder findByDescription(String description);

	public CustomerOrder findByOrderCategory(OrderCategory category);

	public CustomerOrder findByOrderStatus(OrderStatus status);

}
