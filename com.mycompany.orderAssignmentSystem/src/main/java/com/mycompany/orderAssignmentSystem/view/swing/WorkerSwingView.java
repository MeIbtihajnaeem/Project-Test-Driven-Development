package com.mycompany.orderAssignmentSystem.view.swing;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.mycompany.orderAssignmentSystem.controller.WorkerController;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.WorkerSearchOption;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.view.WorkerView;

/**
 * The WorkerSwingView class represents the graphical user interface for
 * managing workers and their orders.
 */
public class WorkerSwingView extends JFrame implements WorkerView {

	/** Unique identifier for serialization.. */
	private static final long serialVersionUID = 2L;

	/** The content pane. */
	private JPanel contentPane;

	/** The txt worker id. */
	private JTextField txtWorkerId;

	/** The txt worker name. */
	private JTextField txtWorkerName;

	/** The txt worker phone. */
	private JTextField txtWorkerPhone;

	/** The txt search worker. */
	private JTextField txtSearchWorker;

	/** The txt orders by worker id. */
	private JTextField txtOrdersByWorkerId;

	/** The worker controller. */
	private WorkerController workerController;

	/** The cmb worker category. */
	private JComboBox<OrderCategory> cmbWorkerCategory;

	/** The btn add. */
	private JButton btnAdd;

	/** The btn update. */
	private JButton btnUpdate;

	/** The btn fetch. */
	private JButton btnFetch;

	/** The btn delete. */
	private JButton btnDelete;

	/** The cmb search by options. */
	private JComboBox<WorkerSearchOption> cmbSearchByOptions;

	/** The btn search worker. */
	private JButton btnSearchWorker;

	/** The worker list model. */
	private DefaultListModel<Worker> workerListModel;

	/** The order list model. */
	private DefaultListModel<CustomerOrder> orderListModel;

	/** The list workers. */
	private JList<Worker> listWorkers;

	/** The list orders. */
	private JList<CustomerOrder> listOrders;

	/** The btn clear search worker. */
	private JButton btnClearSearchWorker;

	/** The btn search order. */
	private JButton btnSearchOrder;

	/** The show error while add, update or fetch has error. */
	private JLabel showErrorLbl;

	/** The show error while search worker has error. */
	private JLabel showErrorLblSearchWorker;

	/** The show error while search order has error. */
	private JLabel showErrorLblSearchOrder;

	/** The show error while worker not found error. */
	private JLabel showErrorNotFoundLbl;

	/** The scroll pane. */
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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

	/**
	 * Gets the worker list model.
	 *
	 * @return the worker list model
	 */
	DefaultListModel<Worker> getWorkerListModel() {
		return workerListModel;
	}

	/**
	 * Gets the order list model.
	 *
	 * @return the order list model
	 */
	DefaultListModel<CustomerOrder> getOrderListModel() {
		return orderListModel;
	}

	/**
	 * Create the frame.
	 */
	public WorkerSwingView() {

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

		setTitle("Worker View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 713, 461);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.textHighlight);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0,
				0.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JButton lblManageOrder = new JButton("Manage Order");
		lblManageOrder.setOpaque(true);
		lblManageOrder.setName("lblManageOrder");
		lblManageOrder.setHorizontalAlignment(SwingConstants.CENTER);
		lblManageOrder.setFont(new Font("Arial", Font.BOLD, 14));
		lblManageOrder.setEnabled(true);
		lblManageOrder.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		lblManageOrder.setBackground(new Color(250, 138, 132));
		GridBagConstraints gbc_lblManageOrder = new GridBagConstraints();
		gbc_lblManageOrder.insets = new Insets(0, 0, 5, 5);
		gbc_lblManageOrder.gridx = 3;
		gbc_lblManageOrder.gridy = 0;
		contentPane.add(lblManageOrder, gbc_lblManageOrder);

		JLabel lblWorkerId = new JLabel("Worker ID");
		lblWorkerId.setIconTextGap(8);
		lblWorkerId.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblWorkerId = new GridBagConstraints();
		gbc_lblWorkerId.anchor = GridBagConstraints.EAST;
		gbc_lblWorkerId.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkerId.gridx = 0;
		gbc_lblWorkerId.gridy = 1;
		contentPane.add(lblWorkerId, gbc_lblWorkerId);

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

		txtWorkerId = new JTextField();
		txtWorkerId.addKeyListener(changeWorkerValueListener);
		txtWorkerId.setName("txtWorkerId");
		txtWorkerId.setFont(new Font("Arial", Font.PLAIN, 16));
		txtWorkerId.setColumns(10);
		GridBagConstraints gbc_txtWorkerId = new GridBagConstraints();
		gbc_txtWorkerId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWorkerId.insets = new Insets(0, 0, 5, 5);
		gbc_txtWorkerId.gridx = 1;
		gbc_txtWorkerId.gridy = 1;
		contentPane.add(txtWorkerId, gbc_txtWorkerId);

		btnFetch = new JButton("Fetch");
		btnFetch.setOpaque(true);
		btnFetch.setName("btnFetch");
		btnFetch.setFont(new Font("Arial", Font.PLAIN, 14));
		btnFetch.setEnabled(false);
		btnFetch.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnFetch.setBackground(new Color(252, 195, 174));
		GridBagConstraints gbc_btnFetch = new GridBagConstraints();
		gbc_btnFetch.insets = new Insets(0, 0, 5, 5);
		gbc_btnFetch.gridx = 2;
		gbc_btnFetch.gridy = 1;
		contentPane.add(btnFetch, gbc_btnFetch);

		JLabel lblWorkerName = new JLabel("Worker Name");
		lblWorkerName.setIconTextGap(8);
		lblWorkerName.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblWorkerName = new GridBagConstraints();
		gbc_lblWorkerName.anchor = GridBagConstraints.EAST;
		gbc_lblWorkerName.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkerName.gridx = 0;
		gbc_lblWorkerName.gridy = 2;
		contentPane.add(lblWorkerName, gbc_lblWorkerName);

		txtWorkerName = new JTextField();
		txtWorkerName.addKeyListener(changeWorkerValueListener);
		txtWorkerName.setName("txtWorkerName");
		txtWorkerName.setFont(new Font("Arial", Font.PLAIN, 16));
		txtWorkerName.setColumns(10);
		GridBagConstraints gbc_txtWorkerName = new GridBagConstraints();
		gbc_txtWorkerName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWorkerName.insets = new Insets(0, 0, 5, 5);
		gbc_txtWorkerName.gridx = 1;
		gbc_txtWorkerName.gridy = 2;
		contentPane.add(txtWorkerName, gbc_txtWorkerName);

		btnUpdate = new JButton("Update");
		btnUpdate.setOpaque(true);
		btnUpdate.setName("btnUpdate");
		btnUpdate.setFont(new Font("Arial", Font.PLAIN, 14));
		btnUpdate.setEnabled(false);
		btnUpdate.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnUpdate.setBackground(new Color(252, 195, 174));
		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpdate.gridx = 2;
		gbc_btnUpdate.gridy = 2;
		contentPane.add(btnUpdate, gbc_btnUpdate);

		JLabel lblWorkerPhoneNumber = new JLabel("Worker Phone No.");
		lblWorkerPhoneNumber.setIconTextGap(8);
		lblWorkerPhoneNumber.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblWorkerPhoneNumber = new GridBagConstraints();
		gbc_lblWorkerPhoneNumber.anchor = GridBagConstraints.EAST;
		gbc_lblWorkerPhoneNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkerPhoneNumber.gridx = 0;
		gbc_lblWorkerPhoneNumber.gridy = 3;
		contentPane.add(lblWorkerPhoneNumber, gbc_lblWorkerPhoneNumber);

		txtWorkerPhone = new JTextField();
		txtWorkerPhone.addKeyListener(changeWorkerValueListener);
		txtWorkerPhone.setName("txtWorkerPhone");
		txtWorkerPhone.setFont(new Font("Arial", Font.PLAIN, 16));
		txtWorkerPhone.setColumns(10);
		GridBagConstraints gbc_txtWorkerPhone = new GridBagConstraints();
		gbc_txtWorkerPhone.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWorkerPhone.insets = new Insets(0, 0, 5, 5);
		gbc_txtWorkerPhone.gridx = 1;
		gbc_txtWorkerPhone.gridy = 3;
		contentPane.add(txtWorkerPhone, gbc_txtWorkerPhone);

		btnAdd = new JButton("Add");
		btnAdd.setOpaque(true);
		btnAdd.setName("btnAdd");
		btnAdd.setFont(new Font("Arial", Font.PLAIN, 14));
		btnAdd.setEnabled(false);
		btnAdd.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnAdd.setBackground(new Color(252, 195, 174));
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 3;
		contentPane.add(btnAdd, gbc_btnAdd);

		JLabel lblWorkerCategory = new JLabel("Worker Category");
		lblWorkerCategory.setIconTextGap(8);
		lblWorkerCategory.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblWorkerCategory = new GridBagConstraints();
		gbc_lblWorkerCategory.anchor = GridBagConstraints.EAST;
		gbc_lblWorkerCategory.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkerCategory.gridx = 0;
		gbc_lblWorkerCategory.gridy = 4;
		contentPane.add(lblWorkerCategory, gbc_lblWorkerCategory);

		cmbWorkerCategory.addActionListener(changeWorkerCategoryListener);
		cmbWorkerCategory.setName("cmbWorkerCategory");
		GridBagConstraints gbc_cmbWorkerCategory = new GridBagConstraints();
		gbc_cmbWorkerCategory.insets = new Insets(0, 0, 5, 5);
		gbc_cmbWorkerCategory.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbWorkerCategory.gridx = 1;
		gbc_cmbWorkerCategory.gridy = 4;
		contentPane.add(cmbWorkerCategory, gbc_cmbWorkerCategory);

		btnClearSearchWorker = new JButton("Clear");
		btnClearSearchWorker.setOpaque(true);
		btnClearSearchWorker.setName("btnClearSearchWorker");
		btnClearSearchWorker.setFont(new Font("Arial", Font.PLAIN, 14));
		btnClearSearchWorker.setEnabled(false);
		btnClearSearchWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnClearSearchWorker.setBackground(new Color(252, 195, 174));
		GridBagConstraints gbc_btnClearSearchWorker = new GridBagConstraints();
		gbc_btnClearSearchWorker.insets = new Insets(0, 0, 5, 0);
		gbc_btnClearSearchWorker.gridx = 4;
		gbc_btnClearSearchWorker.gridy = 5;
		contentPane.add(btnClearSearchWorker, gbc_btnClearSearchWorker);

		showErrorLbl = new JLabel("");
		showErrorLbl.setName("showErrorLbl");
		showErrorLbl.setIconTextGap(8);
		showErrorLbl.setForeground(Color.RED);
		showErrorLbl.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_showErrorLbl = new GridBagConstraints();
		gbc_showErrorLbl.insets = new Insets(0, 0, 5, 5);
		gbc_showErrorLbl.gridx = 0;
		gbc_showErrorLbl.gridy = 6;
		contentPane.add(showErrorLbl, gbc_showErrorLbl);

		JLabel lblSearchWorker = new JLabel("Search Worker");
		lblSearchWorker.setIconTextGap(8);
		lblSearchWorker.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblSearchWorker = new GridBagConstraints();
		gbc_lblSearchWorker.anchor = GridBagConstraints.EAST;
		gbc_lblSearchWorker.insets = new Insets(0, 0, 5, 5);
		gbc_lblSearchWorker.gridx = 0;
		gbc_lblSearchWorker.gridy = 7;
		contentPane.add(lblSearchWorker, gbc_lblSearchWorker);

		txtSearchWorker = new JTextField();
		txtSearchWorker.addKeyListener(changeWorkerValueListener);

		txtSearchWorker.setName("txtSearchWorker");
		txtSearchWorker.setFont(new Font("Arial", Font.PLAIN, 16));
		txtSearchWorker.setColumns(10);
		GridBagConstraints gbc_txtSearchWorker = new GridBagConstraints();
		gbc_txtSearchWorker.insets = new Insets(0, 0, 5, 5);
		gbc_txtSearchWorker.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSearchWorker.gridx = 1;
		gbc_txtSearchWorker.gridy = 7;
		contentPane.add(txtSearchWorker, gbc_txtSearchWorker);

		JLabel lblSearchBy = new JLabel("Search By");
		lblSearchBy.setIconTextGap(8);
		lblSearchBy.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblSearchBy = new GridBagConstraints();
		gbc_lblSearchBy.insets = new Insets(0, 0, 5, 5);
		gbc_lblSearchBy.gridx = 2;
		gbc_lblSearchBy.gridy = 7;
		contentPane.add(lblSearchBy, gbc_lblSearchBy);

		cmbSearchByOptions.addActionListener(changeWorkerCategoryListener);
		cmbSearchByOptions.setName("cmbSearchByOptions");
		GridBagConstraints gbc_cmbSearchByOptions = new GridBagConstraints();
		gbc_cmbSearchByOptions.insets = new Insets(0, 0, 5, 5);
		gbc_cmbSearchByOptions.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbSearchByOptions.gridx = 3;
		gbc_cmbSearchByOptions.gridy = 7;
		contentPane.add(cmbSearchByOptions, gbc_cmbSearchByOptions);

		btnSearchWorker = new JButton("Search");
		btnSearchWorker.setOpaque(true);
		btnSearchWorker.setName("btnSearchWorker");
		btnSearchWorker.setFont(new Font("Arial", Font.PLAIN, 14));
		btnSearchWorker.setEnabled(false);
		btnSearchWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnSearchWorker.setBackground(new Color(252, 195, 174));
		GridBagConstraints gbc_btnSearchWorker = new GridBagConstraints();
		gbc_btnSearchWorker.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearchWorker.gridx = 4;
		gbc_btnSearchWorker.gridy = 7;
		contentPane.add(btnSearchWorker, gbc_btnSearchWorker);

		showErrorLblSearchWorker = new JLabel("");
		showErrorLblSearchWorker.setName("showErrorLblSearchWorker");
		showErrorLblSearchWorker.setIconTextGap(8);
		showErrorLblSearchWorker.setForeground(Color.RED);
		showErrorLblSearchWorker.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_showErrorLblSearchWorker = new GridBagConstraints();
		gbc_showErrorLblSearchWorker.gridwidth = 2;
		gbc_showErrorLblSearchWorker.insets = new Insets(0, 0, 5, 5);
		gbc_showErrorLblSearchWorker.gridx = 0;
		gbc_showErrorLblSearchWorker.gridy = 10;
		contentPane.add(showErrorLblSearchWorker, gbc_showErrorLblSearchWorker);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 9;
		contentPane.add(scrollPane, gbc_scrollPane);

		workerListModel = new DefaultListModel<>();
		listWorkers = new JList<>(workerListModel);

		listWorkers.addListSelectionListener(e -> btnDelete.setEnabled(listWorkers.getSelectedIndex() != -1));
		listWorkers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listWorkers.setName("listWorkers");
		scrollPane.setViewportView(listWorkers);
		btnDelete = new JButton("Delete");
		btnDelete.setOpaque(true);
		btnDelete.setName("btnDelete");
		btnDelete.setFont(new Font("Arial", Font.PLAIN, 14));
		btnDelete.setEnabled(false);
		btnDelete.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnDelete.setBackground(new Color(252, 195, 174));
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 5, 0);
		gbc_btnDelete.gridx = 4;
		gbc_btnDelete.gridy = 11;
		contentPane.add(btnDelete, gbc_btnDelete);

		JLabel lblSearchOrdersBy = new JLabel("Search Orders By Worker ID");
		lblSearchOrdersBy.setIconTextGap(8);
		lblSearchOrdersBy.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblSearchOrdersBy = new GridBagConstraints();
		gbc_lblSearchOrdersBy.anchor = GridBagConstraints.EAST;
		gbc_lblSearchOrdersBy.insets = new Insets(0, 0, 5, 5);
		gbc_lblSearchOrdersBy.gridx = 0;
		gbc_lblSearchOrdersBy.gridy = 12;
		contentPane.add(lblSearchOrdersBy, gbc_lblSearchOrdersBy);

		txtOrdersByWorkerId = new JTextField();
		txtOrdersByWorkerId.addKeyListener(changeWorkerValueListener);
		txtOrdersByWorkerId.setName("txtOrdersByWorkerId");
		txtOrdersByWorkerId.setFont(new Font("Arial", Font.PLAIN, 16));
		txtOrdersByWorkerId.setColumns(10);
		GridBagConstraints gbc_txtOrdersByWorkerId = new GridBagConstraints();
		gbc_txtOrdersByWorkerId.insets = new Insets(0, 0, 5, 5);
		gbc_txtOrdersByWorkerId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtOrdersByWorkerId.gridx = 1;
		gbc_txtOrdersByWorkerId.gridy = 12;
		contentPane.add(txtOrdersByWorkerId, gbc_txtOrdersByWorkerId);

		btnSearchOrder = new JButton("Search");
		btnSearchOrder.setOpaque(true);
		btnSearchOrder.setName("btnSearchOrder");
		btnSearchOrder.setFont(new Font("Arial", Font.PLAIN, 14));
		btnSearchOrder.setEnabled(false);
		btnSearchOrder.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnSearchOrder.setBackground(new Color(252, 195, 174));
		GridBagConstraints gbc_btnSearchOrder = new GridBagConstraints();
		gbc_btnSearchOrder.insets = new Insets(0, 0, 5, 5);
		gbc_btnSearchOrder.gridx = 2;
		gbc_btnSearchOrder.gridy = 12;
		contentPane.add(btnSearchOrder, gbc_btnSearchOrder);

		showErrorLblSearchOrder = new JLabel("");
		showErrorLblSearchOrder.setName("showErrorLblSearchOrder");
		showErrorLblSearchOrder.setIconTextGap(8);
		showErrorLblSearchOrder.setForeground(Color.RED);
		showErrorLblSearchOrder.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_showErrorLblSearchOrder = new GridBagConstraints();
		gbc_showErrorLblSearchOrder.insets = new Insets(0, 0, 5, 5);
		gbc_showErrorLblSearchOrder.gridx = 0;
		gbc_showErrorLblSearchOrder.gridy = 13;
		contentPane.add(showErrorLblSearchOrder, gbc_showErrorLblSearchOrder);
		orderListModel = new DefaultListModel<>();
		listOrders = new JList<>(orderListModel);
		listOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listOrders.setName("listOrders");
		GridBagConstraints gbc_listOrders = new GridBagConstraints();
		gbc_listOrders.gridwidth = 4;
		gbc_listOrders.insets = new Insets(0, 0, 5, 5);
		gbc_listOrders.fill = GridBagConstraints.BOTH;
		gbc_listOrders.gridx = 0;
		gbc_listOrders.gridy = 12;
		contentPane.add(listOrders, gbc_listOrders);

		showErrorNotFoundLbl = new JLabel("");
		showErrorNotFoundLbl.setName("showErrorNotFoundLbl");
		showErrorNotFoundLbl.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_lblError = new GridBagConstraints();
		gbc_lblError.gridheight = 2;
		gbc_lblError.gridwidth = 2;
		gbc_lblError.insets = new Insets(0, 0, 0, 5);
		gbc_lblError.gridx = 0;
		gbc_lblError.gridy = 17;
		contentPane.add(showErrorNotFoundLbl, gbc_lblError);

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
				} else {
					workerController.deleteWorker(listWorkers.getSelectedValue());

				}
			}

		};

		btnAdd.addActionListener(crudActionListener);
		btnUpdate.addActionListener(crudActionListener);
		btnFetch.addActionListener(crudActionListener);
		btnDelete.addActionListener(filterActionListener);
		btnSearchOrder.addActionListener(crudActionListener);
		btnSearchWorker.addActionListener(filterActionListener);
		btnClearSearchWorker.addActionListener(filterActionListener);

	}

	/**
	 * Sets the worker controller.
	 *
	 * @param workerController the new worker controller
	 */
	public void setWorkerController(WorkerController workerController) {
		this.workerController = workerController;
	}

	/**
	 * Handle button and combo box states.
	 */
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

	/**
	 * Handle search worker and clear button states.
	 */
	private void handleSearchWorkerAndClearButtonStates() {
		boolean isSearchWorkerTextEmpty = txtSearchWorker.getText().trim().isEmpty();
		boolean isSearchOptionEmpty = cmbSearchByOptions.getSelectedItem() == null;
		btnSearchWorker.setEnabled(!isSearchWorkerTextEmpty && !isSearchOptionEmpty);
		btnClearSearchWorker.setEnabled(!isSearchWorkerTextEmpty && !isSearchOptionEmpty);
	}

	/**
	 * Handle search order by worker id button states.
	 */
	private void handleSearchOrderByWorkerIdButtonStates() {
		boolean isOrdersByWorkerId = txtOrdersByWorkerId.getText().trim().isEmpty();
		btnSearchOrder.setEnabled(!isOrdersByWorkerId);
	}

	/**
	 * Show all workers.
	 *
	 * @param worker the worker
	 */
	@Override
	public void showAllWorkers(List<Worker> worker) {
		resetAllSearchStates();

		worker.stream().forEach(workerListModel::addElement);
		resetErrorLabel();
	}

	/**
	 * Worker added.
	 *
	 * @param worker the worker
	 */
	@Override
	public void workerAdded(Worker worker) {
		workerListModel.addElement(worker);
		resetErrorLabel();
	}

	/**
	 * Worker modified.
	 *
	 * @param worker the worker
	 */
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

	/**
	 * Show fetched worker.
	 *
	 * @param worker the worker
	 */
	@Override
	public void showFetchedWorker(Worker worker) {
		txtWorkerName.setText(worker.getWorkerName());
		txtWorkerPhone.setText(worker.getWorkerPhoneNumber());
		cmbWorkerCategory.setSelectedItem(worker.getWorkerCategory());
		resetErrorLabel();
	}

	/**
	 * Show search result for worker.
	 *
	 * @param workers the workers
	 */
	@Override
	public void showSearchResultForWorker(List<Worker> workers) {
		workerListModel.removeAllElements();
		workers.stream().forEach(workerListModel::addElement);
		resetErrorLabel();
	}

	/**
	 * Show order by worker id.
	 *
	 * @param orders the orders
	 */
	@Override
	public void showOrderByWorkerId(List<CustomerOrder> orders) {
		orderListModel.removeAllElements();
		orders.stream().forEach(orderListModel::addElement);
		resetErrorLabel();
	}

	/**
	 * Worker removed.
	 *
	 * @param worker the worker
	 */
	@Override
	public void workerRemoved(Worker worker) {
		workerListModel.removeElement(worker);
		resetErrorLabel();
	}

	/**
	 * Show error.
	 *
	 * @param message the message
	 * @param worker  the worker
	 */
	@Override
	public void showError(String message, Worker worker) {
		showErrorLbl.setText(message + ": " + worker);
	}

	/**
	 * Show error not found.
	 *
	 * @param message the message
	 * @param worker  the worker
	 */
	@Override
	public void showErrorNotFound(String message, Worker worker) {
		showErrorNotFoundLbl.setText(message + ": " + worker);
	}

	/**
	 * Show search error.
	 *
	 * @param message    the message
	 * @param searchText the search text
	 */
	@Override
	public void showSearchError(String message, String searchText) {
		showErrorLblSearchWorker.setText(message + ": " + searchText);
	}

	/**
	 * Show search order by worker id error.
	 *
	 * @param message the message
	 * @param worker  the worker
	 */
	@Override
	public void showSearchOrderByWorkerIdError(String message, Worker worker) {
		showErrorLblSearchOrder.setText(message + ": " + worker);
	}

	/**
	 * Reset error label.
	 */
	private void resetErrorLabel() {
		showErrorLbl.setText(" ");
		showErrorNotFoundLbl.setText(" ");
		showErrorLblSearchWorker.setText(" ");
		showErrorLblSearchOrder.setText(" ");
	}

	/**
	 * Reset all search states.
	 */
	private void resetAllSearchStates() {
		txtSearchWorker.setText(" ");
		txtOrdersByWorkerId.setText(" ");
		cmbSearchByOptions.setSelectedItem(null);
	}

}
