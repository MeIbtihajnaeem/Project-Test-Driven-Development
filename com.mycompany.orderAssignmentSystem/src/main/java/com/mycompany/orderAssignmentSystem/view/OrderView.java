package com.mycompany.orderAssignmentSystem.view;

import java.util.List;

import com.mycompany.orderAssignmentSystem.model.CustomerOrder;

public interface OrderView {
	void showAllOrder(List<CustomerOrder> order);

	void orderAdded(CustomerOrder order);

	void orderModified(CustomerOrder order);

	void showFetchedOrder(CustomerOrder order);

	void showSearchResultForOrder(List<CustomerOrder> order);

	void orderRemoved(CustomerOrder order);

	void showError(String message, CustomerOrder order);

	void showErrorNotFound(String message, CustomerOrder order);

	void showSearchError(String message, String searchText);
}
