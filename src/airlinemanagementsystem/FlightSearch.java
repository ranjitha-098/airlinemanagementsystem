package airlinemanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import net.proteanit.sql.DbUtils;

public class FlightSearch extends JFrame implements ActionListener {

    private Choice sourceChoice;
    private Choice destinationChoice;
    private JTextField flightNameField;
    private JTextField flightCodeField;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JDateChooser dateChooser;
    private Choice sortChoice;
    private JTable table;
    private JButton searchButton;
    private JButton resetButton;
    private JLabel statusLabel;
    private TableRowSorter<DefaultTableModel> sorter;
    
    // Store current search parameters for re-sorting
    private String currentSource;
    private String currentDestination;
    private String currentFlightName;
    private String currentFlightCode;
    private Double currentMinPrice;
    private Double currentMaxPrice;
    private String currentDate;

    public FlightSearch() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        setTitle("Search Flights - Advanced Search");

        // Heading
        JLabel heading = new JLabel("Search Available Flights");
        heading.setFont(new Font("Tahoma", Font.BOLD, 26));
        heading.setForeground(new Color(0, 102, 204));
        heading.setBounds(300, 15, 400, 35);
        add(heading);

        // Row 1: Source and Destination
        JLabel sourceLabel = new JLabel("Source:");
        sourceLabel.setBounds(40, 70, 80, 25);
        sourceLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(sourceLabel);

        sourceChoice = new Choice();
        sourceChoice.setBounds(130, 70, 180, 25);
        add(sourceChoice);

        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setBounds(330, 70, 90, 25);
        destinationLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(destinationLabel);

        destinationChoice = new Choice();
        destinationChoice.setBounds(430, 70, 180, 25);
        add(destinationChoice);

        // Row 2: Flight Name and Code
        JLabel flightNameLabel = new JLabel("Flight Name:");
        flightNameLabel.setBounds(40, 110, 90, 25);
        flightNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(flightNameLabel);

        flightNameField = new JTextField();
        flightNameField.setBounds(130, 110, 180, 25);
        flightNameField.setToolTipText("Search by flight name (partial match)");
        add(flightNameField);

        JLabel flightCodeLabel = new JLabel("Flight Code:");
        flightCodeLabel.setBounds(330, 110, 90, 25);
        flightCodeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(flightCodeLabel);

        flightCodeField = new JTextField();
        flightCodeField.setBounds(430, 110, 180, 25);
        flightCodeField.setToolTipText("Search by flight code (partial match)");
        add(flightCodeField);

        // Row 3: Price Range and Date
        JLabel minPriceLabel = new JLabel("Min Price:");
        minPriceLabel.setBounds(40, 150, 80, 25);
        minPriceLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(minPriceLabel);

        minPriceField = new JTextField();
        minPriceField.setBounds(130, 150, 100, 25);
        minPriceField.setToolTipText("Minimum price filter");
        add(minPriceField);

        JLabel maxPriceLabel = new JLabel("Max Price:");
        maxPriceLabel.setBounds(240, 150, 80, 25);
        maxPriceLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(maxPriceLabel);

        maxPriceField = new JTextField();
        maxPriceField.setBounds(330, 150, 100, 25);
        maxPriceField.setToolTipText("Maximum price filter");
        add(maxPriceField);

        JLabel dateLabel = new JLabel("Departure Date:");
        dateLabel.setBounds(450, 150, 110, 25);
        dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(dateLabel);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(570, 150, 150, 25);
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setToolTipText("Filter by departure date");
        add(dateChooser);

        // Row 4: Sort By
        JLabel sortLabel = new JLabel("Sort By:");
        sortLabel.setBounds(40, 190, 80, 25);
        sortLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(sortLabel);

        sortChoice = new Choice();
        sortChoice.add("Default (Source, Destination, Name)");
        sortChoice.add("Flight Name (A-Z)");
        sortChoice.add("Flight Name (Z-A)");
        sortChoice.add("Flight Code (A-Z)");
        sortChoice.add("Flight Code (Z-A)");
        sortChoice.add("Source (A-Z)");
        sortChoice.add("Source (Z-A)");
        sortChoice.add("Destination (A-Z)");
        sortChoice.add("Destination (Z-A)");
        sortChoice.add("Price (Low to High)");
        sortChoice.add("Price (High to Low)");
        sortChoice.setBounds(130, 190, 280, 25);
        sortChoice.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    applySortingToCurrentResults();
                }
            }
        });
        add(sortChoice);

        // Buttons
        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0, 102, 204));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        searchButton.setBounds(750, 150, 100, 35);
        searchButton.addActionListener(this);
        add(searchButton);

        resetButton = new JButton("Reset");
        resetButton.setBackground(new Color(102, 102, 102));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        resetButton.setBounds(860, 150, 100, 35);
        resetButton.addActionListener(this);
        add(resetButton);

        // Status Label
        statusLabel = new JLabel("Ready to search");
        statusLabel.setBounds(40, 240, 400, 20);
        statusLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
        statusLabel.setForeground(Color.GRAY);
        add(statusLabel);

        // Table
        table = new JTable();
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(40, 270, 920, 380);
        add(scrollPane);

        populateChoices();
        loadFlights(null, null, null, null, null, null, null);

        setSize(1000, 700);
        setLocation(250, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    private void loadFlights(String source, String destination, String flightName, String flightCode, 
                             Double minPrice, Double maxPrice, String date) {
        // Store current search parameters
        currentSource = source;
        currentDestination = destination;
        currentFlightName = flightName;
        currentFlightCode = flightCode;
        currentMinPrice = minPrice;
        currentMaxPrice = maxPrice;
        currentDate = date;
        
        statusLabel.setText("Searching...");
        statusLabel.setForeground(new Color(0, 102, 204));
        
        List<String> filters = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        if (source != null && !source.isEmpty()) {
            filters.add("source = ?");
            parameters.add(source);
        }

        if (destination != null && !destination.isEmpty()) {
            filters.add("destination = ?");
            parameters.add(destination);
        }

        if (flightName != null && !flightName.trim().isEmpty()) {
            filters.add("f_name LIKE ?");
            parameters.add("%" + flightName.trim() + "%");
        }

        if (flightCode != null && !flightCode.trim().isEmpty()) {
            filters.add("f_code LIKE ?");
            parameters.add("%" + flightCode.trim() + "%");
        }

        if (minPrice != null) {
            filters.add("price >= ?");
            parameters.add(minPrice);
        }

        if (maxPrice != null) {
            filters.add("price <= ?");
            parameters.add(maxPrice);
        }

        if (date != null && !date.isEmpty()) {
            filters.add("DATE(departure_date) = ?");
            parameters.add(date);
        }

        StringBuilder queryBuilder = new StringBuilder("select * from flight");
        if (!filters.isEmpty()) {
            queryBuilder.append(" where ");
            queryBuilder.append(String.join(" and ", filters));
        }
        
        // Get sorting order from sortChoice
        String orderBy = getSortOrder();
        queryBuilder.append(" ").append(orderBy);

        try {
            Conn conn = new Conn();
            PreparedStatement ps = conn.c.prepareStatement(queryBuilder.toString());
            
            for (int i = 0; i < parameters.size(); i++) {
                Object param = parameters.get(i);
                if (param instanceof Double) {
                    ps.setDouble(i + 1, (Double) param);
                } else {
                    ps.setString(i + 1, param.toString());
                }
            }
            
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) DbUtils.resultSetToTableModel(rs);
            table.setModel(model);

            // Update status
            int rowCount = model.getRowCount();
            if (rowCount == 0) {
                statusLabel.setText("No flights found matching your criteria.");
                statusLabel.setForeground(Color.RED);
            } else {
                statusLabel.setText("Found " + rowCount + " flight(s)");
                statusLabel.setForeground(new Color(0, 150, 0));
            }

            // Auto-resize columns
            resizeTableColumns();
            
        } catch (Exception e) {
            table.setModel(new DefaultTableModel());
            statusLabel.setText("Error: Unable to fetch flights. Please try again.");
            statusLabel.setForeground(Color.RED);
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private String getSortOrder() {
        String sortOption = sortChoice.getSelectedItem();
        switch (sortOption) {
            case "Flight Name (A-Z)":
                return "order by f_name ASC";
            case "Flight Name (Z-A)":
                return "order by f_name DESC";
            case "Flight Code (A-Z)":
                return "order by f_code ASC";
            case "Flight Code (Z-A)":
                return "order by f_code DESC";
            case "Source (A-Z)":
                return "order by source ASC";
            case "Source (Z-A)":
                return "order by source DESC";
            case "Destination (A-Z)":
                return "order by destination ASC";
            case "Destination (Z-A)":
                return "order by destination DESC";
            case "Price (Low to High)":
                return "order by price ASC";
            case "Price (High to Low)":
                return "order by price DESC";
            default:
                return "order by source, destination, f_name";
        }
    }
    
    private void applySortingToCurrentResults() {
        // Re-apply search with new sorting - always re-query to apply sorting
        loadFlights(currentSource, currentDestination, currentFlightName, currentFlightCode, 
                   currentMinPrice, currentMaxPrice, currentDate);
    }


    private void resizeTableColumns() {
        if (table.getColumnCount() > 0) {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(150);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            // Validation
            String selectedSource = sourceChoice.getSelectedItem();
            String selectedDestination = destinationChoice.getSelectedItem();

            if (!"All".equals(selectedSource) && !"All".equals(selectedDestination) 
                && selectedSource.equals(selectedDestination)) {
                JOptionPane.showMessageDialog(this, 
                    "Source and destination cannot be the same!", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            String src = "All".equals(selectedSource) ? null : selectedSource;
            String dest = "All".equals(selectedDestination) ? null : selectedDestination;
            
            String flightName = flightNameField.getText().trim();
            String flightCode = flightCodeField.getText().trim();
            
            Double minPrice = null;
            Double maxPrice = null;
            
            try {
                String minPriceText = minPriceField.getText().trim();
                if (!minPriceText.isEmpty()) {
                    minPrice = Double.parseDouble(minPriceText);
                    if (minPrice < 0) {
                        JOptionPane.showMessageDialog(this, "Price cannot be negative!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid minimum price format!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                String maxPriceText = maxPriceField.getText().trim();
                if (!maxPriceText.isEmpty()) {
                    maxPrice = Double.parseDouble(maxPriceText);
                    if (maxPrice < 0) {
                        JOptionPane.showMessageDialog(this, "Price cannot be negative!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid maximum price format!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
                JOptionPane.showMessageDialog(this, "Minimum price cannot be greater than maximum price!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String date = null;
            if (dateChooser.getDate() != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                date = sdf.format(dateChooser.getDate());
            }
            
            String flightNameParam = flightName.isEmpty() ? null : flightName;
            String flightCodeParam = flightCode.isEmpty() ? null : flightCode;
            
            loadFlights(src, dest, flightNameParam, flightCodeParam, minPrice, maxPrice, date);
            
        } else if (e.getSource() == resetButton) {
            sourceChoice.select("All");
            destinationChoice.select("All");
            flightNameField.setText("");
            flightCodeField.setText("");
            minPriceField.setText("");
            maxPriceField.setText("");
            dateChooser.setDate(null);
            sortChoice.select("Default (Source, Destination, Name)");
            loadFlights(null, null, null, null, null, null, null);
        }
    }

    public static void main(String[] args) {
        new FlightSearch();
    }
}
