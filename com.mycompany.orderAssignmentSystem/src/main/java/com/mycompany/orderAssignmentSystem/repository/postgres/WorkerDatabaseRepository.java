package com.mycompany.orderAssignmentSystem.repository.postgres;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;

public class WorkerDatabaseRepository implements WorkerRepository {
	private EntityManagerFactory entityManagerFactory;

	public WorkerDatabaseRepository(EntityManagerFactory entityManagerFactory) {
		super();
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public synchronized List<Worker> findAll() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		List<Worker> resultList = entityManager.createQuery("SELECT w FROM Worker w", Worker.class).getResultList();
		entityManager.close();
		return resultList;
	}

	@Override
	public synchronized Worker findById(Long workerId) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

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
			entityManager.close();
			return worker;
		} catch (Exception e) {
			entityManager.close();
			throw new NullPointerException("failed to get worker by id.");
		}

	}

	@Override
	public synchronized List<Worker> findByName(String workerName) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		TypedQuery<Worker> query = entityManager.createQuery("SELECT w FROM Worker w where w.workerName=:name",
				Worker.class);
		query.setParameter("name", workerName);
		List<Worker> resultList = query.getResultList();
		entityManager.close();
		return resultList;
	}

	@Override
	public synchronized List<Worker> findByOrderCategory(OrderCategory category) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		TypedQuery<Worker> query = entityManager.createQuery("SELECT w FROM Worker w where w.workerCategory=:category",
				Worker.class);
		query.setParameter("category", category);
		List<Worker> resultList = query.getResultList();
		entityManager.close();
		return resultList;
	}

	@Override
	public synchronized Worker findByPhoneNumber(String phoneNumber) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			TypedQuery<Worker> query = entityManager
					.createQuery("SELECT w FROM Worker w where w.workerPhoneNumber=:phone", Worker.class);
			query.setParameter("phone", phoneNumber);
			Worker singleResult = query.getSingleResult();
			entityManager.close();
			return singleResult;
		} catch (NoResultException e) {
			entityManager.close();
			return null;
		}

	}

	@Override
	public synchronized Worker save(Worker worker) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			worker = entityManager.merge(worker);
			transaction.commit();
			entityManager.close();
			return worker;

		} catch (Exception e) {
			transaction.rollback();
			entityManager.close();
			throw new IllegalArgumentException("failed to create worker.");
		}
	}

	@Override
	public synchronized void delete(Worker worker) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
//		entityManager.flush();
//		entityManager.clear();

		try {
			transaction.begin();
//			Worker entityToRemove = entityManager.getReference(Worker.class, worker.getWorkerId());

			entityManager.remove(entityManager.getReference(Worker.class, worker.getWorkerId()));
			entityManager.flush();

			transaction.commit();
			entityManager.close();
		} catch (Exception e) {
			transaction.rollback();
			entityManager.close();
			throw new IllegalArgumentException("failed to delete worker.");
		}

	}

}
