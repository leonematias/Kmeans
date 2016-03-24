package kmeans;

/**
 * A 2D vector
 * 
 * @author matias.leone
 */
public class Vector2 {
    
    public float X;
    public float Y;
    
    public Vector2() {
        X = 0;
        Y = 0;
    }

    public Vector2(float X, float Y) {
        set(X, Y);
    }
    
    public Vector2(Vector2 v) {
        set(v);
    }
    
    public Vector2 set(float X, float Y) {
        this.X = X;
        this.Y = Y;
        return this;
    }
    
    public Vector2 set(Vector2 v) {
        return set(v.X, v.Y);
    }
    
    public static Vector2 interpolate(Vector2 v1, float s1, Vector2 v2, float s2, Vector2 out) {
        out.X = v1.X * s1 + v2.X * s2;
        out.Y = v1.Y * s1 + v2.Y * s2;
        return out;
    }
    
    public static Vector2 interpolate(Vector2 v1, float s1, Vector2 v2, float s2, Vector2 v3, float s3, Vector2 out) {
        out.X = v1.X * s1 + v2.X * s2 + v3.X * s3;
        out.Y = v1.Y * s1 + v2.Y * s2 + v3.Y * s3;
        return out;
    }
    
    public static float length(float x1, float y1, float x2, float y2) {
        return (float)Math.sqrt(lengthSq(x1, y1, x2, y2));
    }
    
    public static float length(Vector2 a, Vector2 b) {
        return length(a.X, a.Y, b.X, b.Y);
    }
    
    public static float length(Vector2 v) {
        return length(v, v);
    }
    
    public static float lengthSq(Vector2 v) {
        return lengthSq(v, v);
    }
    
    public static float lengthSq(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return dx * dx + dy * dy;
    }
    
    public static float lengthSq(Vector2 a, Vector2 b) {
        return lengthSq(a.X, a.Y, b.X, b.Y);
    }
    
    public static Vector2 add(Vector2 a, Vector2 b, Vector2 out) {
        return out.set(a.X + b.X, a.Y + b.Y);
    }
    
    public Vector2 add(float s) {
        X += s;
        Y += s;
        return this;
    }
    
    public static Vector2 sub(Vector2 a, Vector2 b, Vector2 out) {
        return out.set(a.X - b.X, a.Y - b.Y);
    }
    
    public Vector2 sub(float s) {
        X -= s;
        Y -= s;
        return this;
    }
    
    public Vector2 mul(float s) {
        X *= s;
        Y *= s;
        return this;
    }
    
    public Vector2 div(float s) {
        X /= s;
        Y /= s;
        return this;
    }
    
    public Vector2 normalize() {
        float l = length(this);
        div(l);
        return this;
    }
    
    public float get(int i) {
        if(i == 0) return X;
        if(i == 1) return Y;
        throw new IndexOutOfBoundsException("Invalid index: " + i);
    }
    
}
