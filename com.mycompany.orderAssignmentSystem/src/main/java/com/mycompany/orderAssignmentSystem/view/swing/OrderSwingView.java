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

import com.toedter.calendar.JDateChooser;

public class OrderSwingView extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtOrderId;
	private JTextField txtCustomerName;
	private JTextField txtCustomerAddress;
	private JTextField txtSearchOrder;
	private JTextField txtCustomerPhone;
	private JTextField txtOrderDescription;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OrderSwingView frame = new OrderSwingView();
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
	public OrderSwingView() {
		Font fontWithSize = new Font("Arial", Font.BOLD, 14);
		Font textFieldFontSize = new Font("Arial", Font.PLAIN, 12);

		ImageIcon icon = new ImageIcon(OrderSwingView.class.getResource("/images/check-circle.jpg"));
		Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Adjust the width and
		ImageIcon scaledIcon = new ImageIcon(scaledImage);

		setTitle("Order");
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

		JLabel lblOrderForm = new JLabel("Order Form");
		lblOrderForm.setFont(fontWithSize);
		lblOrderForm.setBounds(10, 11, 285, 30);
		panelHeader.add(lblOrderForm);

		JLabel lblManageWorker = new JLabel("Manage Worker");
		lblManageWorker.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblManageWorker.setOpaque(true);
		lblManageWorker.setHorizontalAlignment(SwingConstants.CENTER);
		lblManageWorker.setFont(new Font("Arial", Font.BOLD, 14));
		lblManageWorker.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		lblManageWorker.setBackground(new Color(250, 138, 132));
		lblManageWorker.setBounds(671, 10, 153, 30);
		panelHeader.add(lblManageWorker);

		JPanel panelContent = new JPanel();
		panelContent.setLayout(null);
		panelContent.setBackground(new Color(226, 238, 121));
		panelContent.setBounds(0, 40, 900, 511);
		contentPane.add(panelContent);

		JLabel lblWorkerId = new JLabel("Order ID");
		lblWorkerId.setIcon(scaledIcon);
		lblWorkerId.setIconTextGap(8);
		lblWorkerId.setFont(fontWithSize);
		lblWorkerId.setBounds(21, 22, 235, 20);
		panelContent.add(lblWorkerId);

		txtOrderId = new JTextField();
		txtOrderId.setFont(textFieldFontSize);
		txtOrderId.setColumns(10);
		txtOrderId.setBounds(266, 22, 250, 20);
		panelContent.add(txtOrderId);

		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Arial", Font.PLAIN, 14));
		btnAdd.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnAdd.setBackground(new Color(252, 195, 174));
		btnAdd.setBounds(528, 68, 120, 30);
		btnAdd.setOpaque(true); // Set content area filled property

		panelContent.add(btnAdd);

		JLabel lblCustomerName = new JLabel("Customer Name");
		lblCustomerName.setIcon(scaledIcon);
		lblCustomerName.setIconTextGap(8);
		lblCustomerName.setFont(fontWithSize);
		lblCustomerName.setBounds(21, 50, 235, 20);
		panelContent.add(lblCustomerName);

		txtCustomerName = new JTextField();
		txtCustomerName.setFont(textFieldFontSize);
		txtCustomerName.setColumns(10);
		txtCustomerName.setBounds(266, 50, 250, 20);
		panelContent.add(txtCustomerName);

		JLabel lblWorkerPhoneNumber = new JLabel("Customer Address");
		lblWorkerPhoneNumber.setIcon(scaledIcon);
		lblWorkerPhoneNumber.setIconTextGap(8);
		lblWorkerPhoneNumber.setFont(fontWithSize);
		lblWorkerPhoneNumber.setBounds(21, 80, 235, 20);
		panelContent.add(lblWorkerPhoneNumber);

		txtCustomerAddress = new JTextField();
		txtCustomerAddress.setFont(textFieldFontSize);
		txtCustomerAddress.setColumns(10);
		txtCustomerAddress.setBounds(266, 80, 250, 20);
		panelContent.add(txtCustomerAddress);

		JLabel lblWorkerCategory = new JLabel("Worker");
		lblWorkerCategory.setIcon(scaledIcon);
		lblWorkerCategory.setIconTextGap(8);
		lblWorkerCategory.setFont(fontWithSize);
		lblWorkerCategory.setBounds(21, 260, 235, 20);
		panelContent.add(lblWorkerCategory);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setFont(new Font("Arial", Font.PLAIN, 14));
		btnUpdate.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnUpdate.setBackground(new Color(252, 195, 174));
		btnUpdate.setBounds(528, 117, 120, 30);
		btnUpdate.setOpaque(true); // Set content area filled property

		panelContent.add(btnUpdate);

		JComboBox cmbWorker = new JComboBox();
		cmbWorker.setBounds(266, 260, 250, 20);
		panelContent.add(cmbWorker);

		JLabel lblSearchWorker = new JLabel("Search Order");
		lblSearchWorker.setIconTextGap(8);
		lblSearchWorker.setFont(fontWithSize);
		lblSearchWorker.setBounds(21, 318, 150, 32);
		panelContent.add(lblSearchWorker);

		txtSearchOrder = new JTextField();
		txtSearchOrder.setFont(textFieldFontSize);
		txtSearchOrder.setColumns(10);
		txtSearchOrder.setBounds(153, 319, 206, 32);
		panelContent.add(txtSearchOrder);

		JLabel lblSearchBy = new JLabel("Search By");
		lblSearchBy.setIconTextGap(8);
		lblSearchBy.setFont(fontWithSize);
		lblSearchBy.setBounds(382, 318, 111, 32);
		panelContent.add(lblSearchBy);

		JButton btnSearchOrder = new JButton("Search");
		btnSearchOrder.setFont(new Font("Arial", Font.PLAIN, 14));
		btnSearchOrder.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnSearchOrder.setBackground(new Color(252, 195, 174));
		btnSearchOrder.setBounds(680, 319, 120, 30);
		btnSearchOrder.setOpaque(true); // Set content area filled property

		panelContent.add(btnSearchOrder);

		JComboBox cmbSearchBy = new JComboBox();
		cmbSearchBy.setBounds(491, 319, 177, 32);
		panelContent.add(cmbSearchBy);

		JList listOrders = new JList();
		listOrders.setBounds(21, 387, 803, 75);
		panelContent.add(listOrders);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Arial", Font.PLAIN, 14));
		btnDelete.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnDelete.setBackground(new Color(252, 195, 174));
		btnDelete.setBounds(373, 475, 120, 30);
		btnDelete.setOpaque(true); // Set content area filled property

		panelContent.add(btnDelete);

		JLabel lblCustomerPhone = new JLabel("Customer Phone #");
		lblCustomerPhone.setIcon(scaledIcon);
		lblCustomerPhone.setIconTextGap(8);
		lblCustomerPhone.setFont(fontWithSize);
		lblCustomerPhone.setBounds(21, 110, 235, 20);
		panelContent.add(lblCustomerPhone);

		txtCustomerPhone = new JTextField();
		txtCustomerPhone.setFont(textFieldFontSize);
		txtCustomerPhone.setColumns(10);
		txtCustomerPhone.setBounds(266, 110, 250, 20);
		panelContent.add(txtCustomerPhone);

		JDateChooser appointmentDateChooser = new JDateChooser();
		appointmentDateChooser.setFont(textFieldFontSize);
		appointmentDateChooser.setDateFormatString("dd-MM-yyyy");
		appointmentDateChooser.setBounds(266, 140, 250, 20);
		panelContent.add(appointmentDateChooser);

		JLabel lblAppointmentDate = new JLabel("Appointment Date");
		lblAppointmentDate.setIcon(scaledIcon);
		lblAppointmentDate.setIconTextGap(8);
		lblAppointmentDate.setFont(fontWithSize);
		lblAppointmentDate.setBounds(21, 140, 235, 20);
		panelContent.add(lblAppointmentDate);

		JLabel lblOrderDescription = new JLabel("Order Description");
		lblOrderDescription.setIcon(scaledIcon);
		lblOrderDescription.setIconTextGap(8);
		lblOrderDescription.setFont(fontWithSize);
		lblOrderDescription.setBounds(21, 170, 235, 20);
		panelContent.add(lblOrderDescription);

		txtOrderDescription = new JTextField();
		txtOrderDescription.setFont(textFieldFontSize);
		txtOrderDescription.setColumns(10);
		txtOrderDescription.setBounds(266, 170, 250, 20);
		panelContent.add(txtOrderDescription);

		JLabel lblOrderCategory = new JLabel("Order Category");
		lblOrderCategory.setIcon(scaledIcon);
		lblOrderCategory.setIconTextGap(8);
		lblOrderCategory.setFont(fontWithSize);
		lblOrderCategory.setBounds(21, 200, 235, 20);
		panelContent.add(lblOrderCategory);

		JComboBox cmbOrderCategory = new JComboBox();
		cmbOrderCategory.setBounds(266, 200, 250, 20);
		panelContent.add(cmbOrderCategory);

		JLabel lblOrderStatus = new JLabel("Order Status");
		lblOrderStatus.setIcon(scaledIcon);
		lblOrderStatus.setIconTextGap(8);
		lblOrderStatus.setFont(fontWithSize);
		lblOrderStatus.setBounds(21, 230, 235, 20);
		panelContent.add(lblOrderStatus);

		JComboBox cmbOrderStatus = new JComboBox();
		cmbOrderStatus.setBounds(266, 230, 250, 20);
		panelContent.add(cmbOrderStatus);
		
		JButton btnFetch_1 = new JButton("Fetch");
		btnFetch_1.setFont(new Font("Arial", Font.PLAIN, 14));
		btnFetch_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnFetch_1.setBackground(new Color(252, 195, 174));
		btnFetch_1.setBounds(528, 20, 120, 30);
		btnFetch_1.setOpaque(true); // Set content area filled property
		panelContent.add(btnFetch_1);
		
		JLabel searchErrorLbl = new JLabel("Search Error");
		searchErrorLbl.setForeground(Color.RED);
		searchErrorLbl.setIconTextGap(8);
		searchErrorLbl.setFont(new Font("Arial", Font.BOLD, 14));
		searchErrorLbl.setBounds(61, 346, 651, 32);
		panelContent.add(searchErrorLbl);
		
		JLabel showErrorLbl = new JLabel("Search Error");
		showErrorLbl.setIconTextGap(8);
		showErrorLbl.setForeground(Color.RED);
		showErrorLbl.setFont(new Font("Arial", Font.BOLD, 14));
		showErrorLbl.setBounds(21, 289, 651, 20);
		panelContent.add(showErrorLbl);
		
		JButton btnClearSearch = new JButton("Clear");
		btnClearSearch.setOpaque(true);
		btnClearSearch.setFont(new Font("Arial", Font.PLAIN, 14));
		btnClearSearch.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnClearSearch.setBackground(new Color(252, 195, 174));
		btnClearSearch.setBounds(680, 284, 120, 30);
		panelContent.add(btnClearSearch);

		JPanel panelFooter = new JPanel();
		panelFooter.setLayout(null);
		panelFooter.setBackground(new Color(250, 138, 132));
		panelFooter.setBounds(0, 550, 834, 45);
		panelFooter.setOpaque(true); // Set content area filled property
		contentPane.add(panelFooter);

		JLabel lblError = new JLabel("Currently, there is no error.");
		lblError.setFont(new Font("Arial", Font.BOLD, 13));
		lblError.setBounds(10, 14, 814, 14);
		lblError.setOpaque(true); // Set content area filled property
		panelFooter.add(lblError);
	}
}
