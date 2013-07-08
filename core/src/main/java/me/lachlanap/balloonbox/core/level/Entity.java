package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import me.lachlanap.balloonbox.core.level.controller.Controller;

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
    private boolean onGround;
    private boolean markedForKill;

    public Entity(boolean fixed, Vector2 initialPosition, Vector2 size, EntityType type, boolean needsGroundSensor) {
        this.fixed = fixed;
        this.initialPosition = initialPosition;
        this.size = size;
        this.type = type;
        this.needsGroundSensor = needsGroundSensor;
    }

    void attachToWorld(World world) {
        BodyDef def = new BodyDef();
        def.type = fixed ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        def.position.set(initialPosition);
        def.fixedRotation = true;
        def.awake = !fixed;

        body = world.createBody(def);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.restitution = 0.1f;
        fixtureDef.friction = 0.1f;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x, size.y);
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);


        if (needsGroundSensor) {
            shape.setAsBox(size.x, size.y / 4, new Vector2(0, -size.y / (3 / 4f)), 0);
            fixtureDef.isSensor = true;

            groundSensor = body.createFixture(fixtureDef);
        }
    }
    
    void detachFromWorld(World world){
        world.destroyBody(body);
        body = null;
    }

    public boolean checkGroundSensor(Contact contact) {
        if (groundSensor == null)
            return false;

        if (contact.getFixtureA() == groundSensor)
            return true;
        if (contact.getFixtureB() == groundSensor)
            return true;

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

    public void setOnGround(boolean b) {
        onGround = b;
    }

    public boolean isOnGround() {
        return onGround;
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

    public float getAngle() {
        return body.getAngle();
    }

    public EntityType getType() {
        return type;
    }
}
