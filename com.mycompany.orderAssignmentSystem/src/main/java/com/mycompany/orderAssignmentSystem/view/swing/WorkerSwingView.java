package com.mycompany.orderAssignmentSystem.view.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

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

public class WorkerSwingView extends JFrame {

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

	/**
	 * Launch the application.
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
	 * Create the frame.
	 */
	public WorkerSwingView() {
		Font fontSizeTitle = new Font("Arial", Font.BOLD, 14);
		Font fontWithSize = new Font("Arial", Font.BOLD, 14);
		Font fontSizeBtn = new Font("Arial", Font.PLAIN, 14);

		ImageIcon icon = new ImageIcon(OrderSwingView.class.getResource("/images/check-circle.jpg"));
		Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Adjust the width and
		ImageIcon scaledIcon = new ImageIcon(scaledImage);
		setTitle("Worker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panelHeader = new JPanel();
		panelHeader.setLayout(null);
		panelHeader.setBackground(new Color(117, 169, 249));
		panelHeader.setBounds(0, 0, 834, 50);
		contentPane.add(panelHeader);

		JLabel lblWorkderForm = new JLabel("Worker Form");
		lblWorkderForm.setFont(fontSizeTitle);
		lblWorkderForm.setBounds(10, 11, 285, 30);
		panelHeader.add(lblWorkderForm);

		JLabel lblManageOrder = new JLabel("Manage Order");
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
		txtWorkerId.setFont(new Font("Arial", Font.PLAIN, 16));
		txtWorkerId.setColumns(10);
		txtWorkerId.setBounds(266, 24, 250, 32);
		panelContent.add(txtWorkerId);

		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(fontSizeBtn);
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

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setFont(fontSizeBtn);
		btnUpdate.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnUpdate.setBackground(new Color(252, 195, 174));
		btnUpdate.setBounds(528, 100, 120, 30);
		btnUpdate.setOpaque(true); // Set content area filled property

		panelContent.add(btnUpdate);

		JButton btnFetch = new JButton("Fetch");
		btnFetch.setFont(fontSizeBtn);
		btnFetch.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnFetch.setBackground(new Color(252, 195, 174));
		btnFetch.setBounds(526, 20, 120, 30);
		btnFetch.setOpaque(true); // Set content area filled property

		panelContent.add(btnFetch);

		JComboBox cmbWorkerCategory = new JComboBox();
		cmbWorkerCategory.setBounds(266, 120, 250, 32);
		panelContent.add(cmbWorkerCategory);

		JLabel lblSearchWorker = new JLabel("Search Worker");
		lblSearchWorker.setIconTextGap(8);
		lblSearchWorker.setFont(fontWithSize);
		lblSearchWorker.setBounds(21, 185, 166, 32);
		panelContent.add(lblSearchWorker);

		txtSearchWorker = new JTextField();
		txtSearchWorker.setFont(new Font("Arial", Font.PLAIN, 16));
		txtSearchWorker.setColumns(10);
		txtSearchWorker.setBounds(197, 185, 189, 32);
		panelContent.add(txtSearchWorker);

		JLabel lblSearchBy = new JLabel("Search By");
		lblSearchBy.setIconTextGap(8);
		lblSearchBy.setFont(fontWithSize);
		lblSearchBy.setBounds(396, 185, 111, 32);
		panelContent.add(lblSearchBy);

		JButton btnSearchWorker = new JButton("Search");
		btnSearchWorker.setFont(fontSizeBtn);
		btnSearchWorker.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnSearchWorker.setBackground(new Color(252, 195, 174));
		btnSearchWorker.setBounds(704, 185, 120, 30);
		btnSearchWorker.setOpaque(true); // Set content area filled property

		panelContent.add(btnSearchWorker);

		JComboBox cmbSearchBy = new JComboBox();
		cmbSearchBy.setBounds(517, 185, 177, 32);
		panelContent.add(cmbSearchBy);

		JList listWorkers = new JList();
		listWorkers.setBounds(21, 249, 673, 75);
		panelContent.add(listWorkers);

		JButton btnDelete = new JButton("Delete");
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
		txtOrdersByWorkerId.setFont(new Font("Arial", Font.PLAIN, 16));
		txtOrdersByWorkerId.setColumns(10);
		txtOrdersByWorkerId.setBounds(331, 336, 185, 34);
		panelContent.add(txtOrdersByWorkerId);

		JList listOrders = new JList();
		listOrders.setBounds(21, 398, 803, 75);
		panelContent.add(listOrders);

		JButton btnSearchOrder = new JButton("Search");
		btnSearchOrder.setFont(fontSizeBtn);
		btnSearchOrder.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnSearchOrder.setBackground(new Color(252, 195, 174));
		btnSearchOrder.setBounds(526, 336, 120, 30);
		btnSearchOrder.setOpaque(true); // Set content area filled property

		panelContent.add(btnSearchOrder);
		
		JLabel showErrorLbl = new JLabel("Search Error");
		showErrorLbl.setIconTextGap(8);
		showErrorLbl.setForeground(Color.RED);
		showErrorLbl.setFont(new Font("Arial", Font.BOLD, 14));
		showErrorLbl.setBounds(19, 164, 651, 20);
		panelContent.add(showErrorLbl);
		
		JLabel showErrorLbl_1 = new JLabel("Search Error");
		showErrorLbl_1.setIconTextGap(8);
		showErrorLbl_1.setForeground(Color.RED);
		showErrorLbl_1.setFont(new Font("Arial", Font.BOLD, 14));
		showErrorLbl_1.setBounds(21, 225, 651, 20);
		panelContent.add(showErrorLbl_1);
		
		JLabel showErrorLbl_1_1 = new JLabel("Search Error");
		showErrorLbl_1_1.setIconTextGap(8);
		showErrorLbl_1_1.setForeground(Color.RED);
		showErrorLbl_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		showErrorLbl_1_1.setBounds(19, 378, 651, 20);
		panelContent.add(showErrorLbl_1_1);
		
		JButton btnClearSearch = new JButton("Clear");
		btnClearSearch.setOpaque(true);
		btnClearSearch.setFont(new Font("Arial", Font.PLAIN, 14));
		btnClearSearch.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnClearSearch.setBackground(new Color(252, 195, 174));
		btnClearSearch.setBounds(704, 143, 120, 30);
		panelContent.add(btnClearSearch);
		
		JButton btnClearSearch_1 = new JButton("Clear");
		btnClearSearch_1.setOpaque(true);
		btnClearSearch_1.setFont(new Font("Arial", Font.PLAIN, 14));
		btnClearSearch_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnClearSearch_1.setBackground(new Color(252, 195, 174));
		btnClearSearch_1.setBounds(658, 336, 120, 30);
		panelContent.add(btnClearSearch_1);

		JPanel panelFooter = new JPanel();
		panelFooter.setLayout(null);
		panelFooter.setBackground(new Color(250, 138, 132));
		panelFooter.setBounds(0, 550, 834, 45);
		contentPane.add(panelFooter);

		JLabel lblError = new JLabel("Currently, there is no error.");
		lblError.setFont(new Font("Arial", Font.BOLD, 13));
		lblError.setBounds(10, 14, 814, 14);
		panelFooter.add(lblError);
	}
}
