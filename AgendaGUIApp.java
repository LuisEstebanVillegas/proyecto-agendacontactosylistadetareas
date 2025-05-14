import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class AgendaGUIApp extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel contactsPanel, tasksPanel;

    // Contacts components
    private JTable contactsTable;
    private DefaultTableModel contactsTableModel;
    private JButton addContactBtn, editContactBtn, deleteContactBtn;

    // Tasks components
    private JTable tasksTable;
    private DefaultTableModel tasksTableModel;
    private JButton addTaskBtn, editTaskBtn, deleteTaskBtn, completeTaskBtn;

    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayList<Task> tasks = new ArrayList<>();

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AgendaGUIApp() {
        setTitle("Agenda de Contactos y Tareas");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        createContactsPanel();
        createTasksPanel();

        tabbedPane.addTab("Contactos", contactsPanel);
        tabbedPane.addTab("Tareas", tasksPanel);

        add(tabbedPane);

        setVisible(true);
    }

    private void createContactsPanel() {
        contactsPanel = new JPanel(new BorderLayout(10, 10));
        String[] columns = {"Nombre", "Teléfono", "Correo electrónico"};
        contactsTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // disable editing directly
            }
        };
        contactsTable = new JTable(contactsTableModel);
        contactsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contactsTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(contactsTable);
        contactsPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        addContactBtn = new JButton("Agregar");
        editContactBtn = new JButton("Modificar");
        deleteContactBtn = new JButton("Eliminar");

        editContactBtn.setEnabled(false);
        deleteContactBtn.setEnabled(false);

        buttonsPanel.add(addContactBtn);
        buttonsPanel.add(editContactBtn);
        buttonsPanel.add(deleteContactBtn);

        contactsPanel.add(buttonsPanel, BorderLayout.SOUTH);

        addContactBtn.addActionListener(e -> openContactDialog(null));
        editContactBtn.addActionListener(e -> {
            int selected = contactsTable.getSelectedRow();
            if (selected >= 0) {
                openContactDialog(contacts.get(selected));
            }
        });
        deleteContactBtn.addActionListener(e -> {
            int selected = contactsTable.getSelectedRow();
            if (selected >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar contacto seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    contacts.remove(selected);
                    refreshContactsTable();
                }
            }
        });

        contactsTable.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = contactsTable.getSelectedRow() >= 0;
            editContactBtn.setEnabled(selected);
            deleteContactBtn.setEnabled(selected);
        });
    }

    private void createTasksPanel() {
        tasksPanel = new JPanel(new BorderLayout(10, 10));
        String[] columns = {"Título", "Descripción", "Vence", "Estado"};
        tasksTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tasksTable = new JTable(tasksTableModel);
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasksTable.setRowHeight(26);
        tasksTable.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(tasksTable);
        tasksPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        addTaskBtn = new JButton("Agregar");
        editTaskBtn = new JButton("Modificar");
        deleteTaskBtn = new JButton("Eliminar");
        completeTaskBtn = new JButton("Marcar Completada");

        editTaskBtn.setEnabled(false);
        deleteTaskBtn.setEnabled(false);
        completeTaskBtn.setEnabled(false);

        buttonsPanel.add(addTaskBtn);
        buttonsPanel.add(editTaskBtn);
        buttonsPanel.add(deleteTaskBtn);
        buttonsPanel.add(completeTaskBtn);

        tasksPanel.add(buttonsPanel, BorderLayout.SOUTH);

        addTaskBtn.addActionListener(e -> openTaskDialog(null));
        editTaskBtn.addActionListener(e -> {
            int selected = tasksTable.getSelectedRow();
            if (selected >= 0) {
                openTaskDialog(tasks.get(selected));
            }
        });
        deleteTaskBtn.addActionListener(e -> {
            int selected = tasksTable.getSelectedRow();
            if (selected >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar tarea seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tasks.remove(selected);
                    refreshTasksTable();
                }
            }
        });
        completeTaskBtn.addActionListener(e -> {
            int selected = tasksTable.getSelectedRow();
            if (selected >= 0) {
                Task t = tasks.get(selected);
                t.completed = true;
                refreshTasksTable();
            }
        });

        tasksTable.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = tasksTable.getSelectedRow() >= 0;
            editTaskBtn.setEnabled(selected);
            deleteTaskBtn.setEnabled(selected);
            completeTaskBtn.setEnabled(selected && !tasks.get(tasksTable.getSelectedRow()).completed);
        });
    }

    private void openContactDialog(Contact contact) {
        JDialog dialog = new JDialog(this, (contact == null) ? "Agregar Contacto" : "Modificar Contacto", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel nameLabel = new JLabel("Nombre:");
        JLabel phoneLabel = new JLabel("Teléfono:");
        JLabel emailLabel = new JLabel("Correo electrónico:");

        JTextField nameField = new JTextField(25);
        JTextField phoneField = new JTextField(25);
        JTextField emailField = new JTextField(25);

        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        phoneField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        if (contact != null) {
            nameField.setText(contact.name);
            phoneField.setText(contact.phone);
            emailField.setText(contact.email);
        }

        c.insets = new Insets(8, 10, 8, 10);
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_END;
        panel.add(nameLabel, c);
        c.gridy++;
        panel.add(phoneLabel, c);
        c.gridy++;
        panel.add(emailLabel, c);

        c.gridx = 1; c.gridy = 0; c.anchor = GridBagConstraints.LINE_START;
        panel.add(nameField, c);
        c.gridy++;
        panel.add(phoneField, c);
        c.gridy++;
        panel.add(emailField, c);

        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        c.gridx = 0; c.gridy++; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        panel.add(btnPanel, c);

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (contact == null) {
                contacts.add(new Contact(name, phone, email));
            } else {
                contact.name = name;
                contact.phone = phone;
                contact.email = email;
            }
            refreshContactsTable();
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openTaskDialog(Task task) {
        JDialog dialog = new JDialog(this, (task == null) ? "Agregar Tarea" : "Modificar Tarea", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Título:");
        JLabel descriptionLabel = new JLabel("Descripción:");
        JLabel dueDateLabel = new JLabel("Fecha de vencimiento (YYYY-MM-DD):");

        JTextField titleField = new JTextField(30);
        JTextArea descriptionArea = new JTextArea(6, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JTextField dueDateField = new JTextField(20);
        dueDateField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        titleField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        if (task != null) {
            titleField.setText(task.title);
            descriptionArea.setText(task.description);
            dueDateField.setText(task.dueDate.format(dateFormatter));
        }

        c.insets = new Insets(8, 10, 8, 10);
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_END;
        panel.add(titleLabel, c);
        c.gridy++;
        panel.add(descriptionLabel, c);
        c.gridy++;
        panel.add(dueDateLabel, c);

        c.gridx = 1; c.gridy = 0; c.anchor = GridBagConstraints.LINE_START;
        panel.add(titleField, c);
        c.gridy++;
        panel.add(descriptionScroll, c);
        c.gridy++;
        panel.add(dueDateField, c);

        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        c.gridx = 0; c.gridy++; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        panel.add(btnPanel, c);

        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String dateStr = dueDateField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "El título es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalDate dueDate;
            try {
                dueDate = LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (task == null) {
                tasks.add(new Task(title, description, dueDate));
            } else {
                task.title = title;
                task.description = description;
                task.dueDate = dueDate;
            }
            refreshTasksTable();
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void refreshContactsTable() {
        contactsTableModel.setRowCount(0);
        for (Contact c : contacts) {
            contactsTableModel.addRow(new Object[]{c.name, c.phone, c.email});
        }
    }

    private void refreshTasksTable() {
        tasksTableModel.setRowCount(0);
        for (Task t : tasks) {
            tasksTableModel.addRow(new Object[]{
                    t.title,
                    t.description,
                    t.dueDate.format(dateFormatter),
                    t.completed ? "Completada" : "Pendiente"
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AgendaGUIApp());
    }

    // Data classes
    static class Contact {
        String name;
        String phone;
        String email;

        Contact(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }
    }

    static class Task {
        String title;
        String description;
        LocalDate dueDate;
        boolean completed;

        Task(String title, String description, LocalDate dueDate) {
            this.title = title;
            this.description = description;
            this.dueDate = dueDate;
            this.completed = false;
        }
    }
}
