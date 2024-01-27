import javafx.scene.effect.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ParticleManager {
    private ArrayList<CursorParticle> cursorParticles;
    private static ParticleManager instance;
    private final double outerRadius = 10;
    private final double innerRadius = 7;
    private Bloom bloom;

    public static ParticleManager getInstance(){
        if(instance == null){
            instance = new ParticleManager();
        }
        return instance;
    }

    public ParticleManager(){
        cursorParticles = new ArrayList<>();
        bloom = new Bloom();
        bloom.setThreshold(0.1);
    }

    public void createCursorParticle(double posX , double posY , double velX , double velY){
        cursorParticles.add(new CursorParticle(posX, posY, velX, velY));
    }

    public void particleUpdate(){

        //draw outer particle first
//        for(int i = 0 ; i < cursorParticles.size() ; i++){
//            CursorParticle cP = cursorParticles.get(i);
//            Main.gc.setFill(Color.web("WHITE"));
//            Main.gc.fillOval(cP.getPosX()-outerRadius, cP.getPosY()-outerRadius, outerRadius * 2, outerRadius * 2);
//
//        }
//
//        //then draw inner particle
//        for(int i = 0 ; i < cursorParticles.size() ; i++){
//            CursorParticle cP = cursorParticles.get(i);
//            if(cP.isTimeout()){//remove particle when timeout
//                cursorParticles.remove(i);
//                i--;
//            }
//            Main.gc.setFill(Color.web("BLACK"));
//            Main.gc.fillOval(cP.getPosX()-innerRadius, cP.getPosY()-innerRadius, innerRadius * 2, innerRadius * 2);
//            cP.update();
//        }

        //color for trail
        Color color1 = Color.web("ROYALBLUE");
        Color color2 = color1.deriveColor(0,0.9,1,1);
        Color color3 = color1.deriveColor(0,0.85,1,1);
        Color color4 = color1.deriveColor(0,0.8,1,1);

        for(int i = 0 ; i < cursorParticles.size() ; i++){
            CursorParticle cP = cursorParticles.get(i);

            if(cP.isTimeout()){//remove particle when timeout
                cursorParticles.remove(i);
                i--;
            }

            //make trails blue -> smoother trails
            Main.gc.setEffect(new GaussianBlur(0.5));

            //trails get smaller over time
            if(cP.getDespawnTimer()<0.02) {
                Main.gc.setFill(color1);
                Main.gc.fillOval(cP.getPosX() - outerRadius, cP.getPosY() - outerRadius, outerRadius * 2, outerRadius * 2);
            } else if (cP.getDespawnTimer()<0.06) {
                Main.gc.setFill(color2);
                Main.gc.fillOval(cP.getPosX() - outerRadius*0.8, cP.getPosY() - outerRadius*0.8, outerRadius*0.8 * 2, outerRadius*0.8 * 2);
            } else if (cP.getDespawnTimer()<0.08) {
                Main.gc.setFill(color3);
                Main.gc.fillOval(cP.getPosX() - outerRadius*0.6, cP.getPosY() - outerRadius*0.6, outerRadius*0.6 * 2, outerRadius*0.6 * 2);
            } else {
                Main.gc.setFill(color4);
                Main.gc.fillOval(cP.getPosX() - outerRadius*0.4, cP.getPosY() - outerRadius*0.4, outerRadius*0.4 * 2, outerRadius*0.4 * 2);
            }
            Main.gc.setEffect(null);

            cP.update();
        }

    }
}
