/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollment.java;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public final class Enrollment extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    
    private final ArrayList<String> studentList = new ArrayList<>();

    
    private final HashMap<String, Boolean> hashmap = new HashMap<>();

    private final JTextField studentField = new JTextField(18);
    private final JTextArea listArea = new JTextArea(18, 40);

    private final JButton enrollButton = new JButton("Enroll");
    private final JButton withdrawButton = new JButton("Withdraw");
    private final JButton doneButton = new JButton("Mark as Done");
    private final JButton viewButton = new JButton("View Enrolled");

    public Enrollment() {
        super("Student Enrollment System");

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);

        
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        JLabel titleLabel = new JLabel("Student Enrollment System");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        panel.add(titleLabel, c);

       
        c.gridy = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        studentField.setFont(studentField.getFont().deriveFont(15f));
        panel.add(studentField, c);

        c.gridx = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        JLabel promptLabel = new JLabel("Enter Student Name");
        promptLabel.setFont(promptLabel.getFont().deriveFont(Font.BOLD, 15f));
        panel.add(promptLabel, c);

        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 12));
        JButton[] buttons = {enrollButton, withdrawButton, doneButton, viewButton};
        for (JButton button : buttons) {
            button.setPreferredSize(new Dimension(180, 38));
            buttonPanel.add(button);
        }
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        panel.add(buttonPanel, c);

        
        listArea.setEditable(false);
        listArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(listArea);

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, c);

        add(panel);

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void addListeners() {
        enrollButton.addActionListener(this);
        studentField.addActionListener(this); 
        withdrawButton.addActionListener(this);
        doneButton.addActionListener(this);
        viewButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == enrollButton || source == studentField) {
            enroll();
        } else if (source == withdrawButton) {
            withdraw();
        } else if (source == doneButton) {
            markAsDone();
        } else if (source == viewButton) {
            viewEnrolled();
        }
    }

    private void enroll() {
        String target = getStudent();
        if (target == null) {
            return;
        }

        int index = searchStudent(target);

        if (index != -1) {
            showMessage("\"" + target + "\" is already enrolled.");
        } else {
            studentList.add(target);
            hashmap.put(target, false);
            studentField.setText("");
            showMessage("\"" + target + "\" has been enrolled.");
        }
        listArea.setText(""); 
    }

    private void withdraw() {
        String target = getStudent();
        if (target == null) {
            return;
        }

        int index = searchStudent(target);

        if (index == -1) {
            showMessage("\"" + target + "\" is not enrolled.");
        } else {
            studentList.remove(index);
            hashmap.remove(target);
            studentField.setText("");
            showMessage("\"" + target + "\" has been withdrawn.");
        }
        listArea.setText(""); 
    }

    private void markAsDone() {
        String target = getStudent();
        if (target == null) {
            return;
        }

        int index = searchStudent(target);

        if (index == -1) {
            showMessage("\"" + target + "\" is not enrolled.");
        } else {
            hashmap.put(target, true);
            studentField.setText("");
            showMessage("\"" + target + "\" has been marked as done.");
        }
        listArea.setText(""); 
    }

    private int searchStudent(String target) {

        String[] data = studentList.toArray(new String[0]);

        int linearResults = linearSearch(data, target);

        String[] sortedData = Arrays.copyOf(data, data.length);
        Arrays.sort(sortedData);

        int binaryResult = binarySearch(sortedData, target);

        boolean hashResult = hashmap.containsKey(target);

        if (binaryResult == -1 || !hashResult) {
            return -1;
        }
        return linearResults;
    }

    public static int linearSearch(String[] data, String target) {

        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(target)) {
                return i;
            }
        }

        return -1;

    }

    public static int binarySearch(String[] data, String target) {

        int left = 0, right = data.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int compare = data[mid].compareTo(target);
            if (compare == 0) {
                return mid;
            } else if (compare < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;

    }

    private void viewEnrolled() {
        StringBuilder sb = new StringBuilder();

        if (studentList.isEmpty()) {
            sb.append("No students enrolled.\nType a name and click Enroll.\n");
        } else {
            for (int i = 0; i < studentList.size(); i++) {
                String student = studentList.get(i);
                boolean isDone = hashmap.getOrDefault(student, false);

                sb.append(i + 1).append(". ").append(student);
                if (isDone) {
                    sb.append(" (Done)");
                }
                sb.append("\n");
            }
        }

        listArea.setText(sb.toString());
        listArea.setCaretPosition(0);
    }

    private String getStudent() {
        String student = studentField.getText().trim();
        if (student.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Type a student name first.",
                    "Name needed", JOptionPane.WARNING_MESSAGE);
            studentField.requestFocus();
            return null;
        }
        return student;
    }

    
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Student Enrollment System", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Enrollment enrollment = new Enrollment();
            enrollment.addListeners();
            enrollment.setVisible(true);
        });
    }
}
