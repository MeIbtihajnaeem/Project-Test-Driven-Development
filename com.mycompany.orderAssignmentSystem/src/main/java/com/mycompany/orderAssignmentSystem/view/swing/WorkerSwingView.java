package com.mycompany.orderAssignmentSystem.view.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionListener;

import com.mycompany.orderAssignmentSystem.controller.WorkerController;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.WorkerSearchOption;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.view.WorkerView;

public class WorkerSwingView extends JFrame implements WorkerView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private JPanel contentPane;
	private JTextField txtWorkerId;
	private JTextField txtWorkerName;
	private JTextField txtWorkerPhone;
	private JTextField txtSearchWorker;
	private JTextField txtOrdersByWorkerId;
	private WorkerController workerController;
	private JComboBox<OrderCategory> cmbWorkerCategory;
	private JButton btnAdd;
	private JButton btnUpdate;
	private JButton btnFetch;
	private JButton btnDelete;
	private JComboBox<WorkerSearchOption> cmbSearchByOptions;
	private JButton btnSearchWorker;
	private DefaultListModel<Worker> workerListModel;
	private DefaultListModel<CustomerOrder> orderListModel;
	private JList<Worker> listWorkers;
	private JList<CustomerOrder> listOrders;
	private JButton btnClearSearchWorker;
	private JButton btnSearchOrder;
	private JLabel showErrorLbl;
	private JLabel showErrorLblSearchWorker;
	private JLabel showErrorLblSearchOrder;
	private JLabel showErrorNotFoundLbl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					WorkerSwingView frame = new WorkerSwingView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	DefaultListModel<Worker> getWorkerListModel() {
		return workerListModel;
	}

	DefaultListModel<CustomerOrder> getOrderListModel() {
		return orderListModel;
	}

	public JComboBox<OrderCategory> getCmbWorkerCategory() {
		return cmbWorkerCategory;
	}

	/**
	 * Create the frame.
	 */
	public WorkerSwingView() {
		initializeAndPopulateComboBoxData();
		initializeUIComponents();

		KeyListener changeWorkerValueListener = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getSource() == txtSearchWorker) {
					handleSearchWorkerAndClearButtonStates();

				} else if (e.getSource() == txtOrdersByWorkerId) {
					handleSearchOrderByWorkerIdButtonStates();
				} else {
					handleButtonAndComboBoxStates();
				}
			}

		};

		ActionListener changeWorkerCategoryListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == cmbSearchByOptions) {
					handleSearchWorkerAndClearButtonStates();

				} else {
					handleButtonAndComboBoxStates();
				}
			}
		};

		ListSelectionListener listSelectionListener = e -> {
			btnDelete.setEnabled(listWorkers.getSelectedIndex() != -1);
		};

		ActionListener crudActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Worker worker = new Worker();
				if (e.getSource() == btnAdd) {
					worker.setWorkerName(txtWorkerName.getText());
					worker.setWorkerPhoneNumber(txtWorkerPhone.getText());
					worker.setWorkerCategory((OrderCategory) cmbWorkerCategory.getSelectedItem());
					workerController.createNewWorker(worker);
				} else if (e.getSource() == btnUpdate) {
					worker.setWorkerId(Long.parseLong(txtWorkerId.getText()));
					worker.setWorkerName(txtWorkerName.getText());
					worker.setWorkerPhoneNumber(txtWorkerPhone.getText());
					worker.setWorkerCategory((OrderCategory) cmbWorkerCategory.getSelectedItem());
					workerController.updateWorker(worker);
				} else if (e.getSource() == btnFetch) {
					worker.setWorkerId(Long.parseLong(txtWorkerId.getText()));
					workerController.fetchWorkerById(worker);

				} else {
					worker.setWorkerId(Long.parseLong(txtOrdersByWorkerId.getText()));
					workerController.fetchOrdersByWorkerId(worker);
				}

			}
		};
		ActionListener filterActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnSearchWorker) {
					String searchText = txtSearchWorker.getText();
					WorkerSearchOption searchOption = (WorkerSearchOption) cmbSearchByOptions.getSelectedItem();
					workerController.searchWorker(searchText, searchOption);
				} else if (e.getSource() == btnClearSearchWorker) {
					workerController.getAllWorkers();
				} else if (e.getSource() == btnDelete) {
					workerController.deleteWorker(listWorkers.getSelectedValue());

				}
			}

		};

		listWorkers.addListSelectionListener(listSelectionListener);
		txtSearchWorker.addKeyListener(changeWorkerValueListener);
		txtWorkerId.addKeyListener(changeWorkerValueListener);
		txtWorkerName.addKeyListener(changeWorkerValueListener);
		txtWorkerPhone.addKeyListener(changeWorkerValueListener);
		txtOrdersByWorkerId.addKeyListener(changeWorkerValueListener);
		cmbWorkerCategory.addActionListener(changeWorkerCategoryListener);
		cmbSearchByOptions.addActionListener(changeWorkerCategoryListener);

		btnAdd.addActionListener(crudActionListener);
		btnUpdate.addActionListener(crudActionListener);
		btnFetch.addActionListener(crudActionListener);
		btnDelete.addActionListener(filterActionListener);
		btnSearchOrder.addActionListener(crudActionListener);
		btnSearchWorker.addActionListener(filterActionListener);
		btnClearSearchWorker.addActionListener(filterActionListener);

	}

	private void initializeAndPopulateComboBoxData() {
		cmbWorkerCategory = new JComboBox<>();
		for (OrderCategory category : OrderCategory.values()) {
			cmbWorkerCategory.addItem(category);
		}
		cmbWorkerCategory.setSelectedItem(null);

		cmbSearchByOptions = new JComboBox<WorkerSearchOption>();
		for (WorkerSearchOption workerSearchOption : WorkerSearchOption.values()) {
			cmbSearchByOptions.addItem(workerSearchOption);
		}
		cmbSearchByOptions.setSelectedItem(null);
	}

	public void setWorkerController(WorkerController workerController) {
		this.workerController = workerController;
	}

	private void handleButtonAndComboBoxStates() {
		boolean isWorkerIdEmpty = txtWorkerId.getText().trim().isEmpty();
		boolean isWorkerNameEmpty = txtWorkerName.getText().trim().isEmpty();
		boolean isWorkerPhoneNumberEmpty = txtWorkerPhone.getText().trim().isEmpty();
		boolean isWorkerCategoryEmpty = cmbWorkerCategory.getSelectedItem() == null;
		btnAdd.setEnabled(isWorkerIdEmpty && !isWorkerNameEmpty && !isWorkerPhoneNumberEmpty && !isWorkerCategoryEmpty);
		btnUpdate.setEnabled(
				!isWorkerIdEmpty && !isWorkerNameEmpty && !isWorkerPhoneNumberEmpty && !isWorkerCategoryEmpty);
		btnFetch.setEnabled(!isWorkerIdEmpty && isWorkerNameEmpty && isWorkerPhoneNumberEmpty);
		if (btnFetch.isEnabled()) {
			cmbWorkerCategory.setSelectedItem(null);
		}
	}

	private void handleSearchWorkerAndClearButtonStates() {
		boolean isSearchWorkerTextEmpty = txtSearchWorker.getText().trim().isEmpty();
		boolean isSearchOptionEmpty = cmbSearchByOptions.getSelectedItem() == null;
		btnSearchWorker.setEnabled(!isSearchWorkerTextEmpty && !isSearchOptionEmpty);
		btnClearSearchWorker.setEnabled(!isSearchWorkerTextEmpty && !isSearchOptionEmpty);
	}

	private void handleSearchOrderByWorkerIdButtonStates() {
		boolean isOrdersByWorkerId = txtOrdersByWorkerId.getText().trim().isEmpty();
		btnSearchOrder.setEnabled(!isOrdersByWorkerId);
	}

	private void initializeUIComponents() {
		Font fontSizeTitle = new Font("Arial", Font.BOLD, 14);
		Font fontWithSize = new Font("Arial", Font.BOLD, 14);
		Font fontSizeBtn = new Font("Arial", Font.PLAIN, 14);

		ImageIcon icon = new ImageIcon(OrderSwingView.class.getResource("/images/check-circle.jpg"));
		Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Adjust the width and
		ImageIcon scaledIcon = new ImageIcon(scaledImage);

		setExtendedState(JFrame.MAXIMIZED_BOTH);

		setTitle("Worker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 650);
		contentPane = new JPanel();
		contentPane.setBackground(Color.white); // Set background colo
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBounds(100, 100, 850, 650);

		JPanel panelHeader = new JPanel();
		panelHeader.setLayout(null);
		panelHeader.setBackground(new Color(117, 169, 249));
		panelHeader.setBounds(0, 0, 834, 50);
		contentPane.add(panelHeader);

		JLabel lblWorkderForm = new JLabel("Worker Form");
		lblWorkderForm.setFont(fontSizeTitle);
		lblWorkderForm.setBounds(10, 11, 285, 30);
		panelHeader.add(lblWorkderForm);

		JButton lblManageOrder = new JButton("Manage Order");
		lblManageOrder.setName("lblManageOrder");
		lblManageOrder.setEnabled(true);
		lblManageOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblManageOrder.setOpaque(true);
		lblManageOrder.setHorizontalAlignment(SwingConstants.CENTER);
		lblManageOrder.setFont(new Font("Arial", Font.BOLD, 14));
		lblManageOrder.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		lblManageOrder.setBackground(new Color(250, 138, 132));
		lblManageOrder.setBounds(671, 10, 153, 30);
		panelHeader.add(lblManageOrder);

		JPanel panelContent = new JPanel();
		panelContent.setLayout(null);
		panelContent.setBackground(new Color(226, 238, 121));
		panelContent.setBounds(0, 40, 900, 511);
		contentPane.add(panelContent);

		JLabel lblWorkerId = new JLabel("Worker ID");
		lblWorkerId.setIcon(scaledIcon);
		lblWorkerId.setIconTextGap(8);
		lblWorkerId.setFont(fontWithSize);
		lblWorkerId.setBounds(21, 22, 235, 32);
		panelContent.add(lblWorkerId);

		txtWorkerId = new JTextField();
		txtWorkerId.setName("txtWorkerId");
		txtWorkerId.setFont(new Font("Arial", Font.PLAIN, 16));
		txtWorkerId.setColumns(10);
		txtWorkerId.setBounds(266, 24, 250, 32);
		panelContent.add(txtWorkerId);

		btnAdd = new JButton("Add");
		btnAdd.setFont(fontSizeBtn);
		btnAdd.setEnabled(false);
		btnAdd.setName("btnAdd");
		btnAdd.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnAdd.setBackground(new Color(252, 195, 174));
		btnAdd.setBounds(528, 60, 120, 30);
		btnAdd.setOpaque(true); // Set content area filled property

		panelContent.add(btnAdd);

		JLabel lblWorkerName = new JLabel("Worker Name");
		lblWorkerName.setIcon(scaledIcon);
		lblWorkerName.setIconTextGap(8);
		lblWorkerName.setFont(fontWithSize);
		lblWorkerName.setBounds(21, 60, 235, 32);
		panelContent.add(lblWorkerName);

		txtWorkerName = new JTextField();
		txtWorkerName.setName("txtWorkerName");
		txtWorkerName.setFont(new Font("Arial", Font.PLAIN, 16));
		txtWorkerName.setColumns(10);
		txtWorkerName.setBounds(266, 60, 250, 32);
		panelContent.add(txtWorkerName);

		JLabel lblWorkerPhoneNumber = new JLabel("Worker Phone No.");
		lblWorkerPhoneNumber.setIcon(scaledIcon);
		lblWorkerPhoneNumber.setIconTextGap(8);
		lblWorkerPhoneNumber.setFont(fontWithSize);
		lblWorkerPhoneNumber.setBounds(21, 90, 235, 32);
		panelContent.add(lblWorkerPhoneNumber);

		txtWorkerPhone = new JTextField();
		txtWorkerPhone.setName("txtWorkerPhone");
		txtWorkerPhone.setFont(new Font("Arial", Font.PLAIN, 16));
		txtWorkerPhone.setColumns(10);
		txtWorkerPhone.setBounds(266, 90, 250, 32);
		panelContent.add(txtWorkerPhone);

		JLabel lblWorkerCategory = new JLabel("Worker Category");
		lblWorkerCategory.setIcon(scaledIcon);
		lblWorkerCategory.setIconTextGap(8);
		lblWorkerCategory.setFont(fontWithSize);
		lblWorkerCategory.setBounds(21, 120, 235, 32);
		panelContent.add(lblWorkerCategory);

		btnUpdate = new JButton("Update");
		btnUpdate.setName("btnUpdate");
		btnUpdate.setEnabled(false);
		btnUpdate.setFont(fontSizeBtn);
		btnUpdate.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnUpdate.setBackground(new Color(252, 195, 174));
		btnUpdate.setBounds(528, 100, 120, 30);
		btnUpdate.setOpaque(true); // Set content area filled property

		panelContent.add(btnUpdate);

		btnFetch = new JButton("Fetch");
		btnFetch.setFont(fontSizeBtn);
		btnFetch.setName("btnFetch");
		btnFetch.setEnabled(false);
		btnFetch.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnFetch.setBackground(new Color(252, 195, 174));
		btnFetch.setBounds(526, 20, 120, 30);
		btnFetch.setOpaque(true); // Set content area filled property

		panelContent.add(btnFetch);

//		cmbWorkerCategory = new JComboBox();a
		cmbWorkerCategory.setName("cmbWorkerCategory");
		cmbWorkerCategory.setBounds(266, 120, 250, 32);
		panelContent.add(cmbWorkerCategory);

		JLabel lblSearchWorker = new JLabel("Search Worker");
		lblSearchWorker.setIconTextGap(8);
		lblSearchWorker.setFont(fontWithSize);
		lblSearchWorker.setBounds(21, 185, 166, 32);
		panelContent.add(lblSearchWorker);

		txtSearchWorker = new JTextField();
		txtSearchWorker.setName("txtSearchWorker");
		txtSearchWorker.setFont(new Font("Arial", Font.PLAIN, 16));
		txtSearchWorker.setColumns(10);
		txtSearchWorker.setBounds(197, 185, 189, 32);
		panelContent.add(txtSearchWorker);

		JLabel lblSearchBy = new JLabel("Search By");
		lblSearchBy.setIconTextGap(8);
		lblSearchBy.setFont(fontWithSize);
		lblSearchBy.setBounds(396, 185, 111, 32);
		panelContent.add(lblSearchBy);

		btnSearchWorker = new JButton("Search");
		btnSearchWorker.setEnabled(false);
		btnSearchWorker.setName("btnSearchWorker");
		btnSearchWorker.setFont(fontSizeBtn);
		btnSearchWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnSearchWorker.setBackground(new Color(252, 195, 174));
		btnSearchWorker.setBounds(704, 185, 120, 30);
		btnSearchWorker.setOpaque(true); // Set content area filled property

		panelContent.add(btnSearchWorker);

		cmbSearchByOptions.setName("cmbSearchByOptions");
		cmbSearchByOptions.setBounds(517, 185, 177, 32);
		panelContent.add(cmbSearchByOptions);

		workerListModel = new DefaultListModel<Worker>();
		listWorkers = new JList<>(workerListModel);
		listWorkers.setName("listWorkers");
		listWorkers.setBounds(21, 249, 673, 75);
		panelContent.add(listWorkers);

		btnDelete = new JButton("Delete");
		btnDelete.setName("btnDelete");
		btnDelete.setEnabled(false);
		btnDelete.setFont(fontSizeBtn);
		btnDelete.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnDelete.setOpaque(true); // Set content area filled property

		btnDelete.setBackground(new Color(252, 195, 174));
		btnDelete.setBounds(704, 294, 120, 30);
		panelContent.add(btnDelete);

		JLabel lblSearchOrdersBy = new JLabel("Search Orders By Worker ID");
		lblSearchOrdersBy.setIconTextGap(8);
		lblSearchOrdersBy.setFont(fontWithSize);
		lblSearchOrdersBy.setBounds(21, 336, 300, 30);
		panelContent.add(lblSearchOrdersBy);

		txtOrdersByWorkerId = new JTextField();
		txtOrdersByWorkerId.setName("txtOrdersByWorkerId");
		txtOrdersByWorkerId.setFont(new Font("Arial", Font.PLAIN, 16));
		txtOrdersByWorkerId.setColumns(10);
		txtOrdersByWorkerId.setBounds(331, 336, 185, 34);
		panelContent.add(txtOrdersByWorkerId);

		orderListModel = new DefaultListModel<CustomerOrder>();
		listOrders = new JList<>(orderListModel);
		listOrders.setName("listOrders");
		listOrders.setBounds(21, 398, 803, 75);
		panelContent.add(listOrders);

		btnSearchOrder = new JButton("Search");
		btnSearchOrder.setName("btnSearchOrder");
		btnSearchOrder.setEnabled(false);
		btnSearchOrder.setFont(fontSizeBtn);
		btnSearchOrder.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnSearchOrder.setBackground(new Color(252, 195, 174));
		btnSearchOrder.setBounds(526, 336, 120, 30);
		btnSearchOrder.setOpaque(true); // Set content area filled property

		panelContent.add(btnSearchOrder);

		showErrorLbl = new JLabel("");
		showErrorLbl.setName("showErrorLbl");
		showErrorLbl.setIconTextGap(8);
		showErrorLbl.setForeground(Color.RED);
		showErrorLbl.setFont(new Font("Arial", Font.BOLD, 14));
		showErrorLbl.setBounds(19, 164, 651, 20);
		panelContent.add(showErrorLbl);

		showErrorLblSearchWorker = new JLabel("");
		showErrorLblSearchWorker.setName("showErrorLblSearchWorker");

		showErrorLblSearchWorker.setIconTextGap(8);
		showErrorLblSearchWorker.setForeground(Color.RED);
		showErrorLblSearchWorker.setFont(new Font("Arial", Font.BOLD, 14));
		showErrorLblSearchWorker.setBounds(21, 225, 651, 20);
		panelContent.add(showErrorLblSearchWorker);

		showErrorLblSearchOrder = new JLabel("");
		showErrorLblSearchOrder.setName("showErrorLblSearchOrder");

		showErrorLblSearchOrder.setIconTextGap(8);
		showErrorLblSearchOrder.setForeground(Color.RED);
		showErrorLblSearchOrder.setFont(new Font("Arial", Font.BOLD, 14));
		showErrorLblSearchOrder.setBounds(19, 378, 651, 20);
		panelContent.add(showErrorLblSearchOrder);

		btnClearSearchWorker = new JButton("Clear");
		btnClearSearchWorker.setName("btnClearSearchWorker");
		btnClearSearchWorker.setEnabled(false);
		btnClearSearchWorker.setOpaque(true);
		btnClearSearchWorker.setFont(new Font("Arial", Font.PLAIN, 14));
		btnClearSearchWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnClearSearchWorker.setBackground(new Color(252, 195, 174));
		btnClearSearchWorker.setBounds(704, 143, 120, 30);
		panelContent.add(btnClearSearchWorker);

		JPanel panelFooter = new JPanel();
		panelFooter.setLayout(null);
		panelFooter.setBackground(new Color(250, 138, 132));
		panelFooter.setBounds(0, 550, 834, 45);
		contentPane.add(panelFooter);

		showErrorNotFoundLbl = new JLabel("");
		showErrorNotFoundLbl.setName("showErrorNotFoundLbl");
		showErrorNotFoundLbl.setFont(new Font("Arial", Font.BOLD, 13));
		showErrorNotFoundLbl.setBounds(10, 14, 814, 14);
		panelFooter.add(showErrorNotFoundLbl);

	}

	@Override
	public void showAllWorkers(List<Worker> worker) {
		resetAllSearchStates();

		worker.stream().forEach(workerListModel::addElement);
		resetErrorLabel();
	}

	@Override
	public void workerAdded(Worker worker) {
		workerListModel.addElement(worker);
		resetErrorLabel();
	}

	@Override
	public void workerModified(Worker worker) {
		for (int i = 0; i < workerListModel.getSize(); i++) {
			if (workerListModel.getElementAt(i).getWorkerId().equals(worker.getWorkerId())) {
				workerListModel.removeElementAt(i);
				workerListModel.addElement(worker);
				resetErrorLabel();
			} else {
				resetErrorLabel();
			}
		}
	}

	@Override
	public void showFetchedWorker(Worker worker) {
		txtWorkerName.setText(worker.getWorkerName());
		txtWorkerPhone.setText(worker.getWorkerPhoneNumber());
		cmbWorkerCategory.setSelectedItem(worker.getWorkerCategory());
		resetErrorLabel();
	}

	@Override
	public void showSearchResultForWorker(List<Worker> workers) {
		workerListModel.removeAllElements();
		workers.stream().forEach(workerListModel::addElement);
		resetErrorLabel();
	}

	@Override
	public void showOrderByWorkerId(List<CustomerOrder> orders) {
		orderListModel.removeAllElements();
		orders.stream().forEach(orderListModel::addElement);
		resetErrorLabel();
	}

	@Override
	public void workerRemoved(Worker worker) {
		workerListModel.removeElement(worker);
		resetErrorLabel();
	}

	@Override
	public void showError(String message, Worker worker) {
		showErrorLbl.setText(message + ": " + worker);
	}

	@Override
	public void showErrorNotFound(String message, Worker worker) {
		showErrorNotFoundLbl.setText(message + ": " + worker);
	}

	@Override
	public void showSearchError(String message, String searchText) {
		showErrorLblSearchWorker.setText(message + ": " + searchText);
	}

	@Override
	public void showSearchOrderByWorkerIdError(String message, Worker worker) {
		showErrorLblSearchOrder.setText(message + ": " + worker);
	}

	private void resetErrorLabel() {
		showErrorLbl.setText(" ");
		showErrorNotFoundLbl.setText(" ");
		showErrorLblSearchWorker.setText(" ");
		showErrorLblSearchOrder.setText(" ");
	}

	private void resetAllSearchStates() {
		txtSearchWorker.setText(" ");
		txtOrdersByWorkerId.setText(" ");
		cmbSearchByOptions.setSelectedItem(null);
	}
}
