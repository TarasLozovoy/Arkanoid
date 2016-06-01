package ua.in.levor.arkanoid.PowerUps;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PowerUpHelper {
    private static PowerUpHelper instance;
    private Array<Vector2> requestedPowerUpsPositions = new Array<Vector2>();

    private PowerUpHelper(){}

    public static PowerUpHelper getInstance() {
        if (instance == null) {
            instance = new PowerUpHelper();
        }
        return instance;
    }

    public void requestNewPowerUp(Vector2 position) {
        requestedPowerUpsPositions.add(position);
    }

    public Array<Vector2> getRequestedPowerUpsPositions() {
        return requestedPowerUpsPositions;
    }

    public void clearRequestedPowerUpsPositions() {
        requestedPowerUpsPositions.clear();
    }
}
