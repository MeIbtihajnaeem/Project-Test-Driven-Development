package com.mycompany.orderAssignmentSystem.repository.postgres;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;

public class WorkerDatabaseRepository implements WorkerRepository {
	private EntityManager entityManager;

	public WorkerDatabaseRepository(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public synchronized List<Worker> findAll() {

		return entityManager.createQuery("SELECT w FROM Worker w", Worker.class).getResultList();
	}

	@Override
	public synchronized Worker findById(Long workerId) {
		try {

			Worker worker = entityManager.find(Worker.class, workerId);
			entityManager.clear();

			if (worker != null) {
				TypedQuery<CustomerOrder> query = entityManager.createQuery(
						"SELECT o FROM CustomerOrder o where o.worker.workerId=:worker_id", CustomerOrder.class);
				query.setParameter("worker_id", workerId);
				worker.setOrders(query.getResultList());
				System.out.println(worker.getOrders());
			}

			return worker;
		} catch (NoResultException e) {
			return null;
		}

	}

	@Override
	public synchronized List<Worker> findByName(String workerName) {
		TypedQuery<Worker> query = entityManager.createQuery("SELECT w FROM Worker w where w.workerName=:name",
				Worker.class);
		query.setParameter("name", workerName);
		return query.getResultList();
	}

	@Override
	public synchronized List<Worker> findByOrderCategory(OrderCategory category) {
		TypedQuery<Worker> query = entityManager.createQuery("SELECT w FROM Worker w where w.workerCategory=:category",
				Worker.class);
		query.setParameter("category", category.toString());
		return query.getResultList();
	}

	@Override
	public synchronized Worker findByPhoneNumber(String phoneNumber) {
		try {
			TypedQuery<Worker> query = entityManager
					.createQuery("SELECT w FROM Worker w where w.workerPhoneNumber=:phone", Worker.class);
			query.setParameter("phone", phoneNumber);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	@Override
	public synchronized Worker save(Worker worker) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Worker newWorker = entityManager.merge(worker);
			transaction.commit();

			return newWorker;

		} catch (Exception e) {
			transaction.rollback();
			return null;
		}
	}

	@Override
	public synchronized void delete(Worker worker) {
		EntityTransaction transaction = entityManager.getTransaction();
		entityManager.clear();
		try {
			transaction.begin();
//			entityManager.clear();
//			entityManager.remove(worker);
			entityManager.remove(entityManager.contains(worker) ? worker : entityManager.merge(worker));

			transaction.commit();
		} catch (Exception e) {
			System.out.println(e.toString());
			transaction.rollback();
		}

	}

}
