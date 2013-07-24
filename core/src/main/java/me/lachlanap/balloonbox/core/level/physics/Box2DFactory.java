package me.lachlanap.balloonbox.core.level.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import me.lachlanap.balloonbox.core.level.Level.StaticLevelData;

/**
 *
 * @author lachlan
 */
public class Box2DFactory {

    public static Body createLevelGeometry(World world, StaticLevelData staticLevelData) {
        ChainShape[] brickGeometry =
                GridToChainFactory.makeChainShapes(staticLevelData.brickMap, StaticLevelData.GRID_SCALE);

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(0, 0);
        def.awake = false;

        Body level = world.createBody(def);

        for (ChainShape shape : brickGeometry) {
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1;
            fixtureDef.restitution = 0.1f;
            fixtureDef.friction = 0.1f;
            fixtureDef.shape = shape;

            level.createFixture(fixtureDef);
        }

        return level;
    }

    public static Body createFixed(World world, Vector2 centre, Vector2 size) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(centre);
        def.fixedRotation = true;
        def.awake = false;

        Body body = world.createBody(def);


        FixtureDef fixtureDef = new FixtureDef();
        //fixtureDef.density = 1;
        //fixtureDef.restitution = 0.1f;
        //fixtureDef.friction = 0.1f;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2f, size.y / 2f);
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);

        return body;
    }

    public static Body createDynamic(World world, Vector2 centre, Vector2 size, float density) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(centre);
        def.fixedRotation = true;
        def.awake = true;

        Body body = world.createBody(def);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.restitution = 0.1f;
        fixtureDef.friction = 0.1f;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2f, size.y / 2f);
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);

        return body;
    }

    public static Body createSensor(World world, Vector2 centre, Shape shape) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(centre);
        bodyDef.awake = true;

        Body body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);

        return body;
    }
}
