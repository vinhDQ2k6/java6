package com.sof3062.view;

import com.sof3062.model.Student;
import com.sof3062.service.StudentService;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentFrame extends JFrame {

  private JTextField txtId;
  private JTextField txtName;
  private JTextField txtMark;
  private JRadioButton rdoMale;
  private JRadioButton rdoFemale;
  private ButtonGroup grpGender;
  private JTable table;
  private DefaultTableModel tableModel;

  private final StudentService service = new StudentService();

  public StudentFrame() {
    initComponents();
    loadTable();
  }

  private void initComponents() {
    setTitle("Quản lý sinh viên");
    setSize(600, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout(10, 10));

    // --- Form Panel ---
    JPanel pnlForm = new JPanel(new GridBagLayout());
    pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // ID
    gbc.gridx = 0;
    gbc.gridy = 0;
    pnlForm.add(new JLabel("Id"), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0.3;
    txtId = new JTextField();
    pnlForm.add(txtId, gbc);

    // Name
    gbc.gridx = 1;
    gbc.gridy = 0;
    pnlForm.add(new JLabel("Full Name"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 0.7;
    txtName = new JTextField();
    pnlForm.add(txtName, gbc);

    // Gender
    gbc.gridx = 0;
    gbc.gridy = 2;
    pnlForm.add(new JLabel("Gender"), gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    JPanel pnlGender = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    rdoMale = new JRadioButton("Male");
    rdoFemale = new JRadioButton("Female");
    grpGender = new ButtonGroup();
    grpGender.add(rdoMale);
    grpGender.add(rdoFemale);
    rdoFemale.setSelected(true);
    pnlGender.add(rdoMale);
    pnlGender.add(rdoFemale);
    pnlForm.add(pnlGender, gbc);

    // Mark
    gbc.gridx = 1;
    gbc.gridy = 2;
    pnlForm.add(new JLabel("Average Mark"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    txtMark = new JTextField();
    pnlForm.add(txtMark, gbc);

    add(pnlForm, BorderLayout.NORTH);

    // --- Center Panel (Buttons + Table) ---
    JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
    pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

    // Buttons
    JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    JButton btnCreate = new JButton("Create");
    JButton btnUpdate = new JButton("Update");
    JButton btnDelete = new JButton("Delete");
    JButton btnReset = new JButton("Reset");

    pnlButtons.add(btnCreate);
    pnlButtons.add(btnUpdate);
    pnlButtons.add(btnDelete);
    pnlButtons.add(btnReset);

    pnlCenter.add(pnlButtons, BorderLayout.NORTH);

    // Table
    String[] columns = { "Id", "Full Name", "Gender", "Mark" };
    tableModel = new DefaultTableModel(columns, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    table = new JTable(tableModel);
    table.setRowHeight(25);
    pnlCenter.add(new JScrollPane(table), BorderLayout.CENTER);

    add(pnlCenter, BorderLayout.CENTER);

    // --- Events ---
    btnCreate.addActionListener(e -> create());
    btnUpdate.addActionListener(e -> update());
    btnDelete.addActionListener(e -> delete());
    btnReset.addActionListener(e -> reset());

    table.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 1) {
            edit();
          }
        }
      }
    );
  }

  private void loadTable() {
    tableModel.setRowCount(0);
    new Thread(() -> {
      List<Student> list = service.findAll();
      SwingUtilities.invokeLater(() -> {
        for (Student s : list) {
          tableModel.addRow(
            new Object[] {
              s.getId(),
              s.getName(),
              s.getGender() ? "Male" : "Female",
              s.getMark(),
            }
          );
        }
      });
    }).start();
  }

  private Student getForm() {
    String id = txtId.getText().trim();
    String name = txtName.getText().trim();
    String markStr = txtMark.getText().trim();
    boolean gender = rdoMale.isSelected();

    if (id.isEmpty() || name.isEmpty() || markStr.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Please fill all fields!");
      return null;
    }

    try {
      Double mark = Double.parseDouble(markStr);
      return new Student(id, name, mark, gender);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Mark must be a number!");
      return null;
    }
  }

  private void setForm(Student s) {
    txtId.setText(s.getId());
    txtName.setText(s.getName());
    txtMark.setText(String.valueOf(s.getMark()));
    if (s.getGender() != null && s.getGender()) {
      rdoMale.setSelected(true);
    } else {
      rdoFemale.setSelected(true);
    }
  }

  private void create() {
    Student s = getForm();
    if (s != null) {
      new Thread(() -> {
        service.create(s);
        SwingUtilities.invokeLater(() -> {
          JOptionPane.showMessageDialog(this, "Created successfully!");
          loadTable();
          reset();
        });
      }).start();
    }
  }

  private void update() {
    Student s = getForm();
    if (s != null) {
      new Thread(() -> {
        service.update(s);
        SwingUtilities.invokeLater(() -> {
          JOptionPane.showMessageDialog(this, "Updated successfully!");
          loadTable();
          reset();
        });
      }).start();
    }
  }

  private void delete() {
    String id = txtId.getText().trim();
    if (id.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Please enter ID to delete!");
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(
      this,
      "Are you sure to delete " + id + "?"
    );
    if (confirm == JOptionPane.YES_OPTION) {
      new Thread(() -> {
        service.delete(id);
        SwingUtilities.invokeLater(() -> {
          JOptionPane.showMessageDialog(this, "Deleted successfully!");
          loadTable();
          reset();
        });
      }).start();
    }
  }

  private void reset() {
    txtId.setText("");
    txtName.setText("");
    txtMark.setText("");
    rdoFemale.setSelected(true);
    txtId.requestFocus();
  }

  private void edit() {
    int row = table.getSelectedRow();
    if (row >= 0) {
      String id = (String) table.getValueAt(row, 0);
      String name = (String) table.getValueAt(row, 1);
      String genderStr = (String) table.getValueAt(row, 2);
      Double mark = (Double) table.getValueAt(row, 3);

      Student s = new Student(id, name, mark, genderStr.equals("Male"));
      setForm(s);
    }
  }
}
