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
        List<Vector2> prevCenters = new ArrayList<Vector2>(initialCenters.size());
        for (Vector2 c : initialCenters) {
            clusters.add(new Cluster(c));
            prevCenters.add(new Vector2());
        }
        improveClusters(clusters, points, prevCenters);
        
        return clusters;
    }
    
    private void improveClusters(List<Cluster> clusters, List<Vector2> points, List<Vector2> prevCenters) {
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
            if(diff > 1) {
                 centersChange = true;
                 break;
            }
        }
        
        //Recurse if there was any change
        if(centersChange) {
            improveClusters(clusters, points, prevCenters);
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
