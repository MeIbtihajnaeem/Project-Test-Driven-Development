package com.mycompany.orderAssignmentSystem.controller.utils;

import javax.persistence.EntityManager;

public interface DatabaseConfigurations {

	public EntityManager getConnection();
}
