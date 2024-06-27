package com.mycompany.orderassignmentsystem.view.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.mycompany.orderassignmentsystem.controller.OrderController;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderSearchOptions;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.view.OrderView;

/**
 * The OrderSwingView class represents the graphical user interface for managing
 * orders.
 */
public class OrderSwingView extends JFrame implements OrderView {

	/** The Constant ARIAL. */
	private static final String ARIAL = "Arial";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2L;

	/** The content pane. */
	private JPanel contentPane;

	/** The txt order id. */
	private JTextField txtOrderId;

	/** The txt customer name. */
	private JTextField txtCustomerName;

	/** The txt customer address. */
	private JTextField txtCustomerAddress;

	/** The txt customer phone. */
	private JTextField txtCustomerPhone;

	/** The txt order description. */
	private JTextField txtOrderDescription;

	/** The txt search order. */
	private JTextField txtSearchOrder;

	/** The btn fetch. */
	private JButton btnFetch;

	/** The btn add. */
	private JButton btnAdd;

	/** The btn update. */
	private JButton btnUpdate;

	/** The btn clear search. */
	private JButton btnClearSearch;

	/** The btn search order. */
	private JButton btnSearchOrder;

	/** The btn delete. */
	private JButton btnDelete;

	/** The show error not found lbl. */
	private JLabel showErrorNotFoundLbl;

	/** The txt selected date. */
	private JTextField txtSelectedDate;

	/** The show error. */
	private JLabel showError;

	/** The show search error lbl. */
	private JLabel showSearchErrorLbl;

	/** The order controller. */
	private transient OrderController orderController;

	/** The worker swing view. */
	private WorkerSwingView workerSwingView;

	/** The cmb order category. */
	private JComboBox<OrderCategory> cmbOrderCategory;

	/** The cmb search by. */
	private JComboBox<OrderSearchOptions> cmbSearchBy;

	/** The cmb order status. */
	private JComboBox<OrderStatus> cmbOrderStatus;

	/** The worker list model. */
	private DefaultComboBoxModel<Worker> workerListModel;

	/** The order list model. */
	private DefaultListModel<CustomerOrder> orderListModel;

	/** The list orders. */
	private JList<CustomerOrder> listOrders;

	/** The cmb worker. */
	private JComboBox<Worker> cmbWorker;

	/**
	 * Sets the order controller.
	 *
	 * @param orderController the new order controller
	 */
	public void setOrderController(OrderController orderController) {
		this.orderController = orderController;
	}

	/**
	 * Gets the worker list model.
	 *
	 * @return the worker list model
	 */
	public DefaultComboBoxModel<Worker> getWorkerListModel() {
		return workerListModel;
	}

	/**
	 * Gets the order list model.
	 *
	 * @return the order list model
	 */
	public DefaultListModel<CustomerOrder> getOrderListModel() {
		return orderListModel;
	}

	/**
	 * Sets the worker swing view.
	 *
	 * @param workerSwingView the new worker swing view
	 */
	public void setWorkerSwingView(WorkerSwingView workerSwingView) {
		this.workerSwingView = workerSwingView;
	}

	/**
	 * Create the frame.
	 */
	public OrderSwingView() {
		cmbOrderCategory = new JComboBox<>();
		cmbSearchBy = new JComboBox<>();
		cmbOrderStatus = new JComboBox<>();
		workerListModel = new DefaultComboBoxModel<>();
		cmbWorker = new JComboBox<>(workerListModel);
		txtOrderId = new JTextField();
		txtSelectedDate = new JTextField();
		txtCustomerName = new JTextField();
		txtCustomerAddress = new JTextField();
		txtCustomerPhone = new JTextField();
		txtOrderDescription = new JTextField();
		btnAdd = new JButton("Add");
		btnUpdate = new JButton("Update");
		btnFetch = new JButton("Fetch");
		txtSearchOrder = new JTextField();

		for (OrderCategory category : OrderCategory.values()) {
			cmbOrderCategory.addItem(category);
		}

		for (OrderStatus status : OrderStatus.values()) {
			cmbOrderStatus.addItem(status);
		}
		for (OrderSearchOptions searchOption : OrderSearchOptions.values()) {
			cmbSearchBy.addItem(searchOption);
		}
		cmbOrderCategory.setSelectedItem(null);
		cmbSearchBy.setSelectedItem(null);
		cmbOrderStatus.setSelectedItem(null);
		cmbWorker.setSelectedItem(null);

		setTitle("Order Form");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 742, 629);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("ColorChooser.background"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gblContentPane = new GridBagLayout();
		gblContentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gblContentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gblContentPane.columnWeights = new double[] { 1.0, 1.0, 0.0, 1.0, 0.0 };
		gblContentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0,
				1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gblContentPane);

		JButton btnManageWorker = new JButton("Manage Worker");
		btnManageWorker.setName("btnManageWorker");
		btnManageWorker.setForeground(Color.WHITE);
		btnManageWorker.setOpaque(true);
		btnManageWorker.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnManageWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnManageWorker.setBackground(Color.RED);
		btnManageWorker.setFocusPainted(false);
		btnManageWorker.setPreferredSize(new Dimension(150, 40));
		btnManageWorker.addActionListener(e -> openWorkerForm());

		GridBagConstraints gbcBtnManageWorker = new GridBagConstraints();
		gbcBtnManageWorker.ipady = 10;
		gbcBtnManageWorker.ipadx = 20;
		gbcBtnManageWorker.insets = new Insets(0, 0, 5, 0);
		gbcBtnManageWorker.gridx = 5;
		gbcBtnManageWorker.gridy = 0;
		contentPane.add(btnManageWorker, gbcBtnManageWorker);

		JLabel lblWorkerId = new JLabel("Order ID");
		lblWorkerId.setIconTextGap(8);
		lblWorkerId.setFont(new Font(ARIAL, Font.BOLD, 14));
		GridBagConstraints gbcLblWorkerId = new GridBagConstraints();
		gbcLblWorkerId.anchor = GridBagConstraints.EAST;
		gbcLblWorkerId.insets = new Insets(0, 0, 5, 5);
		gbcLblWorkerId.gridx = 0;
		gbcLblWorkerId.gridy = 1;
		contentPane.add(lblWorkerId, gbcLblWorkerId);

		txtOrderId.setName("txtOrderId");
		txtOrderId.setFont(new Font(ARIAL, Font.PLAIN, 12));
		txtOrderId.setColumns(10);
		txtOrderId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}

			@Override
			public void keyTyped(KeyEvent event) {
				checkCharacterIsNumber(event);
			}

		});

		GridBagConstraints gbcTxtOrderId = new GridBagConstraints();
		gbcTxtOrderId.ipady = 10;
		gbcTxtOrderId.ipadx = 10;
		gbcTxtOrderId.insets = new Insets(0, 0, 5, 5);
		gbcTxtOrderId.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtOrderId.gridx = 1;
		gbcTxtOrderId.gridy = 1;
		contentPane.add(txtOrderId, gbcTxtOrderId);

		JLabel lblAppointmentDate = new JLabel("Appointment Date");
		lblAppointmentDate.setIconTextGap(8);
		lblAppointmentDate.setFont(new Font(ARIAL, Font.BOLD, 14));
		GridBagConstraints gbcLblAppointmentDate = new GridBagConstraints();
		gbcLblAppointmentDate.anchor = GridBagConstraints.EAST;
		gbcLblAppointmentDate.insets = new Insets(0, 0, 5, 5);
		gbcLblAppointmentDate.gridx = 2;
		gbcLblAppointmentDate.gridy = 1;
		contentPane.add(lblAppointmentDate, gbcLblAppointmentDate);

		txtSelectedDate.setName("txtSelectedDate");
		txtSelectedDate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}
		});

		txtSelectedDate.setFont(new Font(ARIAL, Font.PLAIN, 12));
		txtSelectedDate.setColumns(10);
		GridBagConstraints gbcLblSelectedDate = new GridBagConstraints();
		gbcLblSelectedDate.fill = GridBagConstraints.HORIZONTAL;
		gbcLblSelectedDate.ipady = 10;
		gbcLblSelectedDate.ipadx = 10;
		gbcLblSelectedDate.insets = new Insets(0, 0, 5, 5);
		gbcLblSelectedDate.gridx = 3;
		gbcLblSelectedDate.gridy = 1;
		contentPane.add(txtSelectedDate, gbcLblSelectedDate);

		JLabel lblCustomerName = new JLabel("Customer Name");
		lblCustomerName.setIconTextGap(8);
		lblCustomerName.setFont(new Font(ARIAL, Font.BOLD, 14));
		GridBagConstraints gbcLblCustomerName = new GridBagConstraints();
		gbcLblCustomerName.anchor = GridBagConstraints.EAST;
		gbcLblCustomerName.insets = new Insets(0, 0, 5, 5);
		gbcLblCustomerName.gridx = 0;
		gbcLblCustomerName.gridy = 2;
		contentPane.add(lblCustomerName, gbcLblCustomerName);

		txtCustomerName.setName("txtCustomerName");
		txtCustomerName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}
		});

		txtCustomerName.setFont(new Font(ARIAL, Font.PLAIN, 12));
		txtCustomerName.setColumns(10);
		GridBagConstraints gbcTxtCustomerName = new GridBagConstraints();
		gbcTxtCustomerName.ipady = 10;
		gbcTxtCustomerName.ipadx = 10;
		gbcTxtCustomerName.insets = new Insets(0, 0, 5, 5);
		gbcTxtCustomerName.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtCustomerName.gridx = 1;
		gbcTxtCustomerName.gridy = 2;
		contentPane.add(txtCustomerName, gbcTxtCustomerName);

		JLabel lblOrderCategory = new JLabel("Order Category");
		lblOrderCategory.setIconTextGap(8);
		lblOrderCategory.setFont(new Font(ARIAL, Font.BOLD, 14));
		GridBagConstraints gbcLblOrderCategory = new GridBagConstraints();
		gbcLblOrderCategory.anchor = GridBagConstraints.EAST;
		gbcLblOrderCategory.insets = new Insets(0, 0, 5, 5);
		gbcLblOrderCategory.gridx = 2;
		gbcLblOrderCategory.gridy = 2;
		contentPane.add(lblOrderCategory, gbcLblOrderCategory);

		cmbOrderCategory.setName("cmbOrderCategory");
		cmbOrderCategory.addActionListener(e -> handleButtonAndComboBoxStates());

		GridBagConstraints gbcCmbOrderCategory = new GridBagConstraints();
		gbcCmbOrderCategory.ipady = 10;
		gbcCmbOrderCategory.ipadx = 20;
		gbcCmbOrderCategory.insets = new Insets(0, 0, 5, 5);
		gbcCmbOrderCategory.fill = GridBagConstraints.HORIZONTAL;
		gbcCmbOrderCategory.gridx = 3;
		gbcCmbOrderCategory.gridy = 2;
		contentPane.add(cmbOrderCategory, gbcCmbOrderCategory);

		JLabel lblWorkerPhoneNumber = new JLabel("Customer Address");
		lblWorkerPhoneNumber.setIconTextGap(8);
		lblWorkerPhoneNumber.setFont(new Font(ARIAL, Font.BOLD, 14));
		GridBagConstraints gbcLblWorkerPhoneNumber = new GridBagConstraints();
		gbcLblWorkerPhoneNumber.anchor = GridBagConstraints.EAST;
		gbcLblWorkerPhoneNumber.insets = new Insets(0, 0, 5, 5);
		gbcLblWorkerPhoneNumber.gridx = 0;
		gbcLblWorkerPhoneNumber.gridy = 3;
		contentPane.add(lblWorkerPhoneNumber, gbcLblWorkerPhoneNumber);

		txtCustomerAddress.setName("txtCustomerAddress");
		txtCustomerAddress.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}
		});

		txtCustomerAddress.setFont(new Font(ARIAL, Font.PLAIN, 12));
		txtCustomerAddress.setColumns(10);
		GridBagConstraints gbcTxtCustomerAddress = new GridBagConstraints();
		gbcTxtCustomerAddress.ipady = 10;
		gbcTxtCustomerAddress.ipadx = 10;
		gbcTxtCustomerAddress.insets = new Insets(0, 0, 5, 5);
		gbcTxtCustomerAddress.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtCustomerAddress.gridx = 1;
		gbcTxtCustomerAddress.gridy = 3;
		contentPane.add(txtCustomerAddress, gbcTxtCustomerAddress);

		JLabel lblOrderStatus = new JLabel("Order Status");
		lblOrderStatus.setIconTextGap(8);
		lblOrderStatus.setFont(new Font(ARIAL, Font.BOLD, 14));
		GridBagConstraints gbcLblOrderStatus = new GridBagConstraints();
		gbcLblOrderStatus.anchor = GridBagConstraints.EAST;
		gbcLblOrderStatus.insets = new Insets(0, 0, 5, 5);
		gbcLblOrderStatus.gridx = 2;
		gbcLblOrderStatus.gridy = 3;
		contentPane.add(lblOrderStatus, gbcLblOrderStatus);

		cmbOrderStatus.setName("cmbOrderStatus");
		cmbOrderStatus.addActionListener(e -> handleButtonAndComboBoxStates());

		GridBagConstraints gbcCmbOrderStatus = new GridBagConstraints();
		gbcCmbOrderStatus.ipady = 10;
		gbcCmbOrderStatus.ipadx = 20;
		gbcCmbOrderStatus.insets = new Insets(0, 0, 5, 5);
		gbcCmbOrderStatus.fill = GridBagConstraints.HORIZONTAL;
		gbcCmbOrderStatus.gridx = 3;
		gbcCmbOrderStatus.gridy = 3;
		contentPane.add(cmbOrderStatus, gbcCmbOrderStatus);

		JLabel lblCustomerPhone = new JLabel("Customer Phone #");
		lblCustomerPhone.setIconTextGap(8);
		lblCustomerPhone.setFont(new Font(ARIAL, Font.BOLD, 14));
		GridBagConstraints gbcLblCustomerPhone = new GridBagConstraints();
		gbcLblCustomerPhone.anchor = GridBagConstraints.EAST;
		gbcLblCustomerPhone.insets = new Insets(0, 0, 5, 5);
		gbcLblCustomerPhone.gridx = 0;
		gbcLblCustomerPhone.gridy = 4;
		contentPane.add(lblCustomerPhone, gbcLblCustomerPhone);

		txtCustomerPhone.setName("txtCustomerPhone");
		txtCustomerPhone.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}
		});

		txtCustomerPhone.setFont(new Font(ARIAL, Font.PLAIN, 12));
		txtCustomerPhone.setColumns(10);
		GridBagConstraints gbcTxtCustomerPhone = new GridBagConstraints();
		gbcTxtCustomerPhone.ipady = 10;
		gbcTxtCustomerPhone.ipadx = 10;
		gbcTxtCustomerPhone.insets = new Insets(0, 0, 5, 5);
		gbcTxtCustomerPhone.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtCustomerPhone.gridx = 1;
		gbcTxtCustomerPhone.gridy = 4;
		contentPane.add(txtCustomerPhone, gbcTxtCustomerPhone);

		JLabel lblWorkerCategory = new JLabel("Worker");
		lblWorkerCategory.setIconTextGap(8);
		lblWorkerCategory.setFont(new Font(ARIAL, Font.BOLD, 14));
		GridBagConstraints gbcLblWorkerCategory = new GridBagConstraints();
		gbcLblWorkerCategory.anchor = GridBagConstraints.EAST;
		gbcLblWorkerCategory.insets = new Insets(0, 0, 5, 5);
		gbcLblWorkerCategory.gridx = 2;
		gbcLblWorkerCategory.gridy = 4;
		contentPane.add(lblWorkerCategory, gbcLblWorkerCategory);

		cmbWorker.setName("cmbWorker");
		cmbWorker.addActionListener(e -> handleButtonAndComboBoxStates());

		GridBagConstraints gbcCmbWorker = new GridBagConstraints();
		gbcCmbWorker.ipady = 10;
		gbcCmbWorker.ipadx = 20;
		gbcCmbWorker.insets = new Insets(0, 0, 5, 5);
		gbcCmbWorker.fill = GridBagConstraints.HORIZONTAL;
		gbcCmbWorker.gridx = 3;
		gbcCmbWorker.gridy = 4;
		contentPane.add(cmbWorker, gbcCmbWorker);

		JLabel lblOrderDescription = new JLabel("Order Description");
		lblOrderDescription.setIconTextGap(8);
		lblOrderDescription.setFont(new Font(ARIAL, Font.BOLD, 14));
		GridBagConstraints gbcLblOrderDescription = new GridBagConstraints();
		gbcLblOrderDescription.anchor = GridBagConstraints.EAST;
		gbcLblOrderDescription.insets = new Insets(0, 0, 5, 5);
		gbcLblOrderDescription.gridx = 0;
		gbcLblOrderDescription.gridy = 5;
		contentPane.add(lblOrderDescription, gbcLblOrderDescription);

		txtOrderDescription.setName("txtOrderDescription");
		txtOrderDescription.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}
		});
		txtOrderDescription.setFont(new Font(ARIAL, Font.PLAIN, 12));
		txtOrderDescription.setColumns(10);
		GridBagConstraints gbcTxtOrderDescription = new GridBagConstraints();
		gbcTxtOrderDescription.ipady = 10;
		gbcTxtOrderDescription.ipadx = 10;
		gbcTxtOrderDescription.insets = new Insets(0, 0, 5, 5);
		gbcTxtOrderDescription.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtOrderDescription.gridx = 1;
		gbcTxtOrderDescription.gridy = 5;
		contentPane.add(txtOrderDescription, gbcTxtOrderDescription);

		showError = new JLabel("");
		showError.setName("showError");
		showError.setForeground(new Color(139, 0, 0));
		showError.setFont(new Font(ARIAL, Font.PLAIN, 16));
		showError.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbcShowError = new GridBagConstraints();
		gbcShowError.gridwidth = 6;
		gbcShowError.insets = new Insets(0, 0, 5, 0);
		gbcShowError.gridx = 0;
		gbcShowError.gridy = 6;
		contentPane.add(showError, gbcShowError);

		btnFetch.setEnabled(false);
		btnFetch.setName("btnFetch");
		btnFetch.setForeground(Color.WHITE);
		btnFetch.setOpaque(true);
		btnFetch.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnFetch.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnFetch.setBackground(new Color(59, 89, 182));
		btnFetch.setFocusPainted(false);
		btnFetch.setPreferredSize(new Dimension(150, 40));
		btnFetch.addActionListener(e -> fetchOrderMethod());

		GridBagConstraints gbcBtnFetch = new GridBagConstraints();
		gbcBtnFetch.ipady = 10;
		gbcBtnFetch.ipadx = 20;
		gbcBtnFetch.gridy = 7;
		gbcBtnFetch.insets = new Insets(0, 0, 5, 5);
		gbcBtnFetch.gridx = 0;
		contentPane.add(btnFetch, gbcBtnFetch);

		btnAdd.setEnabled(false);
		btnAdd.setName("btnAdd");
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setOpaque(true);
		btnAdd.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnAdd.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnAdd.setBackground(new Color(59, 89, 182));
		btnAdd.setFocusPainted(false);
		btnAdd.setPreferredSize(new Dimension(150, 40));
		btnAdd.addActionListener(e -> addOrderMethod());

		GridBagConstraints gbcBtnAdd = new GridBagConstraints();
		gbcBtnAdd.ipady = 10;
		gbcBtnAdd.ipadx = 20;
		gbcBtnAdd.insets = new Insets(0, 0, 5, 5);
		gbcBtnAdd.gridx = 1;
		gbcBtnAdd.gridy = 7;
		contentPane.add(btnAdd, gbcBtnAdd);

		btnUpdate.setEnabled(false);
		btnUpdate.setName("btnUpdate");
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setOpaque(true);
		btnUpdate.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnUpdate.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnUpdate.setBackground(new Color(59, 89, 182));
		btnUpdate.setFocusPainted(false);
		btnUpdate.setPreferredSize(new Dimension(150, 40));
		btnUpdate.addActionListener(e -> updateOrderMethod());

		GridBagConstraints gbcBtnUpdate = new GridBagConstraints();
		gbcBtnUpdate.ipady = 10;
		gbcBtnUpdate.ipadx = 20;
		gbcBtnUpdate.insets = new Insets(0, 0, 5, 5);
		gbcBtnUpdate.gridx = 2;
		gbcBtnUpdate.gridy = 7;
		contentPane.add(btnUpdate, gbcBtnUpdate);

		btnClearSearch = new JButton("Clear");
		btnClearSearch.setEnabled(false);
		btnClearSearch.setName("btnClearSearch");
		btnClearSearch.setForeground(Color.WHITE);
		btnClearSearch.setOpaque(true);
		btnClearSearch.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnClearSearch.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnClearSearch.setBackground(new Color(59, 89, 182));
		btnClearSearch.setFocusPainted(false);
		btnClearSearch.setPreferredSize(new Dimension(150, 40));
		btnClearSearch.addActionListener(e -> resetAllFields());
		GridBagConstraints gbcBtnClearSearch = new GridBagConstraints();
		gbcBtnClearSearch.ipady = 10;
		gbcBtnClearSearch.ipadx = 20;
		gbcBtnClearSearch.insets = new Insets(0, 0, 5, 5);
		gbcBtnClearSearch.gridx = 3;
		gbcBtnClearSearch.gridy = 7;
		contentPane.add(btnClearSearch, gbcBtnClearSearch);

		JLabel lblSearchWorker = new JLabel("Search Order");
		lblSearchWorker.setIconTextGap(8);
		lblSearchWorker.setFont(new Font(ARIAL, Font.BOLD, 14));
		GridBagConstraints gbcLblSearchWorker = new GridBagConstraints();
		gbcLblSearchWorker.anchor = GridBagConstraints.EAST;
		gbcLblSearchWorker.insets = new Insets(0, 0, 5, 5);
		gbcLblSearchWorker.gridx = 0;
		gbcLblSearchWorker.gridy = 8;
		contentPane.add(lblSearchWorker, gbcLblSearchWorker);

		txtSearchOrder.setName("txtSearchOrder");
		txtSearchOrder.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleSearchAndClearButtonStates();
			}
		});
		txtSearchOrder.setFont(new Font(ARIAL, Font.PLAIN, 12));
		txtSearchOrder.setColumns(10);
		GridBagConstraints gbcTxtSearchOrder = new GridBagConstraints();
		gbcTxtSearchOrder.ipady = 10;
		gbcTxtSearchOrder.ipadx = 20;
		gbcTxtSearchOrder.insets = new Insets(0, 0, 5, 5);
		gbcTxtSearchOrder.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtSearchOrder.gridx = 1;
		gbcTxtSearchOrder.gridy = 8;
		contentPane.add(txtSearchOrder, gbcTxtSearchOrder);

		JLabel lblSearchBy = new JLabel("Search By");
		lblSearchBy.setIconTextGap(8);
		lblSearchBy.setFont(new Font(ARIAL, Font.BOLD, 14));
		GridBagConstraints gbcLblSearchBy = new GridBagConstraints();
		gbcLblSearchBy.anchor = GridBagConstraints.EAST;
		gbcLblSearchBy.insets = new Insets(0, 0, 5, 5);
		gbcLblSearchBy.gridx = 2;
		gbcLblSearchBy.gridy = 8;
		contentPane.add(lblSearchBy, gbcLblSearchBy);

		cmbSearchBy.setName("cmbSearchBy");
		cmbSearchBy.addActionListener(e -> handleSearchAndClearButtonStates());
		GridBagConstraints gbcCmbSearchBy = new GridBagConstraints();
		gbcCmbSearchBy.ipady = 10;
		gbcCmbSearchBy.ipadx = 20;
		gbcCmbSearchBy.insets = new Insets(0, 0, 5, 5);
		gbcCmbSearchBy.fill = GridBagConstraints.HORIZONTAL;
		gbcCmbSearchBy.gridx = 3;
		gbcCmbSearchBy.gridy = 8;
		contentPane.add(cmbSearchBy, gbcCmbSearchBy);

		btnSearchOrder = new JButton("Search");
		btnSearchOrder.setEnabled(false);
		btnSearchOrder.setName("btnSearchOrder");
		btnSearchOrder.setForeground(Color.WHITE);
		btnSearchOrder.setOpaque(true);
		btnSearchOrder.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnSearchOrder.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnSearchOrder.setBackground(new Color(59, 89, 182));
		btnSearchOrder.setFocusPainted(false);
		btnSearchOrder.setPreferredSize(new Dimension(150, 40));
		btnSearchOrder.addActionListener(e -> searchOrderByTextMethod());
		GridBagConstraints gbcBtnSearchOrder = new GridBagConstraints();
		gbcBtnSearchOrder.ipady = 10;
		gbcBtnSearchOrder.ipadx = 20;
		gbcBtnSearchOrder.insets = new Insets(0, 0, 5, 0);
		gbcBtnSearchOrder.gridx = 5;
		gbcBtnSearchOrder.gridy = 8;
		contentPane.add(btnSearchOrder, gbcBtnSearchOrder);

		showSearchErrorLbl = new JLabel("");
		showSearchErrorLbl.setName("showSearchErrorLbl");
		showSearchErrorLbl.setForeground(new Color(139, 0, 0));
		showSearchErrorLbl.setFont(new Font(ARIAL, Font.PLAIN, 16));
		showSearchErrorLbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbcShowSearchErrorLbl = new GridBagConstraints();
		gbcShowSearchErrorLbl.gridwidth = 4;
		gbcShowSearchErrorLbl.insets = new Insets(0, 0, 5, 5);
		gbcShowSearchErrorLbl.gridx = 0;
		gbcShowSearchErrorLbl.gridy = 9;
		contentPane.add(showSearchErrorLbl, gbcShowSearchErrorLbl);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.ipady = 10;
		gbcScrollPane.ipadx = 10;
		gbcScrollPane.gridwidth = 6;
		gbcScrollPane.gridheight = 5;
		gbcScrollPane.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 10;
		contentPane.add(scrollPane, gbcScrollPane);

		orderListModel = new DefaultListModel<>();
		listOrders = new JList<>(orderListModel);
		listOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listOrders.setName("listOrders");
		scrollPane.setViewportView(listOrders);

		btnDelete = new JButton("Delete");
		btnDelete.setName("btnDelete");
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(e -> deleteOrderMethod());
		listOrders.addListSelectionListener(e -> btnDelete.setEnabled(listOrders.getSelectedIndex() != -1));

		btnDelete.setForeground(Color.WHITE);
		btnDelete.setOpaque(true);
		btnDelete.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnDelete.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnDelete.setBackground(new Color(59, 89, 182));
		btnDelete.setFocusPainted(false);
		btnDelete.setPreferredSize(new Dimension(150, 40));
		GridBagConstraints gbcBtnDelete = new GridBagConstraints();
		gbcBtnDelete.ipady = 10;
		gbcBtnDelete.ipadx = 20;
		gbcBtnDelete.insets = new Insets(0, 0, 5, 5);
		gbcBtnDelete.gridx = 1;
		gbcBtnDelete.gridy = 15;
		contentPane.add(btnDelete, gbcBtnDelete);

		showErrorNotFoundLbl = new JLabel("");
		showErrorNotFoundLbl.setName("showErrorNotFoundLbl");
		showErrorNotFoundLbl.setForeground(new Color(139, 0, 0));
		showErrorNotFoundLbl.setFont(new Font(ARIAL, Font.PLAIN, 16));
		showErrorNotFoundLbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbcShowErrorNotFoundLbl = new GridBagConstraints();
		gbcShowErrorNotFoundLbl.insets = new Insets(0, 0, 0, 5);
		gbcShowErrorNotFoundLbl.gridx = 0;
		gbcShowErrorNotFoundLbl.gridy = 16;
		contentPane.add(showErrorNotFoundLbl, gbcShowErrorNotFoundLbl);

	}

	@Override
	public void showAllOrder(List<CustomerOrder> order) {
		resetAllSearchStates();
		orderListModel.clear();
		order.stream().forEach(orderListModel::addElement);
		resetErrorLabelAndClearComboBoxSelection();
	}

	@Override
	public void showAllWorkers(List<Worker> worker) {
		worker.stream().forEach(workerListModel::addElement);
		cmbWorker.setSelectedItem(null);
	}

	@Override
	public void orderAdded(CustomerOrder order) {
		SwingUtilities.invokeLater(() -> {
			orderListModel.addElement(order);
			resetErrorLabelAndClearComboBoxSelection();
		});

	}

	@Override
	public void orderModified(CustomerOrder order) {
		for (int i = 0; i < orderListModel.getSize(); i++) {
			if (orderListModel.getElementAt(i).getOrderId().equals(order.getOrderId())) {
				orderListModel.removeElementAt(i);
				orderListModel.addElement(order);
			}
		}
		resetErrorLabelAndClearComboBoxSelection();

	}

	@Override
	public void showFetchedOrder(CustomerOrder order) {
		txtOrderId.setText(order.getOrderId().toString());
		txtCustomerName.setText(order.getCustomerName());
		txtCustomerAddress.setText(order.getCustomerAddress());
		txtCustomerPhone.setText(order.getCustomerPhoneNumber());
		txtOrderDescription.setText(order.getOrderDescription());
		txtSelectedDate.setText(order.getAppointmentDate());
		cmbOrderCategory.setSelectedItem(order.getOrderCategory());
		cmbOrderStatus.setSelectedItem(order.getOrderStatus());
		cmbWorker.setSelectedItem(order.getWorker());
		resetErrorLabels();
	}

	@Override
	public void showSearchResultForOrder(List<CustomerOrder> order) {
		orderListModel.removeAllElements();
		order.stream().forEach(orderListModel::addElement);

	}

	@Override
	public void orderRemoved(CustomerOrder order) {
		orderListModel.removeElement(order);
		resetErrorLabelAndClearComboBoxSelection();
	}

	@Override
	public void showError(String message, CustomerOrder order) {
		showError.setText(message + ": " + order);

	}

	@Override
	public void showErrorNotFound(String message, CustomerOrder order) {
		showErrorNotFoundLbl.setText(message + ": " + order);

	}

	@Override
	public void showSearchError(String message, String searchText) {
		showSearchErrorLbl.setText(message + ": " + searchText);

	}

	@Override
	public void resetAllFields() {
		txtOrderId.setText("\b");
		txtCustomerName.setText("");
		txtCustomerAddress.setText("");
		txtCustomerPhone.setText("");
		txtOrderDescription.setText("");
		txtSelectedDate.setText("");
		txtSearchOrder.setText("");
		cmbSearchBy.setSelectedItem(null);
		cmbOrderCategory.setSelectedItem(null);
		cmbOrderStatus.setSelectedItem(null);
		cmbWorker.setSelectedItem(null);

		orderController.allOrders();

	}

	/**
	 * Reset all search states.
	 */
	private void resetAllSearchStates() {
		txtSearchOrder.setText(" ");
		cmbSearchBy.setSelectedItem(null);
	}

	/**
	 * Reset error label and clear combo box selection.
	 */
	private void resetErrorLabelAndClearComboBoxSelection() {
		cmbWorker.setSelectedItem(null);
		cmbOrderStatus.setSelectedItem(null);
		cmbOrderCategory.setSelectedItem(null);
		resetErrorLabels();
	}

	/**
	 * Reset error labels.
	 */
	private void resetErrorLabels() {
		showError.setText(" ");
		showErrorNotFoundLbl.setText(" ");
		showSearchErrorLbl.setText(" ");
	}

	/**
	 * Delete order method.
	 */
	private void deleteOrderMethod() {
		orderController.deleteOrder(listOrders.getSelectedValue());
	}

	/**
	 * Search order by text method.
	 */
	private void searchOrderByTextMethod() {
		String searchText = txtSearchOrder.getText();
		OrderSearchOptions searchOption = (OrderSearchOptions) cmbSearchBy.getSelectedItem();
		orderController.searchOrder(searchText, searchOption);
	}

	/**
	 * Fetch order method.
	 */
	private void fetchOrderMethod() {
		CustomerOrder order = new CustomerOrder();
		Long id = Long.parseLong(txtOrderId.getText());

		order.setOrderId(id);
		orderController.fetchOrderById(order);
	}

	/**
	 * Update order method.
	 */
	private void updateOrderMethod() {
		CustomerOrder order = new CustomerOrder();
		Long id = Long.parseLong(txtOrderId.getText());

		order.setOrderId(id);
		order.setCustomerName(txtCustomerName.getText());
		order.setCustomerAddress(txtCustomerAddress.getText());
		order.setCustomerPhoneNumber(txtCustomerPhone.getText());
		order.setOrderDescription(txtOrderDescription.getText());
		order.setAppointmentDate(txtSelectedDate.getText());
		order.setOrderCategory((OrderCategory) cmbOrderCategory.getSelectedItem());
		order.setOrderStatus((OrderStatus) cmbOrderStatus.getSelectedItem());
		order.setWorker((Worker) cmbWorker.getSelectedItem());
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);
	}

	/**
	 * Adds the order method.
	 */
	private void addOrderMethod() {
		new Thread(() -> {
			CustomerOrder order = new CustomerOrder();
			order.setCustomerName(txtCustomerName.getText());
			order.setCustomerAddress(txtCustomerAddress.getText());
			order.setCustomerPhoneNumber(txtCustomerPhone.getText());
			order.setOrderDescription(txtOrderDescription.getText());
			order.setAppointmentDate(txtSelectedDate.getText());
			order.setOrderCategory((OrderCategory) cmbOrderCategory.getSelectedItem());
			order.setOrderStatus((OrderStatus) cmbOrderStatus.getSelectedItem());
			order.setWorker((Worker) cmbWorker.getSelectedItem());
			orderController.createOrUpdateOrder(order, OperationType.ADD);
		}).start();

	}

	/**
	 * Check character is number.
	 *
	 * @param event the event
	 */
	private void checkCharacterIsNumber(KeyEvent event) {
		char character = event.getKeyChar();
		if (!((character >= '0') && (character <= '9') || (character == KeyEvent.VK_BACK_SPACE))) {
			getToolkit().beep();
			event.consume();
		}
	}

	/**
	 * Open worker form.
	 */
	private void openWorkerForm() {
		workerSwingView.setVisible(true);
	}

	/**
	 * Handle button and combo box states.
	 */
	protected void handleButtonAndComboBoxStates() {
		boolean isOrderIdEmpty = txtOrderId.getText().trim().isEmpty();
		boolean isCustomerNameEmpty = txtCustomerName.getText().trim().isEmpty();
		boolean isCustomerAddressEmpty = txtCustomerAddress.getText().trim().isEmpty();
		boolean isCustomerPhoneNumberEmpty = txtCustomerPhone.getText().trim().isEmpty();
		boolean isOrderDescriptionEmpty = txtOrderDescription.getText().trim().isEmpty();
		boolean isAppointmentDateEmpty = txtSelectedDate.getText().trim().isEmpty();
		boolean isOrderCategoryEmpty = cmbOrderCategory.getSelectedItem() == null;
		boolean isOrderStatusEmpty = cmbOrderStatus.getSelectedItem() == null;
		boolean isAssignedWorkerEmpty = cmbWorker.getSelectedItem() == null;
		btnAdd.setEnabled(isOrderIdEmpty && !isCustomerNameEmpty && !isCustomerAddressEmpty
				&& !isCustomerPhoneNumberEmpty && !isOrderDescriptionEmpty && !isAppointmentDateEmpty
				&& !isOrderCategoryEmpty && !isOrderStatusEmpty && !isAssignedWorkerEmpty);

		btnUpdate.setEnabled(!isOrderIdEmpty && !isCustomerNameEmpty && !isCustomerAddressEmpty
				&& !isCustomerPhoneNumberEmpty && !isOrderDescriptionEmpty && !isAppointmentDateEmpty
				&& !isOrderCategoryEmpty && !isOrderStatusEmpty && !isAssignedWorkerEmpty);

		btnFetch.setEnabled(!isOrderIdEmpty && isCustomerNameEmpty && isCustomerAddressEmpty
				&& isCustomerPhoneNumberEmpty && isOrderDescriptionEmpty && isAppointmentDateEmpty
				&& isOrderCategoryEmpty && isOrderStatusEmpty && isAssignedWorkerEmpty);

	}

	/**
	 * Handle search and clear button states.
	 */
	private void handleSearchAndClearButtonStates() {
		boolean isSearchOrderTextEmpty = txtSearchOrder.getText().trim().isEmpty();
		boolean isSearchOptionEmpty = cmbSearchBy.getSelectedItem() == null;
		btnSearchOrder.setEnabled(!isSearchOrderTextEmpty && !isSearchOptionEmpty);
		btnClearSearch.setEnabled(!isSearchOrderTextEmpty && !isSearchOptionEmpty);
	}

}
