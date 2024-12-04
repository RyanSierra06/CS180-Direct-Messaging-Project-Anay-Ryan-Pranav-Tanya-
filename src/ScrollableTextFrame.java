import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.Image.*;
import java.awt.image.BufferedImage;

public class ScrollableTextFrame {
    private JFrame frame;
    private JPanel contentPanel;
    private JScrollPane scrollPane;

    public ScrollableTextFrame() {
        // Create the main frame
        frame = new JFrame("Scrollable Text Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);

        // Create a panel with BoxLayout for vertical stacking
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Wrap the content panel in a JScrollPane
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add the scroll pane to the frame
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    // Method to add a new label with text to the frame
    public void addText(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        contentPanel.add(label);
        contentPanel.revalidate(); // Recalculate the layout
        contentPanel.repaint();   // Refresh the UI
    }

    public void addImage(String imgAdderes) {
        try {
            File imageFile = new File(imgAdderes);
            BufferedImage image = ImageIO.read(imageFile);
            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setPreferredSize(new Dimension(100, 100));
            imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
            contentPanel.add(imageLabel);
            contentPanel.revalidate(); // Recalculate the layout
            contentPanel.repaint();   // Refresh the UI
        } catch(Exception e) {
            e.printStackTrace();
        }  
    }

    public void reset() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Wrap the content panel in a JScrollPane
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add the scroll pane to the frame
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        ScrollableTextFrame textFrame = new ScrollableTextFrame();

        // Example: Adding text to the frame dynamically
        for (int i = 1; i <= 20; i++) {
            textFrame.addText("Line " + i);
            try {
                Thread.sleep(500); // Simulate delay between updates
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
