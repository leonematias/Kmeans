package kmeans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matias.leone
 */
public class Kmeans {

    public Kmeans() {
        
    }
    
    public List<Cluster> findClusters(List<Vector2> initialCenters, List<Vector2> points) {
        
        List<Cluster> clusters = new ArrayList<Cluster>(initialCenters.size());
        for (Vector2 c : initialCenters) {
            clusters.add(new Cluster(c));
        }
        improveClusters(clusters, points);
        
        return clusters;
    }
    
    private void improveClusters(List<Cluster> clusters, List<Vector2> points) {
        for (Cluster c : clusters) {
            c.getPoints().clear();
        }
        
        for (Vector2 p : points) {
            Cluster c = getNearestCluster(clusters, p);
            c.getPoints().add(p);
        }
        
        for (Cluster c : clusters) {
            c.computeCenter();
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
    
    
    
}
