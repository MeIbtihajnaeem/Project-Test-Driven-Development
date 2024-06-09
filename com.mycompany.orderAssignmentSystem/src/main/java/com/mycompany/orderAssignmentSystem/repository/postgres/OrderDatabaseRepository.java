package com.mycompany.orderAssignmentSystem.repository.postgres;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

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
	public synchronized List<CustomerOrder> findAll() {
		return entityManager.createQuery("SELECT o FROM CustomerOrder o", CustomerOrder.class).getResultList();

	}

	@Override
	public synchronized CustomerOrder save(CustomerOrder order) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
//	        Worker managedWorker = entityManager.contains(worker) ? worker : entityManager.merge(worker);

			order = entityManager.contains(order) ? order : entityManager.merge(order);
			transaction.commit();
			return order;
		} catch (Exception e) {
			transaction.rollback();
			throw new IllegalArgumentException("failed to create order.");
		}
	}

	@Override
	public synchronized void delete(CustomerOrder order) {
		EntityTransaction transaction = entityManager.getTransaction();

		try {
			transaction.begin();
			entityManager.remove(entityManager.contains(order) ? order : entityManager.merge(order));
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw new IllegalArgumentException("failed to delete order.");
		}
	}

	@Override

	public synchronized CustomerOrder findById(Long orderId) {
//		try {
		return entityManager.find(CustomerOrder.class, orderId);
//		} catch (NoResultException e) {
//			return null;
//		}

	}

	@Override
	public synchronized List<CustomerOrder> findByCustomerName(String name) {
		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.customerName=:name", CustomerOrder.class);
		query.setParameter("name", name);
		return query.getResultList();
	}

	@Override
	public synchronized List<CustomerOrder> findByCustomerPhoneNumber(String phoneNumber) {
		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.customerPhoneNumber=:phone", CustomerOrder.class);
		query.setParameter("phone", phoneNumber);
		return query.getResultList();
	}

	@Override
	public synchronized List<CustomerOrder> findByDate(String date) {
		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.appointmentDate=:date", CustomerOrder.class);
		query.setParameter("date", date);
		return query.getResultList();
	}

	@Override
	public synchronized List<CustomerOrder> findByOrderCategory(OrderCategory category) {
		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.orderCategory=:category", CustomerOrder.class);
		query.setParameter("category", category);
		return query.getResultList();
	}

	@Override
	public synchronized List<CustomerOrder> findByOrderStatus(OrderStatus status) {
		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.orderStatus=:status", CustomerOrder.class);
		query.setParameter("status", status);
		return query.getResultList();
	}

}
