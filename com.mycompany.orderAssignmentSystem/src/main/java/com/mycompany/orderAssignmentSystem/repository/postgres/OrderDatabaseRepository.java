package com.mycompany.orderassignmentsystem.repository.postgres;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.repository.OrderRepository;

public class OrderDatabaseRepository implements OrderRepository {
	private EntityManagerFactory entityManagerFactory;

	public OrderDatabaseRepository(EntityManagerFactory entityManagerFactory) {
		super();
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public synchronized List<CustomerOrder> findAll() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<CustomerOrder> resultList = entityManager.createQuery("SELECT o FROM CustomerOrder o", CustomerOrder.class)
				.getResultList();
		entityManager.close();
		return resultList;

	}

	@Override
	public synchronized CustomerOrder save(CustomerOrder order) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.clear();
		EntityTransaction transaction = entityManager.getTransaction();
		try {

			transaction.begin();
			order = entityManager.merge(order);
			transaction.commit();
			entityManager.close();
			return order;
		} catch (Exception e) {
			transaction.rollback();
			entityManager.close();
			throw new IllegalArgumentException("failed to create order.");
		}
	}

	@Override
	public synchronized void delete(CustomerOrder order) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();

		try {
			transaction.begin();
			entityManager.remove(entityManager.getReference(CustomerOrder.class, order.getOrderId()));
			entityManager.flush();
			transaction.commit();

			entityManager.close();
		} catch (Exception e) {
			transaction.rollback();
			entityManager.close();
			throw new IllegalArgumentException("failed to delete order.");
		}
	}

	@Override

	public synchronized CustomerOrder findById(Long orderId) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CustomerOrder customerOrder = entityManager.find(CustomerOrder.class, orderId);
		entityManager.close();
		return customerOrder;

	}

	@Override
	public synchronized List<CustomerOrder> findByCustomerName(String name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.customerName=:name", CustomerOrder.class);
		query.setParameter("name", name);
		List<CustomerOrder> resultList = query.getResultList();
		entityManager.close();
		return resultList;
	}

	@Override
	public synchronized List<CustomerOrder> findByCustomerPhoneNumber(String phoneNumber) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.customerPhoneNumber=:phone", CustomerOrder.class);
		query.setParameter("phone", phoneNumber);
		List<CustomerOrder> resultList = query.getResultList();
		entityManager.close();
		return resultList;
	}

	@Override
	public synchronized List<CustomerOrder> findByDate(String date) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.appointmentDate=:date", CustomerOrder.class);
		query.setParameter("date", date);
		List<CustomerOrder> resultList = query.getResultList();
		entityManager.close();
		return resultList;
	}

	@Override
	public synchronized List<CustomerOrder> findByOrderCategory(OrderCategory category) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.orderCategory=:category", CustomerOrder.class);
		query.setParameter("category", category);
		List<CustomerOrder> resultList = query.getResultList();
		entityManager.close();
		return resultList;
	}

	@Override
	public synchronized List<CustomerOrder> findByOrderStatus(OrderStatus status) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		TypedQuery<CustomerOrder> query = entityManager
				.createQuery("SELECT o FROM CustomerOrder o where o.orderStatus=:status", CustomerOrder.class);
		query.setParameter("status", status);
		List<CustomerOrder> resultList = query.getResultList();
		entityManager.close();
		return resultList;
	}

}