package entities;

import java.util.List;

public interface SelfDefendable {

    List<? extends Bullet> shoot();
}
