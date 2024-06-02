package com.mycompany.orderAssignmentSystem.repository.postgres;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.repository.OrderRepository;

public class OrderDatabaseRepository implements OrderRepository {
	private EntityManager entityManager;

	public OrderDatabaseRepository(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public List<CustomerOrder> findAll() {
		return entityManager.createQuery("SELECT o FROM CustomerOrder o", CustomerOrder.class).getResultList();
	}

	@Override
	@Transactional
	public CustomerOrder save(CustomerOrder order) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		CustomerOrder savedOrder = entityManager.merge(order);
		transaction.commit();
		return savedOrder;
	}

	@Override
	@Transactional
	public void delete(CustomerOrder order) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.remove(order);
		transaction.commit();
	}

	@Override
	public CustomerOrder findById(Long orderId) {
		return entityManager.find(CustomerOrder.class, orderId);
	}

	@Override
	public List<CustomerOrder> findByCustomerName(String name) {
		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.customer_name=:name", CustomerOrder.class);
		query.setParameter("name", name);
		return query.getResultList();
	}

	@Override
	public List<CustomerOrder> findByCustomerPhoneNumber(String phoneNumber) {
		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.customer_phone=:phone", CustomerOrder.class);
		query.setParameter("phone", phoneNumber);
		return query.getResultList();
	}

	@Override
	public List<CustomerOrder> findByDate(String date) {
		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.appointment_date=:date", CustomerOrder.class);
		query.setParameter("date", date);
		return query.getResultList();
	}

	@Override
	public List<CustomerOrder> findByOrderCategory(OrderCategory category) {
		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.order_category=:category", CustomerOrder.class);
		query.setParameter("category", category.toString());
		return query.getResultList();
	}

	@Override
	public List<CustomerOrder> findByOrderStatus(OrderStatus status) {
		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.order_status=:status", CustomerOrder.class);
		query.setParameter("status", status.toString());
		return query.getResultList();
	}

}
