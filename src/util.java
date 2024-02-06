import java.awt.*;

public class util {

    //calculate unit vector
    public static double[] calculateDirection(double x1, double y1, double x2, double y2){
        double[] vector = {x2 - x1, y2 - y1};
        double denom = Math.sqrt(vector[0]*vector[0] + vector[1]*vector[1]);
        vector[0]/=denom;
        vector[1]/=denom;
        return vector;
    }

    public static double calculateVectorsDegree(double[] coordinate , double[] origin){
        double[] thisVector = {coordinate[0]-origin[0] , coordinate[1]-origin[1]};
        double[] zeroDegreeVector = {1,0}; // 0 degree at (1,0)
        //angle of 2 vector
        double angleRad = Math.atan2(thisVector[1] , thisVector[0]) - Math.atan2(zeroDegreeVector[1] , zeroDegreeVector[0]);
        return angleRad;
    }

    public static double calculateVectorsDegree(double[] vector){
        double[] zeroDegreeVector = {1,0}; // 0 degree at (1,0)
        //angle of 2 vector
        double angleRad = Math.atan2(vector[1] , vector[0]) - Math.atan2(zeroDegreeVector[1] , zeroDegreeVector[0]);
        return angleRad;
    }


}
