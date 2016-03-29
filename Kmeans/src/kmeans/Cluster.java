package kmeans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matias.leone
 */
public class Cluster {
    
    private Vector2 center;
    private float radius;
    private List<Vector2> points;
    
    public Cluster(Vector2 center) {
        this.center = new Vector2().set(center);
        this.radius = 0;
        this.points = new ArrayList<Vector2>();
    }

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public List<Vector2> getPoints() {
        return points;
    }

    public void computeCenter() {
        if(points.size() == 0) return;
        if(points.size() == 1) {
            this.center.set(points.get(0));
            this.radius = 0;
            return;
        }
        
        float maxDist = -1;
        Vector2 m1 = null;
        Vector2 m2 = null;
        for (int i = 0; i < points.size(); i++) {
            Vector2 p1 = points.get(i);
            for (int j = 0; j < points.size(); j++) {
                if(i == j) continue;
                Vector2 p2 = points.get(j);
                float distSq = Vector2.lengthSq(p1, p2);
                if(distSq > maxDist) {
                    maxDist = distSq;
                    m1 = p1;
                    m2 = p2;
                }
            }
        }
        
        
        
        center = Vector2.add(m1, m2, center).mul(0.5f);
        radius = (float)Math.sqrt(maxDist) / 2;        
    }
    
    
    
}
