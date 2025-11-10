package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import net.proteanit.sql.DbUtils;

public class JourneyDetails extends JFrame implements ActionListener{
    JTable table;
    JTextField pnr;
    JTextField aadhar;
    JButton show;
    JButton showByAadhar;
    JButton showAll;
    
    public JourneyDetails() {
        
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        JLabel lblpnr = new JLabel("PNR Details");
        lblpnr.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblpnr.setBounds(50, 50, 100, 25);
        add(lblpnr);
        
        pnr = new JTextField();
        pnr.setBounds(160, 50, 120, 25);
        add(pnr);
        
        show = new JButton("Show Details");
        show.setBackground(Color.BLACK);
        show.setForeground(Color.WHITE);
        show.setBounds(290, 50, 120, 25);
        show.addActionListener(this);
        add(show);

        JLabel lblaadhar = new JLabel("Aadhar");
        lblaadhar.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblaadhar.setBounds(450, 50, 100, 25);
        add(lblaadhar);

        aadhar = new JTextField();
        aadhar.setBounds(540, 50, 130, 25);
        add(aadhar);

        showByAadhar = new JButton("Show For Aadhar");
        showByAadhar.setBackground(Color.BLACK);
        showByAadhar.setForeground(Color.WHITE);
        showByAadhar.setBounds(690, 50, 150, 25);
        showByAadhar.addActionListener(this);
        add(showByAadhar);

        showAll = new JButton("Show All Journeys");
        showAll.setBackground(Color.DARK_GRAY);
        showAll.setForeground(Color.WHITE);
        showAll.setBounds(540, 90, 300, 25);
        showAll.addActionListener(this);
        add(showAll);
        
        table = new JTable();
        
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 140, 900, 180);
        jsp.setBackground(Color.WHITE);
        add(jsp);
        
        setSize(920, 620);
        setLocation(360, 120);
        setVisible(true);
        loadAllReservations();
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == show) {
            String pnrValue = pnr.getText().trim();
            if (pnrValue.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a PNR to search.");
                return;
            }
            loadReservations("select * from reservation where PNR = ?", pnrValue, "No information found for the provided PNR.");
        } else if (ae.getSource() == showByAadhar) {
            String aadharValue = aadhar.getText().trim();
            if (aadharValue.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter an Aadhar number to search.");
                return;
            }
            loadReservations("select * from reservation where aadhar = ?", aadharValue, "No journeys found for the provided Aadhar number.");
        } else if (ae.getSource() == showAll) {
            loadAllReservations();
        }
    }

    private void loadReservations(String query, String parameter, String emptyMessage) {
        try {
            Conn conn = new Conn();
            PreparedStatement ps = conn.c.prepareStatement(query);
            ps.setString(1, parameter);
            ResultSet rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                table.setModel(new javax.swing.table.DefaultTableModel());
                JOptionPane.showMessageDialog(null, emptyMessage);
                return;
            }
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to fetch journey details. Please try again.");
            e.printStackTrace();
        }
    }

    private void loadAllReservations() {
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery("select * from reservation order by aadhar, PNR");
            if (!rs.isBeforeFirst()) {
                table.setModel(new javax.swing.table.DefaultTableModel());
                JOptionPane.showMessageDialog(null, "No journey records available.");
                return;
            }
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to load journeys. Please try again.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new JourneyDetails();
    }
}
