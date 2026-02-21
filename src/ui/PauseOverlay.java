import java.awt.*;

public class PauseOverlay {

    public void draw(Graphics g, int w, int h) {
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0,0,w,h);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("PAUSED", w/2 - 100, h/2);
    }
}