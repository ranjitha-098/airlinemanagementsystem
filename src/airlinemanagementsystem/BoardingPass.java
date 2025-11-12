package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BoardingPass extends JFrame implements ActionListener{
    
    JTextField tfpnr;
    JLabel tfname, tfnationality, lblsrc, lbldest, labelfname, labelfcode, labeldate;
    JButton fetchButton;
    
    public BoardingPass() {
        getContentPane().setBackground(new Color(245, 245, 250));
        setLayout(null);
        
        // Header Section
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 900, 80);
        headerPanel.setBackground(new Color(0, 51, 102));
        headerPanel.setLayout(null);
        add(headerPanel);
        
        JLabel heading = new JLabel("AIR INDIA");
        heading.setBounds(20, 10, 200, 30);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(Color.WHITE);
        headerPanel.add(heading);
        
        JLabel subheading = new JLabel("Boarding Pass");
        subheading.setBounds(20, 40, 200, 25);
        subheading.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subheading.setForeground(new Color(200, 220, 255));
        headerPanel.add(subheading);
        
        // PNR Input Section
        JPanel inputPanel = new JPanel();
        inputPanel.setBounds(20, 100, 860, 60);
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setLayout(null);
        inputPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        add(inputPanel);
        
        JLabel lblaadhar = new JLabel("Enter PNR Number:");
        lblaadhar.setBounds(20, 18, 150, 25);
        lblaadhar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(lblaadhar);
        
        tfpnr = new JTextField();
        tfpnr.setBounds(180, 15, 200, 30);
        tfpnr.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfpnr.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(tfpnr);
        
        fetchButton = new JButton("Get Boarding Pass");
        fetchButton.setBackground(new Color(0, 102, 204));
        fetchButton.setForeground(Color.WHITE);
        fetchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fetchButton.setBounds(400, 15, 180, 30);
        fetchButton.setFocusPainted(false);
        fetchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fetchButton.addActionListener(this);
        inputPanel.add(fetchButton);
        
        // Passenger Information Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(20, 180, 860, 350);
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(null);
        infoPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        add(infoPanel);
        
        JLabel infoHeading = new JLabel("Passenger & Flight Information");
        infoHeading.setBounds(20, 15, 400, 30);
        infoHeading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        infoHeading.setForeground(new Color(0, 51, 102));
        infoPanel.add(infoHeading);
        
        // Left Column
        int startY = 60;
        int labelWidth = 120;
        int valueWidth = 200;
        int spacing = 40;
        
        addInfoRow(infoPanel, "Passenger Name:", tfname = new JLabel(""), 20, startY, labelWidth, valueWidth);
        addInfoRow(infoPanel, "Nationality:", tfnationality = new JLabel(""), 20, startY + spacing, labelWidth, valueWidth);
        addInfoRow(infoPanel, "Source:", lblsrc = new JLabel(""), 20, startY + 2*spacing, labelWidth, valueWidth);
        addInfoRow(infoPanel, "Destination:", lbldest = new JLabel(""), 20, startY + 3*spacing, labelWidth, valueWidth);
        
        // Right Column
        addInfoRow(infoPanel, "Flight Name:", labelfname = new JLabel(""), 450, startY, labelWidth, valueWidth);
        addInfoRow(infoPanel, "Flight Code:", labelfcode = new JLabel(""), 450, startY + spacing, labelWidth, valueWidth);
        addInfoRow(infoPanel, "Date:", labeldate = new JLabel(""), 450, startY + 2*spacing, labelWidth, valueWidth);
        
        // Airline Logo
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/airindia.png"));
        if (i1.getIconWidth() > 0) {
            Image i2 = i1.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
            ImageIcon image = new ImageIcon(i2);
            JLabel lblimage = new JLabel(image);
            lblimage.setBounds(750, 20, 120, 90);
            infoPanel.add(lblimage);
        }
        
        setSize(920, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    
    private void addInfoRow(JPanel panel, String labelText, JLabel valueLabel, int x, int y, int labelWidth, int valueWidth) {
        JLabel label = new JLabel(labelText);
        label.setBounds(x, y, labelWidth, 25);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(100, 100, 100));
        panel.add(label);
        
        valueLabel.setBounds(x + labelWidth + 10, y, valueWidth, 25);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(new Color(0, 51, 102));
        valueLabel.setText("---");
        panel.add(valueLabel);
    }
    
    public void actionPerformed(ActionEvent ae) {
        String pnr = tfpnr.getText().trim();
        
        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a PNR number", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Conn conn = new Conn();
            String query = "SELECT * FROM reservation WHERE PNR = '" + pnr + "'";
            ResultSet rs = conn.s.executeQuery(query);

            if (rs.next()) {
                tfname.setText(rs.getString("name")); 
                tfnationality.setText(rs.getString("nationality")); 
                lblsrc.setText(rs.getString("src")); 
                lbldest.setText(rs.getString("des"));  
                labelfname.setText(rs.getString("flightname"));  
                labelfcode.setText(rs.getString("flightcode"));  
                labeldate.setText(rs.getString("ddate")); 
            } else {
                JOptionPane.showMessageDialog(this, "PNR not found. Please check and try again.", "Invalid PNR", JOptionPane.ERROR_MESSAGE);
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error retrieving boarding pass: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void clearFields() {
        tfname.setText("---");
        tfnationality.setText("---");
        lblsrc.setText("---");
        lbldest.setText("---");
        labelfname.setText("---");
        labelfcode.setText("---");
        labeldate.setText("---");
    }

    public static void main(String[] args) {
        new BoardingPass();
    }
}
