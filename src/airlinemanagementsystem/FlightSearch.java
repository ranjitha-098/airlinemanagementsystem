package airlinemanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import net.proteanit.sql.DbUtils;

public class FlightSearch extends JFrame implements ActionListener {

    private Choice sourceChoice;
    private Choice destinationChoice;
    private JTable table;
    private JButton searchButton;
    private JButton resetButton;

    public FlightSearch() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        setTitle("Search Flights");

        JLabel heading = new JLabel("Search Available Flights");
        heading.setFont(new Font("Tahoma", Font.PLAIN, 24));
        heading.setForeground(Color.BLUE);
        heading.setBounds(250, 20, 400, 30);
        add(heading);

        JLabel sourceLabel = new JLabel("Source");
        sourceLabel.setBounds(60, 80, 100, 25);
        sourceLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(sourceLabel);

        sourceChoice = new Choice();
        sourceChoice.setBounds(160, 80, 180, 25);
        add(sourceChoice);

        JLabel destinationLabel = new JLabel("Destination");
        destinationLabel.setBounds(370, 80, 120, 25);
        destinationLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(destinationLabel);

        destinationChoice = new Choice();
        destinationChoice.setBounds(500, 80, 180, 25);
        add(destinationChoice);

        searchButton = new JButton("Search");
        searchButton.setBackground(Color.BLACK);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBounds(700, 80, 100, 25);
        searchButton.addActionListener(this);
        add(searchButton);

        resetButton = new JButton("Reset");
        resetButton.setBackground(Color.DARK_GRAY);
        resetButton.setForeground(Color.WHITE);
        resetButton.setBounds(820, 80, 100, 25);
        resetButton.addActionListener(this);
        add(resetButton);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(40, 140, 880, 400);
        add(scrollPane);

        populateChoices();
        loadFlights(null, null);

        setSize(980, 620);
        setLocation(300, 120);
        setVisible(true);
    }

    private void populateChoices() {
        sourceChoice.add("All");
        destinationChoice.add("All");

        try {
            Conn conn = new Conn();
            ResultSet rsSource = conn.s.executeQuery("select distinct source from flight order by source");
            while (rsSource.next()) {
                sourceChoice.add(rsSource.getString("source"));
            }

            ResultSet rsDestination = conn.s.executeQuery("select distinct destination from flight order by destination");
            while (rsDestination.next()) {
                destinationChoice.add(rsDestination.getString("destination"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unable to load flight data. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadFlights(String source, String destination) {
        List<String> filters = new ArrayList<>();
        List<String> parameters = new ArrayList<>();

        if (source != null) {
            filters.add("source = ?");
            parameters.add(source);
        }

        if (destination != null) {
            filters.add("destination = ?");
            parameters.add(destination);
        }

        StringBuilder queryBuilder = new StringBuilder("select * from flight");
        if (!filters.isEmpty()) {
            queryBuilder.append(" where ");
            queryBuilder.append(String.join(" and ", filters));
        }
        queryBuilder.append(" order by source, destination, f_name");

        try {
            Conn conn = new Conn();
            PreparedStatement ps = conn.c.prepareStatement(queryBuilder.toString());
            for (int i = 0; i < parameters.size(); i++) {
                ps.setString(i + 1, parameters.get(i));
            }
            ResultSet rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                table.setModel(new DefaultTableModel());
                JOptionPane.showMessageDialog(this, "No flights found for the selected filters.", "No Results", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            table.setModel(new DefaultTableModel());
            JOptionPane.showMessageDialog(this, "Unable to fetch flights. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String selectedSource = sourceChoice.getSelectedItem();
            String selectedDestination = destinationChoice.getSelectedItem();

            String src = "All".equals(selectedSource) ? null : selectedSource;
            String dest = "All".equals(selectedDestination) ? null : selectedDestination;
            loadFlights(src, dest);
        } else if (e.getSource() == resetButton) {
            sourceChoice.select("All");
            destinationChoice.select("All");
            loadFlights(null, null);
        }
    }

    public static void main(String[] args) {
        new FlightSearch();
    }
}

