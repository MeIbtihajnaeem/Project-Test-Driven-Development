package com.mycompany.orderAssignmentSystem.repository.postgres;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;

public class WorkerDatabaseRepository implements WorkerRepository {
	private EntityManager entityManager;

	public WorkerDatabaseRepository(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public List<Worker> findAll() {

		return entityManager.createQuery("SELECT w FROM Worker w", Worker.class).getResultList();
	}

	@Override
	@Transactional
	public Worker findById(Long workerId) {
//		EntityTransaction transaction = entityManager.getTransaction();
//		transaction.begin();
//		

		Worker worker = entityManager.find(Worker.class, workerId);
//		if (worker != null) {
//			TypedQuery<CustomerOrder> query = entityManager
//					.createQuery("SELECT o FROM CustomerOrder o where o.work_id=:worker_id", CustomerOrder.class);
//			query.setParameter("worker_id", workerId);
//			worker.setOrders(query.getResultList());
//			System.out.println(worker.getOrders());
//		}
//		transaction.commit();

		return worker;
//		return entityManager.createQuery(
//				"SELECT w FROM Worker w, CustomerOrder o where o.worker_id=w.workerId AND w.workerId:=" + workerId,
//				Worker.class).getSingleResult();

	}

	@Override
	@Transactional
	public List<Worker> findByName(String workerName) {
		TypedQuery<Worker> query = entityManager.createQuery("SELECT w FROM Worker w where w.workerName=:name",
				Worker.class);
		query.setParameter("name", workerName);
		return query.getResultList();
	}

	@Override
	@Transactional
	public List<Worker> findByOrderCategory(OrderCategory category) {
		TypedQuery<Worker> query = entityManager.createQuery("SELECT w FROM Worker w where w.workerCategory=:category",
				Worker.class);
		query.setParameter("category", category.toString());
		return query.getResultList();
	}

	@Override
	@Transactional
	public Worker findByPhoneNumber(String phoneNumber) {
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
	@Transactional
	public Worker save(Worker worker) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Worker newWorker = entityManager.merge(worker);
		transaction.commit();
		return newWorker;
	}

	@Override
	@Transactional
	public void delete(Worker worker) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.remove(worker);
		transaction.commit();

	}

}
