package me.lachlanap.balloonbox.core.level.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import java.util.*;
import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.Level;

/**
 *
 * @author lachlan
 */
public class SensorManager implements ContactListener {

    private final Level level;
    private final Map<Fixture, Sensor> fixture2Sensor;
    private final Map<String, Sensor> sensorByName;

    public SensorManager(Level level) {
        this.level = level;
        fixture2Sensor = new HashMap<>();
        sensorByName = new HashMap<>();
    }

    public Sensor createSensor(String name, Vector2 centre, Vector2 extents) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(extents.x, extents.y);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(centre);
        bodyDef.awake = true;

        Body body = level.getWorld().createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;

        Fixture fixture = body.createFixture(fixtureDef);


        Sensor sensor = new Sensor(name, centre, extents);
        fixture2Sensor.put(fixture, sensor);
        sensorByName.put(name, sensor);

        return sensor;
    }

    public List<Sensor> getSensors() {
        return new ArrayList<>(fixture2Sensor.values());
    }

    public Sensor getSensor(String name) {
        return sensorByName.get(name);
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getBody() == fb.getBody() || !contact.isEnabled())
            return;

        List<Entity> entities = findEntities(fa, fb);

        if (entities.isEmpty()) {
            // TODO: write nonentity contact impl
        } else if (entities.size() == 1) {
            Entity e = entities.get(0);
            Sensor s = fixture2Sensor.containsKey(fa) ? fixture2Sensor.get(fa) : fixture2Sensor.get(fb);

            if (e.isMarkedForKill() || s == null)
                return;

            s.add(e);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getBody() == fb.getBody())
            return;

        List<Entity> entities = findEntities(fa, fb);

        if (entities.isEmpty()) {
            // TODO: write nonentity contact impl
        } else if (entities.size() == 1) {
            Entity e = entities.get(0);
            Sensor s = fixture2Sensor.containsKey(fa) ? fixture2Sensor.get(fa) : fixture2Sensor.get(fb);

            if (s == null)
                return;

            s.remove(e);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private List<Entity> findEntities(Fixture... fixtures) {
        List<Entity> entities = new ArrayList<>();

        for (Fixture f : fixtures)
            for (Entity e : level.getEntities())
                if (f.getBody() == e.getBody())
                    entities.add(e);

        return entities;
    }

    public static class Sensor {

        private final String name;
        private final Vector2 position, extents;
        private final List<Entity> touchingEntities;

        private Sensor(String name, Vector2 centre, Vector2 extents) {
            this.name = name;
            this.position = centre;
            this.extents = extents;
            touchingEntities = new ArrayList<>();
        }

        void add(Entity e) {
            if (!touchingEntities.contains(e))
                touchingEntities.add(e);
        }

        void remove(Entity e) {
            touchingEntities.remove(e);
        }

        public String getName() {
            return name;
        }

        public Vector2 getPosition() {
            return position;
        }

        public Vector2 getExtents() {
            return extents;
        }

        public List<Entity> getTouchingEntities() {
            for (Iterator<Entity> it = touchingEntities.iterator(); it.hasNext();) {
                Entity touching = it.next();
                if (touching.isMarkedForKill())
                    it.remove();
            }

            return touchingEntities;
        }
    }
}
