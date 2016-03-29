package kmeans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matias.leone
 */
public class Kmeans {

    private final static int CENTER_CHANGE_SQ = 1;
    private final static int MAX_ITERATIONS = 20;
    private final static int CELL_SIZE = 150;
    private final static float CELL_SPLIT_PERCENT = 0.2f;
    
    public Kmeans() {
        
    }
    
    public List<Cluster> findClusters(List<Vector2> points, Vector2 min, Vector2 max) {
        int cellsX = (int)((max.X - min.X) / CELL_SIZE);
        int cellsY = (int)((max.Y - min.Y) / CELL_SIZE);
        List<Vector2> initialCenters = new ArrayList<Vector2>();
        int splitCount = (int)(points.size() * CELL_SPLIT_PERCENT);
        
        for (int i = 0; i < cellsX; i++) {
            for (int j = 0; j < cellsY; j++) {
                
                float minX = min.X + i * CELL_SIZE;
                float minY = min.Y + j * CELL_SIZE;
                float maxX = minX + CELL_SIZE;
                float maxY = minY + CELL_SIZE;
                float midX = minX + CELL_SIZE / 2;
                float midY = minY + CELL_SIZE / 2;
                
                int count = countPointsInBox(points, minX, minY, maxX, maxY);
                if(count > splitCount) {
                    initialCenters.add(new Vector2((minX + midX) / 2, (minY + midY) / 2));
                    initialCenters.add(new Vector2((midX + maxX) / 2, (minY + midY) / 2));
                    initialCenters.add(new Vector2((minX + midX) / 2, (midY + maxY) / 2));
                    initialCenters.add(new Vector2((midX + maxX) / 2, (midY + maxY) / 2));
                   
                } else if(count > 0){
                    initialCenters.add(new Vector2(midX, midY));
                }
                
            }
        }
        
        return findClusters(initialCenters, points);
    }
    
    private List<Cluster> findClusters(List<Vector2> initialCenters, List<Vector2> points) {
        
        List<Cluster> clusters = new ArrayList<Cluster>(initialCenters.size());
        List<Vector2> prevCenters = new ArrayList<Vector2>(initialCenters.size());
        for (Vector2 c : initialCenters) {
            clusters.add(new Cluster(c));
            prevCenters.add(new Vector2());
        }
        improveClusters(clusters, points, prevCenters, 0);
        
        return clusters;
    }
    
    private void improveClusters(List<Cluster> clusters, List<Vector2> points, List<Vector2> prevCenters, int iterations) {
        //Clear points from all clusters and save current centers
        for (int i = 0; i < clusters.size(); i++) {
            Cluster c = clusters.get(i);
            c.getPoints().clear();
            prevCenters.get(i).set(c.getCenter());
        }
        
        //Assign points to the nearest cluster
        for (Vector2 p : points) {
            Cluster c = getNearestCluster(clusters, p);
            c.getPoints().add(p);
        }
        
        //Compute center and radius of each cluster
        for (Cluster c : clusters) {
            c.computeCenter();
        }
        
        //Remove clusters without any point
        for (int i = clusters.size() - 1; i >= 0; i--) {
            Cluster c = clusters.get(i);
            if(c.getPoints().size() == 0) {
                clusters.remove(i);
                prevCenters.remove(i);
            }
        }
        
        //Check if there was any center adjustment
        boolean centersChange = false;
        for (int i = clusters.size() - 1; i >= 0; i--) {
            Cluster c = clusters.get(i);
            float diff = Vector2.lengthSq(c.getCenter(), prevCenters.get(i));
            if(diff > CENTER_CHANGE_SQ) {
                 centersChange = true;
                 break;
            }
        }
        
        //Recurse if there was any change
        if(centersChange & iterations < MAX_ITERATIONS) {
            improveClusters(clusters, points, prevCenters, ++iterations);
        }
    }
    
    private Cluster getNearestCluster(List<Cluster> clusters, Vector2 p) {
        Cluster nearest = null;
        float minDist = Float.MAX_VALUE;
        for (Cluster c : clusters) {
            float distSq = Vector2.lengthSq(p.X, p.Y, c.getCenter().X, c.getCenter().Y);
            if(distSq < minDist) {
                minDist = distSq;
                nearest = c;
            }
        }
        return nearest;
    }
    
    private int countPointsInBox(List<Vector2> points, float minX, float minY, float maxX, float maxY) {
        int count = 0;
        for (Vector2 p : points) {
            if(p.X >= minX && p.X < maxX && p.Y >= minY && p.Y < maxY) {
                count++;
            }
        }
        return count;
    }
    
    
}
