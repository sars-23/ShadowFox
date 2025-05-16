import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ContactManagerGUI extends JFrame {
    private final ContactManager manager = new ContactManager();
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField nameField = new JTextField(15);
    private final JTextField phoneField = new JTextField(10);
    private final JTextField emailField = new JTextField(15);

    public ContactManagerGUI() {
        setTitle("Simple Contact Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add / Update Contact"));

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Phone (10 digits):"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        formPanel.add(addBtn);
        formPanel.add(updateBtn);

        // Table
        String[] columns = {"Name", "Phone", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);

        // Buttons
        JButton deleteBtn = new JButton("Delete Selected");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteBtn);

        // Layout
        add(formPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        addBtn.addActionListener(e -> addContact());
        updateBtn.addActionListener(e -> updateContact());
        deleteBtn.addActionListener(e -> deleteContact());
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                nameField.setText(tableModel.getValueAt(row, 0).toString());
                phoneField.setText(tableModel.getValueAt(row, 1).toString());
                emailField.setText(tableModel.getValueAt(row, 2).toString());
            }
        });

        setVisible(true);
    }

    private void addContact() {
        try {
            Contact c = getContactFromFields();
            manager.addContact(c);
            refreshTable();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void updateContact() {
        String phone = phoneField.getText();
        if (manager.getContact(phone) == null) {
            showError("Contact not found.");
            return;
        }
        try {
            Contact updated = getContactFromFields();
            manager.updateContact(phone, updated);
            refreshTable();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void deleteContact() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("No row selected.");
            return;
        }
        String phone = tableModel.getValueAt(row, 1).toString();
        manager.deleteContact(phone);
        refreshTable();
    }

    private Contact getContactFromFields() {
        return new Contact(
                nameField.getText(),
                phoneField.getText(),
                emailField.getText()
        );
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Contact c : manager.getAllContacts()) {
            tableModel.addRow(new Object[]{c.getName(), c.getPhone(), c.getEmail()});
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContactManagerGUI::new);
    }
}
