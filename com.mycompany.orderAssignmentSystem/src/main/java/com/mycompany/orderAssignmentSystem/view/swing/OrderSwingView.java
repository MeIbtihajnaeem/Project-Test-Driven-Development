package com.mycompany.orderAssignmentSystem.view.swing;

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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.mycompany.orderAssignmentSystem.controller.OrderController;
import com.mycompany.orderAssignmentSystem.enumerations.OperationType;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderSearchOptions;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.view.OrderView;

public class OrderSwingView extends JFrame implements OrderView {

	private static final long serialVersionUID = 2L;
	private JPanel contentPane;
	private JTextField txtOrderId;
	private JTextField txtCustomerName;
	private JTextField txtCustomerAddress;
	private JTextField txtCustomerPhone;
	private JTextField txtOrderDescription;
	private JTextField txtSearchOrder;

	private JButton btnFetch;
	private JButton btnAdd;
	private JButton btnUpdate;
	private JButton btnClearSearch;
	private JButton btnSearchOrder;
	private JButton btnDelete;

	private JLabel showErrorNotFoundLbl;
	private JTextField txtSelectedDate;
	private JLabel showError;
	private JLabel showSearchErrorLbl;
	private OrderController orderController;
	private WorkerSwingView workerSwingView;

	// List of combo box

	private JComboBox<OrderCategory> cmbOrderCategory;
	private JComboBox<OrderSearchOptions> cmbSearchBy;
	private JComboBox<OrderStatus> cmbOrderStatus;

	// List of orders and worker
	private DefaultComboBoxModel<Worker> workerListModel;
	private DefaultListModel<CustomerOrder> orderListModel;
	private JList<CustomerOrder> listOrders;
	private JComboBox<Worker> cmbWorker;
//	private ValidationConfigurations validationConfig;

	public void setOrderController(OrderController orderController) {
		this.orderController = orderController;
	}

	public DefaultComboBoxModel<Worker> getWorkerListModel() {
		return workerListModel;
	}

	public DefaultListModel<CustomerOrder> getOrderListModel() {
		return orderListModel;
	}

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					OrderSwingView frame = new OrderSwingView();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	public void setWorkerSwingView(WorkerSwingView workerSwingView) {
		this.workerSwingView = workerSwingView;
	}

	/**
	 * Create the frame.
	 */
	public OrderSwingView() {
//		validationConfig = new ExtendedValidationConfigurations();
		cmbOrderCategory = new JComboBox<OrderCategory>();
		cmbSearchBy = new JComboBox<OrderSearchOptions>();
		cmbOrderStatus = new JComboBox<OrderStatus>();
		workerListModel = new DefaultComboBoxModel<>();
		cmbWorker = new JComboBox<Worker>(workerListModel);
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
//		getWorkerListModel().addElement(new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER));

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

//		KeyListener changeTextBoxValueListener = new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//
//				if ( e.getSource() == txtCustomerName
//						|| e.getSource() == txtCustomerAddress || e.getSource() == txtCustomerPhone
//						|| e.getSource() == txtOrderDescription || e.getSource() == txtSelectedDate) {
//					handleButtonAndComboBoxStates();
//
//				} else if (e.getSource() == txtSearchOrder) {
//
//					handleSearchAndClearButtonStates();
//
//				}
//			}
//
//		};

//		ActionListener changeComboBoxValueListener = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (e.getSource() == cmbOrderCategory || e.getSource() == cmbOrderStatus
//						|| e.getSource() == cmbWorker) {
//					handleButtonAndComboBoxStates();
//
//				} else if (e.getSource() == cmbSearchBy) {
//					handleSearchAndClearButtonStates();
//
//				}
//			}
//		};

//		ActionListener crudActionListener = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//				if (e.getSource() == btnAdd) {
//					addOrderMethod();
//				} else if (e.getSource() == btnUpdate) {
//					updateOrderMethod();
//				} else if (e.getSource() == btnFetch) {
//					fetchOrderMethod();
//				} else if (e.getSource() == btnSearchOrder) {
//					searchOrderByTextMethod();
//				} else if (e.getSource() == btnClearSearch) {
//					clearSearchAndFetchOrders();
//				} else if (e.getSource() == btnDelete) {
//					deleteOrderMethod();
//				}
//
////				else {
////					worker.setWorkerId(Long.parseLong(txtOrdersByWorkerId.getText()));
////					workerController.fetchOrdersByWorkerId(worker);
////				}
//
//			}
//
//		
//		};

		setTitle("Order Form");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 742, 629);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("ColorChooser.background"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 0.0, 1.0, 0.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0,
				0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JButton btnManageWorker = new JButton("Manage Worker");
		btnManageWorker.setName("btnManageWorker");
		btnManageWorker.setForeground(Color.WHITE);
		btnManageWorker.setOpaque(true);
		btnManageWorker.setFont(new Font("Arial", Font.BOLD, 16));
		btnManageWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnManageWorker.setBackground(Color.RED);
		btnManageWorker.setFocusPainted(false);
		btnManageWorker.setPreferredSize(new Dimension(150, 40));
		btnManageWorker.addActionListener(e -> openWorkerForm());

		GridBagConstraints gbc_btnManageWorker = new GridBagConstraints();
		gbc_btnManageWorker.ipady = 10;
		gbc_btnManageWorker.ipadx = 20;
		gbc_btnManageWorker.insets = new Insets(0, 0, 5, 0);
		gbc_btnManageWorker.gridx = 5;
		gbc_btnManageWorker.gridy = 0;
		contentPane.add(btnManageWorker, gbc_btnManageWorker);

		JLabel lblWorkerId = new JLabel("Order ID");
		lblWorkerId.setIconTextGap(8);
		lblWorkerId.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblWorkerId = new GridBagConstraints();
		gbc_lblWorkerId.anchor = GridBagConstraints.EAST;
		gbc_lblWorkerId.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkerId.gridx = 0;
		gbc_lblWorkerId.gridy = 1;
		contentPane.add(lblWorkerId, gbc_lblWorkerId);

		txtOrderId.setName("txtOrderId");
		txtOrderId.setFont(new Font("Arial", Font.PLAIN, 12));
		txtOrderId.setColumns(10);
//		txtOrderId.addKeyListener(new KeyAdapter()->handleButtonAndComboBoxStates());;
		txtOrderId.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}

			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE))) {
					getToolkit().beep();
					e.consume();
				}
			}
		});

		GridBagConstraints gbc_txtOrderId = new GridBagConstraints();
		gbc_txtOrderId.ipady = 10;
		gbc_txtOrderId.ipadx = 10;
		gbc_txtOrderId.insets = new Insets(0, 0, 5, 5);
		gbc_txtOrderId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtOrderId.gridx = 1;
		gbc_txtOrderId.gridy = 1;
		contentPane.add(txtOrderId, gbc_txtOrderId);

		JLabel lblAppointmentDate = new JLabel("Appointment Date");
		lblAppointmentDate.setIconTextGap(8);
		lblAppointmentDate.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblAppointmentDate = new GridBagConstraints();
		gbc_lblAppointmentDate.anchor = GridBagConstraints.EAST;
		gbc_lblAppointmentDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblAppointmentDate.gridx = 2;
		gbc_lblAppointmentDate.gridy = 1;
		contentPane.add(lblAppointmentDate, gbc_lblAppointmentDate);

		txtSelectedDate.setName("txtSelectedDate");
		txtSelectedDate.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}
		});

		txtSelectedDate.setFont(new Font("Arial", Font.PLAIN, 12));
		txtSelectedDate.setColumns(10);
		GridBagConstraints gbc_lblSelectedDate = new GridBagConstraints();
		gbc_lblSelectedDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblSelectedDate.ipady = 10;
		gbc_lblSelectedDate.ipadx = 10;
		gbc_lblSelectedDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectedDate.gridx = 3;
		gbc_lblSelectedDate.gridy = 1;
		contentPane.add(txtSelectedDate, gbc_lblSelectedDate);

		JLabel lblCustomerName = new JLabel("Customer Name");
		lblCustomerName.setIconTextGap(8);
		lblCustomerName.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblCustomerName = new GridBagConstraints();
		gbc_lblCustomerName.anchor = GridBagConstraints.EAST;
		gbc_lblCustomerName.insets = new Insets(0, 0, 5, 5);
		gbc_lblCustomerName.gridx = 0;
		gbc_lblCustomerName.gridy = 2;
		contentPane.add(lblCustomerName, gbc_lblCustomerName);

		txtCustomerName.setName("txtCustomerName");
		txtCustomerName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}
		});

		txtCustomerName.setFont(new Font("Arial", Font.PLAIN, 12));
		txtCustomerName.setColumns(10);
		GridBagConstraints gbc_txtCustomerName = new GridBagConstraints();
		gbc_txtCustomerName.ipady = 10;
		gbc_txtCustomerName.ipadx = 10;
		gbc_txtCustomerName.insets = new Insets(0, 0, 5, 5);
		gbc_txtCustomerName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCustomerName.gridx = 1;
		gbc_txtCustomerName.gridy = 2;
		contentPane.add(txtCustomerName, gbc_txtCustomerName);

		JLabel lblOrderCategory = new JLabel("Order Category");
		lblOrderCategory.setIconTextGap(8);
		lblOrderCategory.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblOrderCategory = new GridBagConstraints();
		gbc_lblOrderCategory.anchor = GridBagConstraints.EAST;
		gbc_lblOrderCategory.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderCategory.gridx = 2;
		gbc_lblOrderCategory.gridy = 2;
		contentPane.add(lblOrderCategory, gbc_lblOrderCategory);

		cmbOrderCategory.setName("cmbOrderCategory");
		cmbOrderCategory.addActionListener(e -> handleButtonAndComboBoxStates());

		GridBagConstraints gbc_cmbOrderCategory = new GridBagConstraints();
		gbc_cmbOrderCategory.ipady = 10;
		gbc_cmbOrderCategory.ipadx = 20;
		gbc_cmbOrderCategory.insets = new Insets(0, 0, 5, 5);
		gbc_cmbOrderCategory.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbOrderCategory.gridx = 3;
		gbc_cmbOrderCategory.gridy = 2;
		contentPane.add(cmbOrderCategory, gbc_cmbOrderCategory);

		JLabel lblWorkerPhoneNumber = new JLabel("Customer Address");
		lblWorkerPhoneNumber.setIconTextGap(8);
		lblWorkerPhoneNumber.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblWorkerPhoneNumber = new GridBagConstraints();
		gbc_lblWorkerPhoneNumber.anchor = GridBagConstraints.EAST;
		gbc_lblWorkerPhoneNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkerPhoneNumber.gridx = 0;
		gbc_lblWorkerPhoneNumber.gridy = 3;
		contentPane.add(lblWorkerPhoneNumber, gbc_lblWorkerPhoneNumber);

		txtCustomerAddress.setName("txtCustomerAddress");
		txtCustomerAddress.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}
		});

		txtCustomerAddress.setFont(new Font("Arial", Font.PLAIN, 12));
		txtCustomerAddress.setColumns(10);
		GridBagConstraints gbc_txtCustomerAddress = new GridBagConstraints();
		gbc_txtCustomerAddress.ipady = 10;
		gbc_txtCustomerAddress.ipadx = 10;
		gbc_txtCustomerAddress.insets = new Insets(0, 0, 5, 5);
		gbc_txtCustomerAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCustomerAddress.gridx = 1;
		gbc_txtCustomerAddress.gridy = 3;
		contentPane.add(txtCustomerAddress, gbc_txtCustomerAddress);

		JLabel lblOrderStatus = new JLabel("Order Status");
		lblOrderStatus.setIconTextGap(8);
		lblOrderStatus.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblOrderStatus = new GridBagConstraints();
		gbc_lblOrderStatus.anchor = GridBagConstraints.EAST;
		gbc_lblOrderStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderStatus.gridx = 2;
		gbc_lblOrderStatus.gridy = 3;
		contentPane.add(lblOrderStatus, gbc_lblOrderStatus);

		cmbOrderStatus.setName("cmbOrderStatus");
		cmbOrderStatus.addActionListener(e -> handleButtonAndComboBoxStates());

		GridBagConstraints gbc_cmbOrderStatus = new GridBagConstraints();
		gbc_cmbOrderStatus.ipady = 10;
		gbc_cmbOrderStatus.ipadx = 20;
		gbc_cmbOrderStatus.insets = new Insets(0, 0, 5, 5);
		gbc_cmbOrderStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbOrderStatus.gridx = 3;
		gbc_cmbOrderStatus.gridy = 3;
		contentPane.add(cmbOrderStatus, gbc_cmbOrderStatus);

		JLabel lblCustomerPhone = new JLabel("Customer Phone #");
		lblCustomerPhone.setIconTextGap(8);
		lblCustomerPhone.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblCustomerPhone = new GridBagConstraints();
		gbc_lblCustomerPhone.anchor = GridBagConstraints.EAST;
		gbc_lblCustomerPhone.insets = new Insets(0, 0, 5, 5);
		gbc_lblCustomerPhone.gridx = 0;
		gbc_lblCustomerPhone.gridy = 4;
		contentPane.add(lblCustomerPhone, gbc_lblCustomerPhone);

		txtCustomerPhone.setName("txtCustomerPhone");
		txtCustomerPhone.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}
		});

		txtCustomerPhone.setFont(new Font("Arial", Font.PLAIN, 12));
		txtCustomerPhone.setColumns(10);
		GridBagConstraints gbc_txtCustomerPhone = new GridBagConstraints();
		gbc_txtCustomerPhone.ipady = 10;
		gbc_txtCustomerPhone.ipadx = 10;
		gbc_txtCustomerPhone.insets = new Insets(0, 0, 5, 5);
		gbc_txtCustomerPhone.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCustomerPhone.gridx = 1;
		gbc_txtCustomerPhone.gridy = 4;
		contentPane.add(txtCustomerPhone, gbc_txtCustomerPhone);

		JLabel lblWorkerCategory = new JLabel("Worker");
		lblWorkerCategory.setIconTextGap(8);
		lblWorkerCategory.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblWorkerCategory = new GridBagConstraints();
		gbc_lblWorkerCategory.anchor = GridBagConstraints.EAST;
		gbc_lblWorkerCategory.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkerCategory.gridx = 2;
		gbc_lblWorkerCategory.gridy = 4;
		contentPane.add(lblWorkerCategory, gbc_lblWorkerCategory);

		cmbWorker.setName("cmbWorker");
		cmbWorker.addActionListener(e -> handleButtonAndComboBoxStates());
		GridBagConstraints gbc_cmbWorker = new GridBagConstraints();
		gbc_cmbWorker.ipady = 10;
		gbc_cmbWorker.ipadx = 20;
		gbc_cmbWorker.insets = new Insets(0, 0, 5, 5);
		gbc_cmbWorker.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbWorker.gridx = 3;
		gbc_cmbWorker.gridy = 4;
		contentPane.add(cmbWorker, gbc_cmbWorker);

		JLabel lblOrderDescription = new JLabel("Order Description");
		lblOrderDescription.setIconTextGap(8);
		lblOrderDescription.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblOrderDescription = new GridBagConstraints();
		gbc_lblOrderDescription.anchor = GridBagConstraints.EAST;
		gbc_lblOrderDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderDescription.gridx = 0;
		gbc_lblOrderDescription.gridy = 5;
		contentPane.add(lblOrderDescription, gbc_lblOrderDescription);

		txtOrderDescription.setName("txtOrderDescription");
		txtOrderDescription.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}
		});
		txtOrderDescription.setFont(new Font("Arial", Font.PLAIN, 12));
		txtOrderDescription.setColumns(10);
		GridBagConstraints gbc_txtOrderDescription = new GridBagConstraints();
		gbc_txtOrderDescription.ipady = 10;
		gbc_txtOrderDescription.ipadx = 10;
		gbc_txtOrderDescription.insets = new Insets(0, 0, 5, 5);
		gbc_txtOrderDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtOrderDescription.gridx = 1;
		gbc_txtOrderDescription.gridy = 5;
		contentPane.add(txtOrderDescription, gbc_txtOrderDescription);

		showError = new JLabel("");
		showError.setName("showError");
		showError.setForeground(new Color(139, 0, 0));
		showError.setFont(new Font("Verdana", Font.PLAIN, 16));
		showError.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbc_showError = new GridBagConstraints();
		gbc_showError.gridwidth = 6;
		gbc_showError.insets = new Insets(0, 0, 5, 0);
		gbc_showError.gridx = 0;
		gbc_showError.gridy = 6;
		contentPane.add(showError, gbc_showError);

		btnFetch.setEnabled(false);
		btnFetch.setName("btnFetch");
		btnFetch.setForeground(Color.WHITE);
		btnFetch.setOpaque(true);
		btnFetch.setFont(new Font("Arial", Font.BOLD, 16));
		btnFetch.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnFetch.setBackground(new Color(59, 89, 182));
		btnFetch.setFocusPainted(false);
		btnFetch.setPreferredSize(new Dimension(150, 40));
		btnFetch.addActionListener(e -> fetchOrderMethod());

		GridBagConstraints gbc_btnFetch = new GridBagConstraints();
		gbc_btnFetch.ipady = 10;
		gbc_btnFetch.ipadx = 20;
		gbc_btnFetch.gridy = 7;
		gbc_btnFetch.insets = new Insets(0, 0, 5, 5);
		gbc_btnFetch.gridx = 0;
		contentPane.add(btnFetch, gbc_btnFetch);

		btnAdd.setEnabled(false);
		btnAdd.setName("btnAdd");
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setOpaque(true);
		btnAdd.setFont(new Font("Arial", Font.BOLD, 16));
		btnAdd.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnAdd.setBackground(new Color(59, 89, 182));
		btnAdd.setFocusPainted(false);
		btnAdd.setPreferredSize(new Dimension(150, 40));
		btnAdd.addActionListener(e -> addOrderMethod());

		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.ipady = 10;
		gbc_btnAdd.ipadx = 20;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 7;
		contentPane.add(btnAdd, gbc_btnAdd);

		btnUpdate.setEnabled(false);
		btnUpdate.setName("btnUpdate");
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setOpaque(true);
		btnUpdate.setFont(new Font("Arial", Font.BOLD, 16));
		btnUpdate.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnUpdate.setBackground(new Color(59, 89, 182));
		btnUpdate.setFocusPainted(false);
		btnUpdate.setPreferredSize(new Dimension(150, 40));
		btnUpdate.addActionListener(e -> updateOrderMethod());

		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.ipady = 10;
		gbc_btnUpdate.ipadx = 20;
		gbc_btnUpdate.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpdate.gridx = 2;
		gbc_btnUpdate.gridy = 7;
		contentPane.add(btnUpdate, gbc_btnUpdate);

		btnClearSearch = new JButton("Clear");
		btnClearSearch.setEnabled(false);
		btnClearSearch.setName("btnClearSearch");
		btnClearSearch.setForeground(Color.WHITE);
		btnClearSearch.setOpaque(true);
		btnClearSearch.setFont(new Font("Arial", Font.BOLD, 16));
		btnClearSearch.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnClearSearch.setBackground(new Color(59, 89, 182));
		btnClearSearch.setFocusPainted(false);
		btnClearSearch.setPreferredSize(new Dimension(150, 40));
		btnClearSearch.addActionListener(e -> resetAllFields());
		GridBagConstraints gbc_btnClearSearch = new GridBagConstraints();
		gbc_btnClearSearch.ipady = 10;
		gbc_btnClearSearch.ipadx = 20;
		gbc_btnClearSearch.insets = new Insets(0, 0, 5, 5);
		gbc_btnClearSearch.gridx = 3;
		gbc_btnClearSearch.gridy = 7;
		contentPane.add(btnClearSearch, gbc_btnClearSearch);

		JLabel lblSearchWorker = new JLabel("Search Order");
		lblSearchWorker.setIconTextGap(8);
		lblSearchWorker.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblSearchWorker = new GridBagConstraints();
		gbc_lblSearchWorker.anchor = GridBagConstraints.EAST;
		gbc_lblSearchWorker.insets = new Insets(0, 0, 5, 5);
		gbc_lblSearchWorker.gridx = 0;
		gbc_lblSearchWorker.gridy = 8;
		contentPane.add(lblSearchWorker, gbc_lblSearchWorker);

		txtSearchOrder.setName("txtSearchOrder");
		txtSearchOrder.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleSearchAndClearButtonStates();
			}
		});
		txtSearchOrder.setFont(new Font("Arial", Font.PLAIN, 12));
		txtSearchOrder.setColumns(10);
		GridBagConstraints gbc_txtSearchOrder = new GridBagConstraints();
		gbc_txtSearchOrder.ipady = 10;
		gbc_txtSearchOrder.ipadx = 20;
		gbc_txtSearchOrder.insets = new Insets(0, 0, 5, 5);
		gbc_txtSearchOrder.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSearchOrder.gridx = 1;
		gbc_txtSearchOrder.gridy = 8;
		contentPane.add(txtSearchOrder, gbc_txtSearchOrder);

		JLabel lblSearchBy = new JLabel("Search By");
		lblSearchBy.setIconTextGap(8);
		lblSearchBy.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblSearchBy = new GridBagConstraints();
		gbc_lblSearchBy.anchor = GridBagConstraints.EAST;
		gbc_lblSearchBy.insets = new Insets(0, 0, 5, 5);
		gbc_lblSearchBy.gridx = 2;
		gbc_lblSearchBy.gridy = 8;
		contentPane.add(lblSearchBy, gbc_lblSearchBy);

		cmbSearchBy.setName("cmbSearchBy");
		cmbSearchBy.addActionListener(e -> handleSearchAndClearButtonStates());
		GridBagConstraints gbc_cmbSearchBy = new GridBagConstraints();
		gbc_cmbSearchBy.ipady = 10;
		gbc_cmbSearchBy.ipadx = 20;
		gbc_cmbSearchBy.insets = new Insets(0, 0, 5, 5);
		gbc_cmbSearchBy.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbSearchBy.gridx = 3;
		gbc_cmbSearchBy.gridy = 8;
		contentPane.add(cmbSearchBy, gbc_cmbSearchBy);

		btnSearchOrder = new JButton("Search");
		btnSearchOrder.setEnabled(false);
		btnSearchOrder.setName("btnSearchOrder");
		btnSearchOrder.setForeground(Color.WHITE);
		btnSearchOrder.setOpaque(true);
		btnSearchOrder.setFont(new Font("Arial", Font.BOLD, 16));
		btnSearchOrder.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnSearchOrder.setBackground(new Color(59, 89, 182));
		btnSearchOrder.setFocusPainted(false);
		btnSearchOrder.setPreferredSize(new Dimension(150, 40));
		btnSearchOrder.addActionListener(e -> searchOrderByTextMethod());
		GridBagConstraints gbc_btnSearchOrder = new GridBagConstraints();
		gbc_btnSearchOrder.ipady = 10;
		gbc_btnSearchOrder.ipadx = 20;
		gbc_btnSearchOrder.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearchOrder.gridx = 5;
		gbc_btnSearchOrder.gridy = 8;
		contentPane.add(btnSearchOrder, gbc_btnSearchOrder);

		showSearchErrorLbl = new JLabel("");
		showSearchErrorLbl.setName("showSearchErrorLbl");
		showSearchErrorLbl.setForeground(new Color(139, 0, 0));
		showSearchErrorLbl.setFont(new Font("Verdana", Font.PLAIN, 16));
		showSearchErrorLbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbc_showSearchErrorLbl = new GridBagConstraints();
		gbc_showSearchErrorLbl.gridwidth = 4;
		gbc_showSearchErrorLbl.insets = new Insets(0, 0, 5, 5);
		gbc_showSearchErrorLbl.gridx = 0;
		gbc_showSearchErrorLbl.gridy = 9;
		contentPane.add(showSearchErrorLbl, gbc_showSearchErrorLbl);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.ipady = 10;
		gbc_scrollPane.ipadx = 10;
		gbc_scrollPane.gridwidth = 6;
		gbc_scrollPane.gridheight = 5;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 10;
		contentPane.add(scrollPane, gbc_scrollPane);

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
		btnDelete.setFont(new Font("Arial", Font.BOLD, 16));
		btnDelete.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnDelete.setBackground(new Color(59, 89, 182));
		btnDelete.setFocusPainted(false);
		btnDelete.setPreferredSize(new Dimension(150, 40));
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.ipady = 10;
		gbc_btnDelete.ipadx = 20;
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 1;
		gbc_btnDelete.gridy = 15;
		contentPane.add(btnDelete, gbc_btnDelete);

		showErrorNotFoundLbl = new JLabel("");
		showErrorNotFoundLbl.setName("showErrorNotFoundLbl");
		showErrorNotFoundLbl.setForeground(new Color(139, 0, 0));
		showErrorNotFoundLbl.setFont(new Font("Verdana", Font.PLAIN, 16));
		showErrorNotFoundLbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbc_showErrorNotFoundLbl = new GridBagConstraints();
		gbc_showErrorNotFoundLbl.insets = new Insets(0, 0, 0, 5);
		gbc_showErrorNotFoundLbl.gridx = 0;
		gbc_showErrorNotFoundLbl.gridy = 16;
		contentPane.add(showErrorNotFoundLbl, gbc_showErrorNotFoundLbl);

	}

	private void openWorkerForm() {
		workerSwingView.setVisible(true);
	}

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
//		if (btnFetch.isEnabled()) {
//			cmbWorker.setSelectedItem(null);
//			cmbOrderStatus.setSelectedItem(null);
//			cmbOrderCategory.setSelectedItem(null);
//		}

	}

	private void handleSearchAndClearButtonStates() {
		boolean isSearchOrderTextEmpty = txtSearchOrder.getText().trim().isEmpty();
		boolean isSearchOptionEmpty = cmbSearchBy.getSelectedItem() == null;
		btnSearchOrder.setEnabled(!isSearchOrderTextEmpty && !isSearchOptionEmpty);
		btnClearSearch.setEnabled(!isSearchOrderTextEmpty && !isSearchOptionEmpty);
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
		orderListModel.addElement(order);
		resetErrorLabelAndClearComboBoxSelection();
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
		txtSelectedDate.setText(order.getAppointmentDate().toString());
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
		// TODO Auto-generated method stub
		showError.setText(message + ": " + order);

	}

	@Override
	public void showErrorNotFound(String message, CustomerOrder order) {
		// TODO Auto-generated method stub
		showErrorNotFoundLbl.setText(message + ": " + order);

	}

	@Override
	public void showSearchError(String message, String searchText) {
		// TODO Auto-generated method stub
		showSearchErrorLbl.setText(message + ": " + searchText);

	}

	private void resetAllSearchStates() {
		txtSearchOrder.setText(" ");
		cmbSearchBy.setSelectedItem(null);
	}

	private void resetErrorLabelAndClearComboBoxSelection() {
		cmbWorker.setSelectedItem(null);
		cmbOrderStatus.setSelectedItem(null);
		cmbOrderCategory.setSelectedItem(null);
		resetErrorLabels();
	}

	private void resetErrorLabels() {
		showError.setText(" ");
		showErrorNotFoundLbl.setText(" ");
		showSearchErrorLbl.setText(" ");
	}

	private void deleteOrderMethod() {
		orderController.deleteOrder(listOrders.getSelectedValue());
	}

	private void searchOrderByTextMethod() {
		String searchText = txtSearchOrder.getText();
		OrderSearchOptions searchOption = (OrderSearchOptions) cmbSearchBy.getSelectedItem();
		orderController.searchOrder(searchText, searchOption);
	}

	private void fetchOrderMethod() {
		CustomerOrder order = new CustomerOrder();
		Long id = Long.parseLong(txtOrderId.getText());

		order.setOrderId(id);
		orderController.fetchOrderById(order);
	}

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

	private void addOrderMethod() {
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

}
