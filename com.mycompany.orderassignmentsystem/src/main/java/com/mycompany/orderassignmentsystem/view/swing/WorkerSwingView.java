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

import com.mycompany.orderassignmentsystem.controller.WorkerController;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.WorkerSearchOption;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.view.WorkerView;

/**
 * The WorkerSwingView class represents the graphical user interface for
 * managing workers.
 */
public class WorkerSwingView extends JFrame implements WorkerView {

	/** The Constant ARIAL. */
	private static final String ARIAL = "Arial";

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

	/** The worker controller. */
	private transient WorkerController workerController;

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

	/** The show error while add, update or fetch has error. */
	private JLabel showErrorLbl;

	/** The show error while search worker has error. */
	private JLabel showErrorLblSearchWorker;

	/** The show error while worker not found error. */
	private JLabel showErrorNotFoundLbl;

	/** The scroll pane. */
	private JScrollPane scrollPane;

	/** The btn delete. */
	private JButton btnDeleteWorker;

	/** The worker list model. */
	private DefaultListModel<Worker> workerListModel;

	/** The list workers. */
	private JList<Worker> listWorkers;

	/**
	 * Gets the worker list model.
	 *
	 * @return the worker list model
	 */
	public DefaultListModel<Worker> getWorkerListModel() {
		return workerListModel;
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
	 * Create the frame.
	 */
	public WorkerSwingView() {

		txtWorkerPhone = new JTextField();
		txtWorkerName = new JTextField();

		txtSearchWorker = new JTextField();
		cmbWorkerCategory = new JComboBox<>();
		cmbSearchByOptions = new JComboBox<>();
		workerListModel = new DefaultListModel<>();
		btnFetch = new JButton("Fetch");
		btnUpdate = new JButton("Update");
		btnAdd = new JButton("Add");
		btnSearchWorker = new JButton("Search Worker");
		btnClearSearchWorker = new JButton("Clear");
		btnDeleteWorker = new JButton("Delete");
		for (OrderCategory category : OrderCategory.values()) {
			cmbWorkerCategory.addItem(category);
		}
		for (WorkerSearchOption workerSearchOption : WorkerSearchOption.values()) {
			cmbSearchByOptions.addItem(workerSearchOption);
		}
		cmbWorkerCategory.setSelectedItem(null);
		cmbSearchByOptions.setSelectedItem(null);

		setTitle("Worker View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 767, 522);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gblContentPane = new GridBagLayout();
		gblContentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gblContentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gblContentPane.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 0.0, 0.0 };
		gblContentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0,
				0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE, 0.0, 0.0 };
		contentPane.setLayout(gblContentPane);

		JButton lblManageOrder = new JButton("Manage Order");
		lblManageOrder.setName("lblManageOrder");
		lblManageOrder.setForeground(Color.WHITE);
		lblManageOrder.setOpaque(true);
		lblManageOrder.setFont(new Font(ARIAL, Font.BOLD, 16));
		lblManageOrder.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblManageOrder.setBackground(Color.RED);
		lblManageOrder.setFocusPainted(false);
		lblManageOrder.setPreferredSize(new Dimension(150, 40));
		lblManageOrder.addActionListener(e -> openOrderForm());

		GridBagConstraints gbcLblManageOrder = new GridBagConstraints();
		gbcLblManageOrder.gridheight = 2;
		gbcLblManageOrder.ipady = 10;
		gbcLblManageOrder.ipadx = 20;
		gbcLblManageOrder.insets = new Insets(0, 0, 5, 0);
		gbcLblManageOrder.gridx = 4;
		gbcLblManageOrder.gridy = 1;
		contentPane.add(lblManageOrder, gbcLblManageOrder);

		JLabel lblWorkerId = new JLabel("Worker ID");
		lblWorkerId.setFont(new Font(ARIAL, Font.BOLD, 14));
		lblWorkerId.setForeground(new Color(50, 50, 50));
		lblWorkerId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWorkerId.setVerticalAlignment(SwingConstants.CENTER);

		GridBagConstraints gbcLblWorkerId = new GridBagConstraints();
		gbcLblWorkerId.anchor = GridBagConstraints.EAST;
		gbcLblWorkerId.insets = new Insets(0, 0, 5, 5);
		gbcLblWorkerId.gridx = 0;
		gbcLblWorkerId.gridy = 1;
		contentPane.add(lblWorkerId, gbcLblWorkerId);

		txtWorkerId = new JFormattedTextField();
		txtWorkerId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				checkCharacterIsNumber(e);
			}

		});
		txtWorkerId.setName("txtWorkerId");
		txtWorkerId.setFont(new Font(ARIAL, Font.PLAIN, 16));
		txtWorkerId.setColumns(10);
		GridBagConstraints gbcTxtWorkerId = new GridBagConstraints();
		gbcTxtWorkerId.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtWorkerId.insets = new Insets(0, 0, 5, 5);
		gbcTxtWorkerId.gridx = 1;
		gbcTxtWorkerId.gridy = 1;
		contentPane.add(txtWorkerId, gbcTxtWorkerId);

		JLabel lblWorkerPhoneNumber = new JLabel("Worker Phone No.");
		lblWorkerPhoneNumber.setFont(new Font(ARIAL, Font.BOLD, 14));
		lblWorkerPhoneNumber.setForeground(new Color(50, 50, 50));
		lblWorkerPhoneNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWorkerPhoneNumber.setVerticalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbcLblWorkerPhoneNumber = new GridBagConstraints();
		gbcLblWorkerPhoneNumber.anchor = GridBagConstraints.EAST;
		gbcLblWorkerPhoneNumber.insets = new Insets(0, 0, 5, 5);
		gbcLblWorkerPhoneNumber.gridx = 2;
		gbcLblWorkerPhoneNumber.gridy = 1;
		contentPane.add(lblWorkerPhoneNumber, gbcLblWorkerPhoneNumber);

		txtWorkerPhone.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}

		});
		txtWorkerPhone.setName("txtWorkerPhone");
		txtWorkerPhone.setFont(new Font(ARIAL, Font.PLAIN, 16));
		txtWorkerPhone.setColumns(10);
		GridBagConstraints gbcTxtWorkerPhone = new GridBagConstraints();
		gbcTxtWorkerPhone.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtWorkerPhone.insets = new Insets(0, 0, 5, 5);
		gbcTxtWorkerPhone.gridx = 3;
		gbcTxtWorkerPhone.gridy = 1;
		contentPane.add(txtWorkerPhone, gbcTxtWorkerPhone);

		JLabel lblWorkerName = new JLabel("Worker Name");
		lblWorkerName.setFont(new Font(ARIAL, Font.BOLD, 14));
		lblWorkerName.setForeground(new Color(50, 50, 50));
		lblWorkerName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWorkerName.setVerticalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbcLblWorkerName = new GridBagConstraints();
		gbcLblWorkerName.anchor = GridBagConstraints.EAST;
		gbcLblWorkerName.insets = new Insets(0, 0, 5, 5);
		gbcLblWorkerName.gridx = 0;
		gbcLblWorkerName.gridy = 2;
		contentPane.add(lblWorkerName, gbcLblWorkerName);

		txtWorkerName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleButtonAndComboBoxStates();
			}

		});
		txtWorkerName.setName("txtWorkerName");
		txtWorkerName.setFont(new Font(ARIAL, Font.PLAIN, 16));
		txtWorkerName.setColumns(10);
		GridBagConstraints gbcTxtWorkerName = new GridBagConstraints();
		gbcTxtWorkerName.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtWorkerName.insets = new Insets(0, 0, 5, 5);
		gbcTxtWorkerName.gridx = 1;
		gbcTxtWorkerName.gridy = 2;
		contentPane.add(txtWorkerName, gbcTxtWorkerName);

		JLabel lblWorkerCategory = new JLabel("Worker Category");
		lblWorkerCategory.setFont(new Font(ARIAL, Font.BOLD, 14));
		lblWorkerCategory.setForeground(new Color(50, 50, 50));
		lblWorkerCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWorkerCategory.setVerticalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbcLblWorkerCategory = new GridBagConstraints();
		gbcLblWorkerCategory.anchor = GridBagConstraints.EAST;
		gbcLblWorkerCategory.insets = new Insets(0, 0, 5, 5);
		gbcLblWorkerCategory.gridx = 2;
		gbcLblWorkerCategory.gridy = 2;
		contentPane.add(lblWorkerCategory, gbcLblWorkerCategory);

		cmbWorkerCategory.addActionListener(e -> handleButtonAndComboBoxStates());
		cmbWorkerCategory.setName("cmbWorkerCategory");
		GridBagConstraints gbcCmbWorkerCategory = new GridBagConstraints();
		gbcCmbWorkerCategory.insets = new Insets(0, 0, 5, 5);
		gbcCmbWorkerCategory.fill = GridBagConstraints.HORIZONTAL;
		gbcCmbWorkerCategory.gridx = 3;
		gbcCmbWorkerCategory.gridy = 2;
		contentPane.add(cmbWorkerCategory, gbcCmbWorkerCategory);

		showErrorLbl = new JLabel("");
		showErrorLbl.setName("showErrorLbl");
		showErrorLbl.setForeground(new Color(139, 0, 0));
		showErrorLbl.setFont(new Font(ARIAL, Font.PLAIN, 16));
		showErrorLbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbcShowErrorLbl = new GridBagConstraints();
		gbcShowErrorLbl.gridwidth = 5;
		gbcShowErrorLbl.insets = new Insets(0, 0, 5, 0);
		gbcShowErrorLbl.gridx = 0;
		gbcShowErrorLbl.gridy = 3;
		contentPane.add(showErrorLbl, gbcShowErrorLbl);

		btnFetch.setEnabled(false);
		btnFetch.setName("btnFetch");
		btnFetch.setForeground(Color.WHITE);
		btnFetch.setOpaque(true);
		btnFetch.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnFetch.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnFetch.setBackground(new Color(59, 89, 182));
		btnFetch.setFocusPainted(false);
		btnFetch.setPreferredSize(new Dimension(150, 40));
		btnFetch.addActionListener(e -> fetchWorkerMethod());

		GridBagConstraints gbcBtnFetch = new GridBagConstraints();
		gbcBtnFetch.ipady = 10;
		gbcBtnFetch.ipadx = 20;
		gbcBtnFetch.insets = new Insets(0, 0, 5, 5);
		gbcBtnFetch.gridx = 0;
		gbcBtnFetch.gridy = 4;
		contentPane.add(btnFetch, gbcBtnFetch);

		btnUpdate.setEnabled(false);
		btnUpdate.setName("btnUpdate");
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setOpaque(true);
		btnUpdate.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnUpdate.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnUpdate.setBackground(new Color(59, 89, 182));
		btnUpdate.setFocusPainted(false);
		btnUpdate.setPreferredSize(new Dimension(150, 40));
		btnUpdate.addActionListener(e -> updateWorkerMethod());

		GridBagConstraints gbcBtnUpdate = new GridBagConstraints();
		gbcBtnUpdate.ipady = 10;
		gbcBtnUpdate.ipadx = 20;
		gbcBtnUpdate.insets = new Insets(0, 0, 5, 5);
		gbcBtnUpdate.gridx = 1;
		gbcBtnUpdate.gridy = 4;
		contentPane.add(btnUpdate, gbcBtnUpdate);

		btnAdd.setEnabled(false);
		btnAdd.setName("btnAdd");
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setOpaque(true);
		btnAdd.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnAdd.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnAdd.setBackground(new Color(59, 89, 182));
		btnAdd.setFocusPainted(false);
		btnAdd.setPreferredSize(new Dimension(150, 40));
		btnAdd.addActionListener(e -> addWorkerMethod());

		GridBagConstraints gbcBtnAdd = new GridBagConstraints();
		gbcBtnAdd.ipady = 10;
		gbcBtnAdd.ipadx = 20;
		gbcBtnAdd.insets = new Insets(0, 0, 5, 5);
		gbcBtnAdd.gridx = 2;
		gbcBtnAdd.gridy = 4;
		contentPane.add(btnAdd, gbcBtnAdd);

		JLabel lblSearchWorker = new JLabel("Search Worker");
		lblSearchWorker.setFont(new Font(ARIAL, Font.BOLD, 14));
		lblSearchWorker.setForeground(new Color(50, 50, 50));
		lblSearchWorker.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSearchWorker.setVerticalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbcLblSearchWorker = new GridBagConstraints();
		gbcLblSearchWorker.anchor = GridBagConstraints.EAST;
		gbcLblSearchWorker.insets = new Insets(0, 0, 5, 5);
		gbcLblSearchWorker.gridx = 0;
		gbcLblSearchWorker.gridy = 5;
		contentPane.add(lblSearchWorker, gbcLblSearchWorker);

		txtSearchWorker.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleSearchWorkerAndClearButtonStates();
			}
		});

		txtSearchWorker.setName("txtSearchWorker");
		txtSearchWorker.setFont(new Font(ARIAL, Font.PLAIN, 16));
		txtSearchWorker.setColumns(10);
		GridBagConstraints gbcTxtSearchWorker = new GridBagConstraints();
		gbcTxtSearchWorker.insets = new Insets(0, 0, 5, 5);
		gbcTxtSearchWorker.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtSearchWorker.gridx = 1;
		gbcTxtSearchWorker.gridy = 5;
		contentPane.add(txtSearchWorker, gbcTxtSearchWorker);

		JLabel lblSearchBy = new JLabel("Search By");
		lblSearchBy.setFont(new Font(ARIAL, Font.BOLD, 14));
		lblSearchBy.setForeground(new Color(50, 50, 50));
		lblSearchBy.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSearchBy.setVerticalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbcLblSearchBy = new GridBagConstraints();
		gbcLblSearchBy.insets = new Insets(0, 0, 5, 5);
		gbcLblSearchBy.gridx = 2;
		gbcLblSearchBy.gridy = 5;
		contentPane.add(lblSearchBy, gbcLblSearchBy);

		cmbSearchByOptions.addActionListener(e -> handleSearchWorkerAndClearButtonStates());
		cmbSearchByOptions.setName("cmbSearchByOptions");
		GridBagConstraints gbcCmbSearchByOptions = new GridBagConstraints();
		gbcCmbSearchByOptions.insets = new Insets(0, 0, 5, 5);
		gbcCmbSearchByOptions.fill = GridBagConstraints.HORIZONTAL;
		gbcCmbSearchByOptions.gridx = 3;
		gbcCmbSearchByOptions.gridy = 5;
		contentPane.add(cmbSearchByOptions, gbcCmbSearchByOptions);

		btnSearchWorker.setEnabled(false);
		btnSearchWorker.setName("btnSearchWorker");
		btnSearchWorker.setForeground(Color.WHITE);
		btnSearchWorker.setOpaque(true);
		btnSearchWorker.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnSearchWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnSearchWorker.setBackground(new Color(59, 89, 182));
		btnSearchWorker.setFocusPainted(false);
		btnSearchWorker.setPreferredSize(new Dimension(150, 40));

		btnSearchWorker.addActionListener(e -> {
			String searchText = txtSearchWorker.getText();
			WorkerSearchOption searchOption = (WorkerSearchOption) cmbSearchByOptions.getSelectedItem();
			workerController.searchWorker(searchText, searchOption);
		});
		GridBagConstraints gbcBtnSearchWorker = new GridBagConstraints();
		gbcBtnSearchWorker.ipady = 10;
		gbcBtnSearchWorker.ipadx = 20;
		gbcBtnSearchWorker.insets = new Insets(0, 0, 5, 0);
		gbcBtnSearchWorker.gridx = 4;
		gbcBtnSearchWorker.gridy = 5;
		contentPane.add(btnSearchWorker, gbcBtnSearchWorker);

		showErrorLblSearchWorker = new JLabel("");
		showErrorLblSearchWorker.setName("showErrorLblSearchWorker");
		showErrorLblSearchWorker.setForeground(new Color(139, 0, 0));
		showErrorLblSearchWorker.setFont(new Font(ARIAL, Font.PLAIN, 16));
		showErrorLblSearchWorker.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.RED, 1), BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbcShowErrorLblSearchWorker = new GridBagConstraints();
		gbcShowErrorLblSearchWorker.gridwidth = 2;
		gbcShowErrorLblSearchWorker.insets = new Insets(0, 0, 5, 5);
		gbcShowErrorLblSearchWorker.gridx = 0;
		gbcShowErrorLblSearchWorker.gridy = 6;
		contentPane.add(showErrorLblSearchWorker, gbcShowErrorLblSearchWorker);

		btnClearSearchWorker.setEnabled(false);
		btnClearSearchWorker.setName("btnClearSearchWorker");
		btnClearSearchWorker.setForeground(Color.WHITE);
		btnClearSearchWorker.setOpaque(true);
		btnClearSearchWorker.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnClearSearchWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnClearSearchWorker.setBackground(new Color(59, 89, 182));
		btnClearSearchWorker.setFocusPainted(false);
		btnClearSearchWorker.setPreferredSize(new Dimension(150, 40));
		btnClearSearchWorker.addActionListener(e -> workerController.getAllWorkers());
		GridBagConstraints gbcBtnClearSearchWorker = new GridBagConstraints();
		gbcBtnClearSearchWorker.ipady = 10;
		gbcBtnClearSearchWorker.ipadx = 20;
		gbcBtnClearSearchWorker.insets = new Insets(0, 0, 5, 0);
		gbcBtnClearSearchWorker.gridx = 4;
		gbcBtnClearSearchWorker.gridy = 6;
		contentPane.add(btnClearSearchWorker, gbcBtnClearSearchWorker);

		scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.gridwidth = 5;
		gbcScrollPane.gridheight = 7;
		gbcScrollPane.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 7;
		contentPane.add(scrollPane, gbcScrollPane);

		listWorkers = new JList<>(workerListModel);
		listWorkers.setName("listWorkers");
		listWorkers.addListSelectionListener(e -> btnDeleteWorker.setEnabled(listWorkers.getSelectedIndex() != -1));
		listWorkers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(listWorkers);

		btnDeleteWorker.setEnabled(false);

		btnDeleteWorker.setName("btnDelete");
		btnDeleteWorker.setForeground(Color.WHITE);
		btnDeleteWorker.setOpaque(true);
		btnDeleteWorker.setFont(new Font(ARIAL, Font.BOLD, 16));
		btnDeleteWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnDeleteWorker.setBackground(new Color(59, 89, 182));
		btnDeleteWorker.setFocusPainted(false);
		btnDeleteWorker.setPreferredSize(new Dimension(150, 40));
		btnDeleteWorker.addActionListener(e -> workerController.deleteWorker(listWorkers.getSelectedValue()));
		GridBagConstraints gbcBtnDelete = new GridBagConstraints();
		gbcBtnDelete.ipady = 10;
		gbcBtnDelete.ipadx = 20;
		gbcBtnDelete.insets = new Insets(0, 0, 5, 5);
		gbcBtnDelete.gridx = 1;
		gbcBtnDelete.gridy = 14;
		contentPane.add(btnDeleteWorker, gbcBtnDelete);

		showErrorNotFoundLbl = new JLabel("");
		showErrorNotFoundLbl.setName("showErrorNotFoundLbl");
		showErrorNotFoundLbl.setForeground(new Color(139, 0, 0));
		showErrorNotFoundLbl.setFont(new Font(ARIAL, Font.PLAIN, 16));
		showErrorNotFoundLbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		GridBagConstraints gbcLblError = new GridBagConstraints();
		gbcLblError.gridwidth = 5;
		gbcLblError.insets = new Insets(0, 0, 0, 5);
		gbcLblError.gridx = 0;
		gbcLblError.gridy = 21;
		contentPane.add(showErrorNotFoundLbl, gbcLblError);

	}

	@Override
	public void showAllWorkers(List<Worker> worker) {
		resetAllSearchStates();
		workerListModel.clear();
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

			}
		}
		resetErrorLabel();

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

	/**
	 * Reset error label.
	 */
	private void resetErrorLabel() {
		showErrorLbl.setText(" ");
		showErrorNotFoundLbl.setText(" ");
		showErrorLblSearchWorker.setText(" ");
	}

	/**
	 * Reset all search states.
	 */
	private void resetAllSearchStates() {
		txtSearchWorker.setText(" ");
		cmbSearchByOptions.setSelectedItem(null);
	}

	/**
	 * Fetch worker method.
	 */
	private void fetchWorkerMethod() {
		Worker worker = new Worker();
		Long id = Long.parseLong(txtWorkerId.getText());

		worker.setWorkerId(id);
		workerController.fetchWorkerById(worker);
	}

	/**
	 * Update worker method.
	 */
	private void updateWorkerMethod() {
		Worker worker = new Worker();
		Long id = Long.parseLong(txtWorkerId.getText());
		worker.setWorkerId(id);
		worker.setWorkerName(txtWorkerName.getText());
		worker.setWorkerPhoneNumber(txtWorkerPhone.getText());
		worker.setWorkerCategory((OrderCategory) cmbWorkerCategory.getSelectedItem());
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);
	}

	/**
	 * Adds the worker method.
	 */
	private void addWorkerMethod() {
		Worker worker = new Worker();
		worker.setWorkerName(txtWorkerName.getText());
		worker.setWorkerPhoneNumber(txtWorkerPhone.getText());
		worker.setWorkerCategory((OrderCategory) cmbWorkerCategory.getSelectedItem());
		workerController.createOrUpdateWorker(worker, OperationType.ADD);
	}

	/**
	 * Check character is number.
	 *
	 * @param e the e
	 */
	private void checkCharacterIsNumber(KeyEvent e) {
		char c = e.getKeyChar();
		if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE))) {
			getToolkit().beep();
			e.consume();
		}
	}

	/**
	 * Open order form.
	 */
	private void openOrderForm() {
		this.setVisible(false);
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

}