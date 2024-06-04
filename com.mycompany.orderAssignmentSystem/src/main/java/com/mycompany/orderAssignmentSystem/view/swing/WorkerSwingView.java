package com.mycompany.orderAssignmentSystem.view.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
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
import com.mycompany.orderAssignmentSystem.enumerations.OperationType;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.WorkerSearchOption;
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
	private JFormattedTextField txtWorkerId;

	/** The txt worker name. */
	private JTextField txtWorkerName;

	/** The txt worker phone. */
	private JTextField txtWorkerPhone;

	/** The txt search worker. */
	private JTextField txtSearchWorker;

	/** The txt orders by worker id. */
//	private JTextField txtOrdersByWorkerId;

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

	/** The cmb search by options. */
	private JComboBox<WorkerSearchOption> cmbSearchByOptions;

	/** The btn search worker. */
	private JButton btnSearchWorker;

	/** The btn clear search results and will reset to default state. */
	private JButton btnClearSearchWorker;

	/** The btn search order. */
//	private JButton btnSearchOrder;

	/** The show error while add, update or fetch has error. */
	private JLabel showErrorLbl;

	/** The show error while search worker has error. */
	private JLabel showErrorLblSearchWorker;

	/** The show error while search order has error. */
//	private JLabel showErrorLblSearchOrder;

	/** The show error while worker not found error. */
	private JLabel showErrorNotFoundLbl;
	private JScrollPane scrollPane;
	private JButton btnDelete;
//	private JScrollPane scrollPane_1;

	/** The worker list model. */
	private DefaultListModel<Worker> workerListModel;

	/** The order list model. */
//	private DefaultListModel<CustomerOrder> orderListModel;

	/** The list workers. */
	private JList<Worker> listWorkers;

	/** The list orders. */
//	private JList<CustomerOrder> listOrders;
//	private ValidationConfigurations validationConfig;

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
	public DefaultListModel<Worker> getWorkerListModel() {
		return workerListModel;
	}

	/**
	 * Gets the order list model.
	 *
	 * @return the order list model
	 */
//	public DefaultListModel<CustomerOrder> getOrderListModel() {
//		return orderListModel;
//	}

	/**
	 * Create the frame.
	 */
	public WorkerSwingView() {
		// Initialisation of combo box by enumeration
//		validationConfig = new ExtendedValidationConfigurations();

//		NumberFormat format = NumberFormat.getInstance();
//		NumberFormatter formatter = new NumberFormatter(format) {
//			private static final long serialVersionUID = 2L;
//
//			@Override
//			public Object stringToValue(String string) throws ParseException {
//				if (string == null || string.trim().isEmpty()) {
//					return null;
//				}
//				return super.stringToValue(string);
//			}
//
//			@Override
//			public String valueToString(Object value) throws ParseException {
//				if (value == null) {
//					return "";
//				}
//				return super.valueToString(value);
//			}
//		};
//		formatter.setValueClass(Integer.class);
//		formatter.setMaximum(Integer.MAX_VALUE);
//		formatter.setAllowsInvalid(false);
//		formatter.setCommitsOnValidEdit(true);
		txtWorkerPhone = new JTextField();
		txtWorkerName = new JTextField();

		txtSearchWorker = new JTextField();
//		txtOrdersByWorkerId = new JTextField();
		cmbWorkerCategory = new JComboBox<OrderCategory>();
		cmbSearchByOptions = new JComboBox<WorkerSearchOption>();
		workerListModel = new DefaultListModel<>();
//		orderListModel = new DefaultListModel<>();
		btnFetch = new JButton("Fetch");
		btnUpdate = new JButton("Update");
		btnAdd = new JButton("Add");
		btnSearchWorker = new JButton("Search Worker");
		btnClearSearchWorker = new JButton("Clear");
		btnDelete = new JButton("Delete");
//		btnSearchOrder = new JButton("Search Orders");
		for (OrderCategory category : OrderCategory.values()) {
			cmbWorkerCategory.addItem(category);
		}
		for (WorkerSearchOption workerSearchOption : WorkerSearchOption.values()) {
			cmbSearchByOptions.addItem(workerSearchOption);
		}
		cmbWorkerCategory.setSelectedItem(null);
		cmbSearchByOptions.setSelectedItem(null);

//		KeyListener changeWorkerValueListener = new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//
//				if (e.getSource() == txtSearchWorker) {
//					handleSearchWorkerAndClearButtonStates();
//
//				} else if (e.getSource() == txtOrdersByWorkerId) {
//
//					handleSearchOrderByWorkerIdButtonStates();
//				} else {
//					handleButtonAndComboBoxStates();
//
//				}
//			}
//
//			public void keyTyped(KeyEvent e) {
//				if (e.getSource() == txtWorkerId) {
//					char c = e.getKeyChar();
//					if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
//						getToolkit().beep();
//						e.consume();
//					}
//				}
//				if (e.getSource() == txtOrdersByWorkerId) {
//					char c = e.getKeyChar();
//					if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
//						getToolkit().beep();
//						e.consume();
//					}
//				}
//
//			}
//
//		};

//		ActionListener changeWorkerCategoryListener = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (e.getSource() == cmbSearchByOptions) {
//					handleSearchWorkerAndClearButtonStates();
//
//				} else {
//					handleButtonAndComboBoxStates();
//				}
//			}
//		};
//		ActionListener crudActionListener = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//				if (e.getSource() == btnAdd) {
//					addWorkerMethod();
//				} else if (e.getSource() == btnUpdate) {
//					updateWorkerMethod();
//				} else if (e.getSource() == btnFetch) {
//					fetchWorkerMethod();
//
//				} else {
//					getOrdersByWorkerIdMethod();
//				}
//
//			}
//
//		};
//		ActionListener filterActionListener = new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (e.getSource() == btnSearchWorker) {
//					String searchText = txtSearchWorker.getText();
//					WorkerSearchOption searchOption = (WorkerSearchOption) cmbSearchByOptions.getSelectedItem();
//					workerController.searchWorker(searchText, searchOption);
//				} else if (e.getSource() == btnClearSearchWorker) {
//					workerController.getAllWorkers();
//				} else {
//					workerController.deleteWorker(listWorkers.getSelectedValue());
//
//				}
//			}
//
//		};

		setTitle("Worker View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 767, 522);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 0.0, 0.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0,
				0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE, 0.0, 0.0 };
		contentPane.setLayout(gbl_contentPane);

		JButton lblManageOrder = new JButton("Manage Order");
		lblManageOrder.setName("lblManageOrder");
		lblManageOrder.setForeground(Color.WHITE);
		lblManageOrder.setOpaque(true);
		lblManageOrder.setFont(new Font("Arial", Font.BOLD, 16));
		lblManageOrder.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblManageOrder.setBackground(Color.RED);
		lblManageOrder.setFocusPainted(false);
		lblManageOrder.setPreferredSize(new Dimension(150, 40));
		GridBagConstraints gbc_lblManageOrder = new GridBagConstraints();
		gbc_lblManageOrder.gridheight = 2;
		gbc_lblManageOrder.ipady = 10;
		gbc_lblManageOrder.ipadx = 20;
		gbc_lblManageOrder.insets = new Insets(0, 0, 5, 0);
		gbc_lblManageOrder.gridx = 4;
		gbc_lblManageOrder.gridy = 1;
		contentPane.add(lblManageOrder, gbc_lblManageOrder);

		JLabel lblWorkerId = new JLabel("Worker ID");
		lblWorkerId.setFont(new Font("Arial", Font.BOLD, 14));
		lblWorkerId.setForeground(new Color(50, 50, 50));
		lblWorkerId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWorkerId.setVerticalAlignment(SwingConstants.CENTER);

		GridBagConstraints gbc_lblWorkerId = new GridBagConstraints();
		gbc_lblWorkerId.anchor = GridBagConstraints.EAST;
		gbc_lblWorkerId.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkerId.gridx = 0;
		gbc_lblWorkerId.gridy = 1;
		contentPane.add(lblWorkerId, gbc_lblWorkerId);

		txtWorkerId = new JFormattedTextField();
		txtWorkerId.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}

			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					e.consume();
				}
			}

		});
		txtWorkerId.setName("txtWorkerId");
		txtWorkerId.setFont(new Font("Arial", Font.PLAIN, 16));
		txtWorkerId.setColumns(10);
		GridBagConstraints gbc_txtWorkerId = new GridBagConstraints();
		gbc_txtWorkerId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWorkerId.insets = new Insets(0, 0, 5, 5);
		gbc_txtWorkerId.gridx = 1;
		gbc_txtWorkerId.gridy = 1;
		contentPane.add(txtWorkerId, gbc_txtWorkerId);

		JLabel lblWorkerPhoneNumber = new JLabel("Worker Phone No.");
		lblWorkerPhoneNumber.setFont(new Font("Arial", Font.BOLD, 14));
		lblWorkerPhoneNumber.setForeground(new Color(50, 50, 50));
		lblWorkerPhoneNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWorkerPhoneNumber.setVerticalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblWorkerPhoneNumber = new GridBagConstraints();
		gbc_lblWorkerPhoneNumber.anchor = GridBagConstraints.EAST;
		gbc_lblWorkerPhoneNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkerPhoneNumber.gridx = 2;
		gbc_lblWorkerPhoneNumber.gridy = 1;
		contentPane.add(lblWorkerPhoneNumber, gbc_lblWorkerPhoneNumber);

		txtWorkerPhone.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}

		});
		txtWorkerPhone.setName("txtWorkerPhone");
		txtWorkerPhone.setFont(new Font("Arial", Font.PLAIN, 16));
		txtWorkerPhone.setColumns(10);
		GridBagConstraints gbc_txtWorkerPhone = new GridBagConstraints();
		gbc_txtWorkerPhone.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWorkerPhone.insets = new Insets(0, 0, 5, 5);
		gbc_txtWorkerPhone.gridx = 3;
		gbc_txtWorkerPhone.gridy = 1;
		contentPane.add(txtWorkerPhone, gbc_txtWorkerPhone);

		JLabel lblWorkerName = new JLabel("Worker Name");
		lblWorkerName.setFont(new Font("Arial", Font.BOLD, 14));
		lblWorkerName.setForeground(new Color(50, 50, 50));
		lblWorkerName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWorkerName.setVerticalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblWorkerName = new GridBagConstraints();
		gbc_lblWorkerName.anchor = GridBagConstraints.EAST;
		gbc_lblWorkerName.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkerName.gridx = 0;
		gbc_lblWorkerName.gridy = 2;
		contentPane.add(lblWorkerName, gbc_lblWorkerName);

		txtWorkerName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}

		});
		txtWorkerName.setName("txtWorkerName");
		txtWorkerName.setFont(new Font("Arial", Font.PLAIN, 16));
		txtWorkerName.setColumns(10);
		GridBagConstraints gbc_txtWorkerName = new GridBagConstraints();
		gbc_txtWorkerName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWorkerName.insets = new Insets(0, 0, 5, 5);
		gbc_txtWorkerName.gridx = 1;
		gbc_txtWorkerName.gridy = 2;
		contentPane.add(txtWorkerName, gbc_txtWorkerName);

		JLabel lblWorkerCategory = new JLabel("Worker Category");
		lblWorkerCategory.setFont(new Font("Arial", Font.BOLD, 14));
		lblWorkerCategory.setForeground(new Color(50, 50, 50));
		lblWorkerCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWorkerCategory.setVerticalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblWorkerCategory = new GridBagConstraints();
		gbc_lblWorkerCategory.anchor = GridBagConstraints.EAST;
		gbc_lblWorkerCategory.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkerCategory.gridx = 2;
		gbc_lblWorkerCategory.gridy = 2;
		contentPane.add(lblWorkerCategory, gbc_lblWorkerCategory);

		cmbWorkerCategory.addActionListener(e -> handleButtonAndComboBoxStates());
		cmbWorkerCategory.setName("cmbWorkerCategory");
		GridBagConstraints gbc_cmbWorkerCategory = new GridBagConstraints();
		gbc_cmbWorkerCategory.insets = new Insets(0, 0, 5, 5);
		gbc_cmbWorkerCategory.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbWorkerCategory.gridx = 3;
		gbc_cmbWorkerCategory.gridy = 2;
		contentPane.add(cmbWorkerCategory, gbc_cmbWorkerCategory);

		showErrorLbl = new JLabel("");
		showErrorLbl.setName("showErrorLbl");
		showErrorLbl.setForeground(new Color(139, 0, 0));
		showErrorLbl.setFont(new Font("Verdana", Font.PLAIN, 16));
		showErrorLbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbc_showErrorLbl = new GridBagConstraints();
		gbc_showErrorLbl.gridwidth = 5;
		gbc_showErrorLbl.insets = new Insets(0, 0, 5, 0);
		gbc_showErrorLbl.gridx = 0;
		gbc_showErrorLbl.gridy = 3;
		contentPane.add(showErrorLbl, gbc_showErrorLbl);

		btnFetch.setEnabled(false);
		btnFetch.setName("btnFetch");
		btnFetch.setForeground(Color.WHITE);
		btnFetch.setOpaque(true);
		btnFetch.setFont(new Font("Arial", Font.BOLD, 16));
		btnFetch.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnFetch.setBackground(new Color(59, 89, 182));
		btnFetch.setFocusPainted(false);
		btnFetch.setPreferredSize(new Dimension(150, 40));
		btnFetch.addActionListener(e -> fetchWorkerMethod());

		GridBagConstraints gbc_btnFetch = new GridBagConstraints();
		gbc_btnFetch.ipady = 10;
		gbc_btnFetch.ipadx = 20;
		gbc_btnFetch.insets = new Insets(0, 0, 5, 5);
		gbc_btnFetch.gridx = 0;
		gbc_btnFetch.gridy = 4;
		contentPane.add(btnFetch, gbc_btnFetch);

		btnUpdate.setEnabled(false);
		btnUpdate.setName("btnUpdate");
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setOpaque(true);
		btnUpdate.setFont(new Font("Arial", Font.BOLD, 16));
		btnUpdate.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnUpdate.setBackground(new Color(59, 89, 182));
		btnUpdate.setFocusPainted(false);
		btnUpdate.setPreferredSize(new Dimension(150, 40));
		btnUpdate.addActionListener(e -> updateWorkerMethod());

		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.ipady = 10;
		gbc_btnUpdate.ipadx = 20;
		gbc_btnUpdate.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpdate.gridx = 1;
		gbc_btnUpdate.gridy = 4;
		contentPane.add(btnUpdate, gbc_btnUpdate);

		btnAdd.setEnabled(false);
		btnAdd.setName("btnAdd");
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setOpaque(true);
		btnAdd.setFont(new Font("Arial", Font.BOLD, 16));
		btnAdd.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnAdd.setBackground(new Color(59, 89, 182));
		btnAdd.setFocusPainted(false);
		btnAdd.setPreferredSize(new Dimension(150, 40));
		btnAdd.addActionListener(e -> addWorkerMethod());

		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.ipady = 10;
		gbc_btnAdd.ipadx = 20;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 4;
		contentPane.add(btnAdd, gbc_btnAdd);

		JLabel lblSearchWorker = new JLabel("Search Worker");
		lblSearchWorker.setFont(new Font("Arial", Font.BOLD, 14));
		lblSearchWorker.setForeground(new Color(50, 50, 50));
		lblSearchWorker.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSearchWorker.setVerticalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblSearchWorker = new GridBagConstraints();
		gbc_lblSearchWorker.anchor = GridBagConstraints.EAST;
		gbc_lblSearchWorker.insets = new Insets(0, 0, 5, 5);
		gbc_lblSearchWorker.gridx = 0;
		gbc_lblSearchWorker.gridy = 5;
		contentPane.add(lblSearchWorker, gbc_lblSearchWorker);

		txtSearchWorker.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleSearchWorkerAndClearButtonStates();
			}
		});

		txtSearchWorker.setName("txtSearchWorker");
		txtSearchWorker.setFont(new Font("Arial", Font.PLAIN, 16));
		txtSearchWorker.setColumns(10);
		GridBagConstraints gbc_txtSearchWorker = new GridBagConstraints();
		gbc_txtSearchWorker.insets = new Insets(0, 0, 5, 5);
		gbc_txtSearchWorker.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSearchWorker.gridx = 1;
		gbc_txtSearchWorker.gridy = 5;
		contentPane.add(txtSearchWorker, gbc_txtSearchWorker);

		JLabel lblSearchBy = new JLabel("Search By");
		lblSearchBy.setFont(new Font("Arial", Font.BOLD, 14));
		lblSearchBy.setForeground(new Color(50, 50, 50));
		lblSearchBy.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSearchBy.setVerticalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblSearchBy = new GridBagConstraints();
		gbc_lblSearchBy.insets = new Insets(0, 0, 5, 5);
		gbc_lblSearchBy.gridx = 2;
		gbc_lblSearchBy.gridy = 5;
		contentPane.add(lblSearchBy, gbc_lblSearchBy);

		cmbSearchByOptions.addActionListener(e -> handleSearchWorkerAndClearButtonStates());
		cmbSearchByOptions.setName("cmbSearchByOptions");
		GridBagConstraints gbc_cmbSearchByOptions = new GridBagConstraints();
		gbc_cmbSearchByOptions.insets = new Insets(0, 0, 5, 5);
		gbc_cmbSearchByOptions.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbSearchByOptions.gridx = 3;
		gbc_cmbSearchByOptions.gridy = 5;
		contentPane.add(cmbSearchByOptions, gbc_cmbSearchByOptions);

		btnSearchWorker.setEnabled(false);
		btnSearchWorker.setName("btnSearchWorker");
		btnSearchWorker.setForeground(Color.WHITE);
		btnSearchWorker.setOpaque(true);
		btnSearchWorker.setFont(new Font("Arial", Font.BOLD, 16));
		btnSearchWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnSearchWorker.setBackground(new Color(59, 89, 182));
		btnSearchWorker.setFocusPainted(false);
		btnSearchWorker.setPreferredSize(new Dimension(150, 40));

		btnSearchWorker.addActionListener(e -> {
			String searchText = txtSearchWorker.getText();
			WorkerSearchOption searchOption = (WorkerSearchOption) cmbSearchByOptions.getSelectedItem();
			workerController.searchWorker(searchText, searchOption);
		});
		GridBagConstraints gbc_btnSearchWorker = new GridBagConstraints();
		gbc_btnSearchWorker.ipady = 10;
		gbc_btnSearchWorker.ipadx = 20;
		gbc_btnSearchWorker.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearchWorker.gridx = 4;
		gbc_btnSearchWorker.gridy = 5;
		contentPane.add(btnSearchWorker, gbc_btnSearchWorker);

		showErrorLblSearchWorker = new JLabel("");
		showErrorLblSearchWorker.setName("showErrorLblSearchWorker");
		showErrorLblSearchWorker.setForeground(new Color(139, 0, 0));
		showErrorLblSearchWorker.setFont(new Font("Verdana", Font.PLAIN, 16));
		showErrorLblSearchWorker.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.RED, 1), BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbc_showErrorLblSearchWorker = new GridBagConstraints();
		gbc_showErrorLblSearchWorker.gridwidth = 2;
		gbc_showErrorLblSearchWorker.insets = new Insets(0, 0, 5, 5);
		gbc_showErrorLblSearchWorker.gridx = 0;
		gbc_showErrorLblSearchWorker.gridy = 6;
		contentPane.add(showErrorLblSearchWorker, gbc_showErrorLblSearchWorker);

		btnClearSearchWorker.setEnabled(false);
		btnClearSearchWorker.setName("btnClearSearchWorker");
		btnClearSearchWorker.setForeground(Color.WHITE);
		btnClearSearchWorker.setOpaque(true);
		btnClearSearchWorker.setFont(new Font("Arial", Font.BOLD, 16));
		btnClearSearchWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnClearSearchWorker.setBackground(new Color(59, 89, 182));
		btnClearSearchWorker.setFocusPainted(false);
		btnClearSearchWorker.setPreferredSize(new Dimension(150, 40));
		btnClearSearchWorker.addActionListener(e -> workerController.getAllWorkers());
		GridBagConstraints gbc_btnClearSearchWorker = new GridBagConstraints();
		gbc_btnClearSearchWorker.ipady = 10;
		gbc_btnClearSearchWorker.ipadx = 20;
		gbc_btnClearSearchWorker.insets = new Insets(0, 0, 5, 0);
		gbc_btnClearSearchWorker.gridx = 4;
		gbc_btnClearSearchWorker.gridy = 6;
		contentPane.add(btnClearSearchWorker, gbc_btnClearSearchWorker);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.gridheight = 7;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 7;
		contentPane.add(scrollPane, gbc_scrollPane);

		listWorkers = new JList<Worker>(workerListModel);
		listWorkers.setName("listWorkers");
		listWorkers.addListSelectionListener(e -> btnDelete.setEnabled(listWorkers.getSelectedIndex() != -1));
		listWorkers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(listWorkers);

		btnDelete.setEnabled(false);

		btnDelete.setName("btnDelete");
		btnDelete.setForeground(Color.WHITE);
		btnDelete.setOpaque(true);
		btnDelete.setFont(new Font("Arial", Font.BOLD, 16));
		btnDelete.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnDelete.setBackground(new Color(59, 89, 182));
		btnDelete.setFocusPainted(false);
		btnDelete.setPreferredSize(new Dimension(150, 40));
		btnDelete.addActionListener(e -> workerController.deleteWorker(listWorkers.getSelectedValue()));
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.ipady = 10;
		gbc_btnDelete.ipadx = 20;
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 1;
		gbc_btnDelete.gridy = 14;
		contentPane.add(btnDelete, gbc_btnDelete);

//		JLabel lblSearchOrdersBy = new JLabel("Search Orders By Worker ID");
//		lblSearchOrdersBy.setFont(new Font("Arial", Font.BOLD, 14));
//		lblSearchOrdersBy.setForeground(new Color(50, 50, 50));
//		lblSearchOrdersBy.setHorizontalAlignment(SwingConstants.RIGHT);
//		lblSearchOrdersBy.setVerticalAlignment(SwingConstants.CENTER);
//		GridBagConstraints gbc_lblSearchOrdersBy = new GridBagConstraints();
//		gbc_lblSearchOrdersBy.anchor = GridBagConstraints.EAST;
//		gbc_lblSearchOrdersBy.insets = new Insets(0, 0, 5, 5);
//		gbc_lblSearchOrdersBy.gridx = 0;
//		gbc_lblSearchOrdersBy.gridy = 15;
//		contentPane.add(lblSearchOrdersBy, gbc_lblSearchOrdersBy);

//		txtOrdersByWorkerId.addKeyListener(new KeyAdapter() {
//			public void keyReleased(KeyEvent e) {
//				handleSearchOrderByWorkerIdButtonStates();
//			}
//
//			public void keyTyped(KeyEvent e) {
//				char c = e.getKeyChar();
//				if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
//					getToolkit().beep();
//					e.consume();
//				}
//			}
//
//		});
//		txtOrdersByWorkerId.setName("txtOrdersByWorkerId");
//		txtOrdersByWorkerId.setFont(new Font("Arial", Font.PLAIN, 16));
//		txtOrdersByWorkerId.setColumns(10);
//		GridBagConstraints gbc_txtOrdersByWorkerId = new GridBagConstraints();
//		gbc_txtOrdersByWorkerId.insets = new Insets(0, 0, 5, 5);
//		gbc_txtOrdersByWorkerId.fill = GridBagConstraints.HORIZONTAL;
//		gbc_txtOrdersByWorkerId.gridx = 1;
//		gbc_txtOrdersByWorkerId.gridy = 15;
//		contentPane.add(txtOrdersByWorkerId, gbc_txtOrdersByWorkerId);
//
//		btnSearchOrder.setEnabled(false);
//		btnSearchOrder.setName("btnSearchOrder");
//		btnSearchOrder.setForeground(Color.WHITE);
//		btnSearchOrder.setOpaque(true);
//		btnSearchOrder.setFont(new Font("Arial", Font.BOLD, 16));
//		btnSearchOrder.setBorder(new LineBorder(new Color(0, 0, 0)));
//		btnSearchOrder.setBackground(new Color(59, 89, 182));
//		btnSearchOrder.setFocusPainted(false);
//		btnSearchOrder.setPreferredSize(new Dimension(150, 40));
//		btnSearchOrder.addActionListener(e -> getOrdersByWorkerIdMethod());
//
//		GridBagConstraints gbc_btnSearchOrder = new GridBagConstraints();
//		gbc_btnSearchOrder.ipady = 10;
//		gbc_btnSearchOrder.ipadx = 20;
//		gbc_btnSearchOrder.insets = new Insets(0, 0, 5, 5);
//		gbc_btnSearchOrder.gridx = 2;
//		gbc_btnSearchOrder.gridy = 15;
//		contentPane.add(btnSearchOrder, gbc_btnSearchOrder);

//		scrollPane_1 = new JScrollPane();
//		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
//		gbc_scrollPane_1.gridwidth = 5;
//		gbc_scrollPane_1.gridheight = 4;
//		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
//		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
//		gbc_scrollPane_1.gridx = 0;
//		gbc_scrollPane_1.gridy = 16;
//		contentPane.add(scrollPane_1, gbc_scrollPane_1);
//
//		listOrders = new JList<>(orderListModel);
//		listOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		listOrders.setName("listOrders");
//		scrollPane_1.setViewportView(listOrders);
//
//		showErrorLblSearchOrder = new JLabel("");
//		showErrorLblSearchOrder.setName("showErrorLblSearchOrder");
//		showErrorLblSearchOrder.setForeground(new Color(139, 0, 0));
//		showErrorLblSearchOrder.setFont(new Font("Verdana", Font.PLAIN, 16));
//		showErrorLblSearchOrder.setBorder(BorderFactory.createCompoundBorder(
//				BorderFactory.createLineBorder(Color.RED, 1), BorderFactory.createEmptyBorder(5, 10, 5, 10)));
//		GridBagConstraints gbc_showErrorLblSearchOrder = new GridBagConstraints();
//		gbc_showErrorLblSearchOrder.gridwidth = 5;
//		gbc_showErrorLblSearchOrder.insets = new Insets(0, 0, 5, 5);
//		gbc_showErrorLblSearchOrder.gridx = 0;
//		gbc_showErrorLblSearchOrder.gridy = 20;
//		contentPane.add(showErrorLblSearchOrder, gbc_showErrorLblSearchOrder);

		showErrorNotFoundLbl = new JLabel("");
		showErrorNotFoundLbl.setName("showErrorNotFoundLbl");
		showErrorNotFoundLbl.setForeground(new Color(139, 0, 0));
		showErrorNotFoundLbl.setFont(new Font("Verdana", Font.PLAIN, 16));
		showErrorNotFoundLbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbc_lblError = new GridBagConstraints();
		gbc_lblError.gridwidth = 5;
		gbc_lblError.insets = new Insets(0, 0, 0, 5);
		gbc_lblError.gridx = 0;
		gbc_lblError.gridy = 21;
		contentPane.add(showErrorNotFoundLbl, gbc_lblError);

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
//	private void handleSearchOrderByWorkerIdButtonStates() {
//		boolean isOrdersByWorkerId = txtOrdersByWorkerId.getText().trim().isEmpty();
//		btnSearchOrder.setEnabled(!isOrdersByWorkerId);
//	}

	/**
	 * Show all workers.
	 *
	 * @param worker the worker
	 */
	@Override
	public void showAllWorkers(List<Worker> worker) {
		resetAllSearchStates();
		workerListModel.clear();
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

			}
		}
		resetErrorLabel();

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
//	@Override
//	public void showOrderByWorkerId(List<CustomerOrder> orders) {
////		orderListModel.removeAllElements();
//		orders.stream().forEach(orderListModel::addElement);
//		resetErrorLabel();
//	}

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
//	@Override
//	public void showSearchOrderByWorkerIdError(String message, Worker worker) {
//		showErrorLblSearchOrder.setText(message + ": " + worker);
//	}

	/**
	 * Reset error label.
	 */
	private void resetErrorLabel() {
		showErrorLbl.setText(" ");
		showErrorNotFoundLbl.setText(" ");
		showErrorLblSearchWorker.setText(" ");
//		showErrorLblSearchOrder.setText(" ");
	}

	/**
	 * Reset all search states.
	 */
	private void resetAllSearchStates() {
		txtSearchWorker.setText(" ");
//		txtOrdersByWorkerId.setText(" ");
		cmbSearchByOptions.setSelectedItem(null);
	}

//	private void getOrdersByWorkerIdMethod() {
//		Worker worker = new Worker();
//		Long id = Long.parseLong(txtOrdersByWorkerId.getText());
//		worker.setWorkerId(id);
//		workerController.fetchOrdersByWorkerId(worker);
//	}

	private void fetchWorkerMethod() {
		Worker worker = new Worker();
		Long id = Long.parseLong(txtWorkerId.getText());

		worker.setWorkerId(id);
		workerController.fetchWorkerById(worker);
	}

	private void updateWorkerMethod() {
		Worker worker = new Worker();
		Long id = Long.parseLong(txtWorkerId.getText());
		worker.setWorkerId(id);
		worker.setWorkerName(txtWorkerName.getText());
		worker.setWorkerPhoneNumber(txtWorkerPhone.getText());
		worker.setWorkerCategory((OrderCategory) cmbWorkerCategory.getSelectedItem());
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);
	}

	private void addWorkerMethod() {
		Worker worker = new Worker();
		worker.setWorkerName(txtWorkerName.getText());
		worker.setWorkerPhoneNumber(txtWorkerPhone.getText());
		worker.setWorkerCategory((OrderCategory) cmbWorkerCategory.getSelectedItem());
		workerController.createOrUpdateWorker(worker, OperationType.ADD);
	}

}