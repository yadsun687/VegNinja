public class CursorParticle {
    private double posX;
    private double posY;
    private double despawnTimer=0;
    private boolean isTimeout = false;

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getDespawnTimer() {
        return despawnTimer;
    }

    public void setDespawnTimer(double despawnTimer) {
        this.despawnTimer = despawnTimer;
    }

    public boolean isTimeout() {
        return isTimeout;
    }

    public void setTimeout(boolean timeout) {
        isTimeout = timeout;
    }

    public CursorParticle(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        }

    public void update(){
        if(despawnTimer>0.1) isTimeout=true;
        despawnTimer+=Main.GameAnimationTimer.deltaTime;
    }

}
