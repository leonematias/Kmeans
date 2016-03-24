package kmeans;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

/**
 *
 * @author matias.leone
 */
public class UI {

    private final static int WIN_WIDTH = 1200;
    private final static int WIN_HEIGHT = 720;
    private final static int POINT_RAD = 5;
    private final static int POINTS_COUNT = 500;
    
    private JFrame frame;
    private Kmeans kmeans;
    private List<ScreenPoint> points;
    private List<Cluster> clusters;
    private RenderPanel renderPanel;
    private BufferedImage renderImg;
    private Graphics2D renderG;
    private Dimension graphDim;
    private JTextArea logArea;
    private JSpinner spinner;
    private Stroke normalStroke;
    private Stroke edgeStroke;
    private Stroke dottedStroke;
    
    public static void main(String[] args) {
        new UI();
    }
    
    public UI() {
        kmeans = new Kmeans();
        points = new ArrayList<ScreenPoint>(POINTS_COUNT);
        Random rand = new Random();
        for (int i = 0; i < POINTS_COUNT; i++) {
            int x = 50 + rand.nextInt(WIN_WIDTH - 200);
            int y = 50 + rand.nextInt(WIN_HEIGHT - 200);
            points.add(new ScreenPoint(new Vector2(x, y)));
        }
        
        normalStroke = new BasicStroke();
        edgeStroke = new BasicStroke(2);
        dottedStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f);
        
        frame = new JFrame("K-Means");
        frame.setMinimumSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        JPanel controlsPanel = new JPanel(new FlowLayout());
        spinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        controlsPanel.add(spinner);
        JButton button = new JButton("Compute clusters");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                computeClusters();
            }
        });
        controlsPanel.add(button);
        frame.add(controlsPanel, BorderLayout.NORTH);
        
        renderPanel = new RenderPanel();
        frame.add(renderPanel, BorderLayout.CENTER);
        
        logArea = new JTextArea(4, 100);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setMinimumSize(new Dimension(-1, 200));
        frame.add(scrollPane, BorderLayout.SOUTH);
        
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenDim.width / 2 - WIN_WIDTH / 2, screenDim.height / 2 - WIN_HEIGHT / 2);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        frame.setVisible(true);
    }
    
    /**
     * Main render method
     */
    private void render(Graphics2D g) {
        //Draw clusters
        if(clusters != null) {
            for (Cluster c : clusters) {
                
                g.setPaint(Color.RED);
                g.setStroke(normalStroke);
                g.fillOval((int)c.getCenter().X - POINT_RAD, (int)c.getCenter().Y - POINT_RAD, POINT_RAD * 2, POINT_RAD * 2);
                
                g.setPaint(Color.BLACK);
                g.setStroke(edgeStroke);
                g.drawOval((int)c.getCenter().X - POINT_RAD, (int)c.getCenter().Y - POINT_RAD, POINT_RAD * 2, POINT_RAD * 2);
                
                g.setPaint(Color.RED);
                g.setStroke(dottedStroke);
                g.drawOval((int)(c.getCenter().X - c.getRadius()), (int)(c.getCenter().Y - c.getRadius()), (int)(c.getRadius() * 2), (int)(c.getRadius() * 2));
            }
        }
        
        //Draw points
        g.setPaint(Color.BLUE);
        g.setStroke(normalStroke);
        for (ScreenPoint p : points) {
            g.fill(p.circle);
        }
        g.setPaint(Color.BLACK);
        g.setStroke(edgeStroke);
        for (ScreenPoint p : points) {
            g.draw(p.circle);
        }

    }
    
    /**
     * Compute clusters
     */
    private void computeClusters() {
        int clustersCount = ((SpinnerNumberModel)spinner.getModel()).getNumber().intValue();
        int clusterDist = graphDim.width / clustersCount;
        int y = graphDim.height / 2;
        
        List<Vector2> centers = new ArrayList<Vector2>(clustersCount);
        for (int i = 0; i < clustersCount; i++) {
            centers.add(new Vector2(i * clusterDist, y));
        }
        
        List<Vector2> pList = new ArrayList<Vector2>(points.size());
        for (ScreenPoint p : points) {
            pList.add(p.p);
        }
        
        clusters = kmeans.findClusters(centers, pList);
        renderPanel.repaint();
    }
    
    private void onMouseClicked(int x, int y) {
    }
    
    private void log(String txt) {
        logArea.append(txt);
        logArea.append("\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    /**
     * Render panel
     */
    private class RenderPanel extends Canvas implements MouseListener {
        
        public RenderPanel() {
            addMouseListener(this);
        }
        
        @Override
        public void paint(Graphics g){
                update(g);
	}
        
        @Override
        public void update(Graphics g) {
            
            if(renderImg == null) {
                graphDim = getSize();
                renderImg = (BufferedImage)createImage(graphDim.width, graphDim.height);
                renderG = renderImg.createGraphics();
            }
            
            renderG.setPaint(Color.WHITE);
            renderG.fillRect(0, 0, graphDim.width, graphDim.height);
            
            render(renderG);
            
            g.drawImage(renderImg, 0, 0, this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            onMouseClicked(e.getX(), e.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
    
    private class ScreenPoint {
        public final Vector2 p;
        public final Ellipse2D.Double circle;
        
        public ScreenPoint(Vector2 p) {
            this.p = p;
            this.circle = new Ellipse2D.Double(p.X - POINT_RAD, p.Y - POINT_RAD, POINT_RAD * 2, POINT_RAD * 2);
        }
    }
    
}
