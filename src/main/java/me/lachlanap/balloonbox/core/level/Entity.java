package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import me.lachlanap.balloonbox.core.level.controller.Controller;
import me.lachlanap.balloonbox.core.level.physics.Box2DFactory;

/**
 *
 * @author lachlan
 */
public class Entity {

    private final List<Controller> controllers = new ArrayList<>();
    private final boolean fixed;
    private final Vector2 initialPosition;
    private final Vector2 size;
    private final EntityType type;
    private final boolean needsGroundSensor;
    private Body body;
    private Fixture groundSensor;
    private int onGroundContacts;
    private boolean markedForKill;

    public Entity(boolean fixed, Vector2 initialPosition, Vector2 size, EntityType type, boolean needsGroundSensor) {
        this.fixed = fixed;
        this.initialPosition = initialPosition;
        this.size = size;
        this.type = type;
        this.needsGroundSensor = needsGroundSensor;
    }

    void attachToWorld(World world) {
        if (fixed) {
            body = Box2DFactory.createFixed(world, initialPosition, size);
        } else {
            body = Box2DFactory.createDynamic(world, initialPosition, size, 1);
        }

        if (needsGroundSensor) {
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.isSensor = true;

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(size.x * 0.4f, size.y / 8, new Vector2(0, -size.y / (6 / 4f)), 0);
            fixtureDef.shape = shape;

            groundSensor = body.createFixture(fixtureDef);
        }
    }

    void detachFromWorld(World world) {
        world.destroyBody(body);
        body = null;
    }

    public boolean checkGroundSensor(Contact contact) {
        if (groundSensor == null)
            return false;

        if (contact.getFixtureA() == groundSensor)
            return !contact.getFixtureB().isSensor();
        if (contact.getFixtureB() == groundSensor)
            return !contact.getFixtureA().isSensor();

        return false;
    }

    public Body getBody() {
        return body;
    }

    public void addController(Controller controller) {
        controller.setEntity(this);
        controllers.add(controller);
    }

    public void update() {
        for (Controller c : controllers) {
            c.update();
        }
    }

    public void addOnGround() {
        onGroundContacts++;
    }

    public void takeOnGround() {
        onGroundContacts--;
    }

    public int getOnGroundContacts() {
        return onGroundContacts;
    }

    public boolean isOnGround() {
        return onGroundContacts > 0;
    }

    public void addForce(Vector2 force) {
        body.applyForceToCenter(force, true);
    }

    public void addForce(float x, float y) {
        addForce(new Vector2(x, y));
    }

    public void shoveY(float y) {
        Vector2 vel = body.getLinearVelocity();
        vel.y = y;
        body.setLinearVelocity(vel);
    }

    public void markForKill() {
        markedForKill = true;
    }

    public boolean isMarkedForKill() {
        return markedForKill;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Vector2 getSize() {
        return size;
    }

    public float getAngle() {
        return body.getAngle();
    }

    public EntityType getType() {
        return type;
    }

    public boolean canJumpOn() {
        return type != EntityType.BALLOON;
    }

    @Override
    public String toString() {
        return "[Entity #" + Integer.toHexString(hashCode()).toUpperCase() + ":"
                + " type=" + type.name()
                + " fixed=" + fixed
                + "]";
    }
}
