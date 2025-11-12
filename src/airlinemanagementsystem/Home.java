package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Home extends JFrame implements ActionListener{
    
    JButton btnBookFlight, btnSearchFlights, btnFlightDetails, btnAddCustomer, 
            btnJourneyDetails, btnCancelTicket, btnBoardingPass;
    
    public Home() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 248, 255));
        
        // Background Image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/front.jpg"));
        JLabel image = new JLabel(i1);
        image.setBounds(0, 0, 1600, 800);
        add(image);
        
        // Semi-transparent overlay panel for better readability
        JPanel overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        overlayPanel.setBounds(0, 0, 1600, 150);
        overlayPanel.setOpaque(false);
        overlayPanel.setLayout(null);
        image.add(overlayPanel);
        
        // Main Heading with better styling
        JLabel heading = new JLabel("AIR INDIA WELCOMES YOU");
        heading.setBounds(400, 20, 1200, 50);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 48));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        overlayPanel.add(heading);
        
        // Tagline
        JLabel tagline = new JLabel("Your Journey, Our Commitment");
        tagline.setBounds(400, 75, 1200, 30);
        tagline.setForeground(new Color(255, 255, 200));
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        tagline.setHorizontalAlignment(SwingConstants.CENTER);
        overlayPanel.add(tagline);
        
        // Quick Action Panel
        JPanel quickActionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        quickActionPanel.setBounds(200, 200, 1200, 500);
        quickActionPanel.setOpaque(false);
        quickActionPanel.setLayout(null);
        quickActionPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 3));
        image.add(quickActionPanel);
        
        // Quick Actions Label
        JLabel quickActionsLabel = new JLabel("Quick Actions");
        quickActionsLabel.setBounds(0, 20, 1200, 40);
        quickActionsLabel.setForeground(new Color(0, 51, 102));
        quickActionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        quickActionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quickActionPanel.add(quickActionsLabel);
        
        // Button styling
        int buttonWidth = 250;
        int buttonHeight = 60;
        int startX = 100;
        int startY = 100;
        int spacingX = 300;
        int spacingY = 100;
        
        // Book Flight Button
        btnBookFlight = createStyledButton("Book Flight", startX, startY, buttonWidth, buttonHeight, new Color(0, 153, 76));
        quickActionPanel.add(btnBookFlight);
        
        // Search Flights Button
        btnSearchFlights = createStyledButton("Search Flights", startX + spacingX, startY, buttonWidth, buttonHeight, new Color(0, 102, 204));
        quickActionPanel.add(btnSearchFlights);
        
        // Flight Details Button
        btnFlightDetails = createStyledButton("Flight Details", startX + 2*spacingX, startY, buttonWidth, buttonHeight, new Color(255, 153, 0));
        quickActionPanel.add(btnFlightDetails);
        
        // Add Customer Button
        btnAddCustomer = createStyledButton("Add Customer", startX, startY + spacingY, buttonWidth, buttonHeight, new Color(153, 0, 153));
        quickActionPanel.add(btnAddCustomer);
        
        // Journey Details Button
        btnJourneyDetails = createStyledButton("Journey Details", startX + spacingX, startY + spacingY, buttonWidth, buttonHeight, new Color(204, 0, 102));
        quickActionPanel.add(btnJourneyDetails);
        
        // Cancel Ticket Button
        btnCancelTicket = createStyledButton("Cancel Ticket", startX + 2*spacingX, startY + spacingY, buttonWidth, buttonHeight, new Color(204, 51, 0));
        quickActionPanel.add(btnCancelTicket);
        
        // Boarding Pass Button
        btnBoardingPass = createStyledButton("Boarding Pass", startX + spacingX, startY + 2*spacingY, buttonWidth, buttonHeight, new Color(0, 102, 153));
        quickActionPanel.add(btnBoardingPass);
        
        // Menu Bar
        JMenuBar menubar = new JMenuBar();
        menubar.setBackground(new Color(0, 51, 102));
        menubar.setForeground(Color.WHITE);
        setJMenuBar(menubar);
        
        JMenu details = new JMenu("Details");
        details.setForeground(Color.WHITE);
        details.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menubar.add(details);
        
        JMenuItem flightDetails = new JMenuItem("Flight Details");
        flightDetails.addActionListener(this);
        details.add(flightDetails);
        
        JMenuItem customerDetails = new JMenuItem("Add Customer Details");
        customerDetails.addActionListener(this);
        details.add(customerDetails);
        
        JMenuItem bookFlight = new JMenuItem("Book Flight");
        bookFlight.addActionListener(this);
        details.add(bookFlight);
        
        JMenuItem journeyDetails = new JMenuItem("Journey Details");
        journeyDetails.addActionListener(this);
        details.add(journeyDetails);
        
        JMenuItem ticketCancellation = new JMenuItem("Cancel Ticket");
        ticketCancellation.addActionListener(this);
        details.add(ticketCancellation);

        JMenuItem searchFlights = new JMenuItem("Search Flights");
        searchFlights.addActionListener(this);
        details.add(searchFlights);
        
        JMenu ticket = new JMenu("Ticket");
        ticket.setForeground(Color.WHITE);
        ticket.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menubar.add(ticket);
        
        JMenuItem boardingPass = new JMenuItem("Boarding Pass");
        boardingPass.addActionListener(this);
        ticket.add(boardingPass);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private JButton createStyledButton(String text, int x, int y, int width, int height, Color bgColor) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    public void actionPerformed(ActionEvent ae) {
        String text = ae.getActionCommand();
        
        if (text.equals("Add Customer Details") || text.equals("Add Customer")) {
            new AddCustomer();
        } else if (text.equals("Flight Details")) {
            new FlightInfo();
        } else if (text.equals("Book Flight")) {
            new BookFlight();
        } else if (text.equals("Journey Details")) {
            new JourneyDetails();
        } else if (text.equals("Cancel Ticket")) {
            new Cancel();
        } else if (text.equals("Search Flights")) {
            new FlightSearch();
        } else if (text.equals("Boarding Pass")) {
            new BoardingPass();
        }
    }
    
    public static void main(String[] args) {
        new Home();
    }
}
