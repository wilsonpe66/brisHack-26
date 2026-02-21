import java.awt.*;
import game.WorldState;

public class HUD {

    public void draw(Graphics g, WorldState world) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));

        g.drawString("Lives: " + world.getPlayer().getLives(), 20, 30);
        g.drawString("Score: " + world.getScore(), 20, 50);
    }
}