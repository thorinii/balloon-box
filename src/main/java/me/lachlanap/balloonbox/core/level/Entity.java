package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.lachlanap.balloonbox.core.level.animation.Animation;
import me.lachlanap.balloonbox.core.level.controller.Controller;
import me.lachlanap.balloonbox.core.level.physics.Box2DFactory;

/**
 *
 * @author lachlan
 */
public final class Entity {

    private final List<Controller> controllers = new ArrayList<>();
    private final boolean fixed;
    private final Vector2 initialPosition;
    private final Vector2 size;
    private final EntityType type;
    private final boolean needsGroundSensor;
    private final EntityAnimationController animationController;

    private Body body;
    private Fixture groundSensor;
    private int onGroundContacts;
    private boolean markedForKill;

    public Entity(boolean fixed,
                  Vector2 initialPosition,
                  Vector2 size,
                  EntityType type,
                  boolean needsGroundSensor,
                  EntityAnimationController animationController) {
        this.fixed = fixed;
        this.initialPosition = initialPosition.cpy();
        this.size = size;
        this.type = type;
        this.needsGroundSensor = needsGroundSensor;
        this.animationController = animationController;

        animationController.setDefaultAnimation(EntityAnimations.defaultAnimationFor(type));
        animationController.setShift(EntityAnimations.hashShiftFor(this));
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
        for (JointEdge je : body.getJointList())
            world.destroyJoint(je.joint);

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

    public void removeController(Class<? extends Controller> klass) {
        for (Iterator<Controller> it = controllers.iterator(); it.hasNext();) {
            Controller c = it.next();
            if (klass.isInstance(c))
                it.remove();
        }
    }

    public boolean hasController(Class<? extends Controller> klass) {
        for (Iterator<Controller> it = controllers.iterator(); it.hasNext();) {
            Controller c = it.next();
            if (klass.isInstance(c))
                return true;
        }
        return false;
    }

    public void update(float tpf) {
        for (Controller c : controllers) {
            c.update(tpf);
        }

        animationController.update();
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

    public void setAnimation(Animation animation) {
        animationController.setAnimation(animation);
    }

    public void setAnimationToDefault() {
        animationController.resetAnimation();
    }

    public EntityAnimationController getAnimationController() {
        return animationController;
    }

    @Override
    public String toString() {
        return "[Entity #" + Integer.toHexString(hashCode()).toUpperCase() + ":"
               + " type=" + type.name()
               + " fixed=" + fixed
               + "]";
    }
}
