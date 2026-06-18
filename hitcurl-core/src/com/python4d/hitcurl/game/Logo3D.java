package com.python4d.hitcurl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.python4d.hitcurl.screens.SplashScreen;

public class Logo3D {

	private static final String TAG = SplashScreen.class.getName();
	protected PerspectiveCamera camera3D;
	protected ModelBatch modelBatch;
	private Model box;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	protected Environment environment;
	private Model model;
	private Model sphere;
	private float x = 0.001f;
	private Model sphere2;
	private float posy;
	private float posx;
	private boolean moveOnScreen = false;
	private Vector3 vect3;
	private float size;
	private float incx = 1.0f;
	private float incy = 1.0f;

	public Logo3D(String object, float x, float y, float size) {
		this.posx = x;
		this.posy = y;
		this.size = size;
		this.camera3D = new PerspectiveCamera(67f, 100f, 100f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
		// Move the camera 3 units back along the z-axis and look at the
		// origin
		vect3 = new Vector3(-posx * 1 / size, -posy * 1 / size, 0f);
		camera3D.unproject(vect3);
		camera3D.position.set(0, 0, 1 / size);
		camera3D.lookAt(new Vector3(0f, 0f, 0f).add(vect3));
		// Near and Far (plane) repesent the minimum and maximum ranges of
		// the
		// camera in, um, units
		camera3D.near = 0.1f;
		camera3D.far = 300.0f;
		camera3D.update();
		// A ModelBatch is like a SpriteBatch, just for models. Use it to
		// batch
		// up geometry for OpenGL
		modelBatch = new ModelBatch();
		// A ModelBuilder can be used to build meshes by hand
		ModelBuilder modelBuilder = new ModelBuilder();
		// It also has the handy ability to make certain premade shapes,
		// like a
		// Cube
		// We pass in a ColorAttribute, making our cubes diffuse ( aka,
		// color )
		// red.
		// And let openGL know we are interested in the Position and Normal
		// channels
		box = modelBuilder.createBox(0f, 0f, 0f,
				new Material(ColorAttribute.createDiffuse(Color.YELLOW)),
				Usage.Position | Usage.Normal);

		sphere = modelBuilder.createSphere(2f, 2f, 2f, 10, 10,
				new Material(ColorAttribute.createDiffuse(Color.YELLOW)),
				Usage.Position | Usage.Normal);
		sphere2 = modelBuilder.createSphere(2f, 2f, 2f, 10, 10,
				new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				Usage.Position | Usage.Normal);
		// A model holds all of the information about an, um, model, such as
		// vertex data and texture info
		// However, you need an instance to actually render it. The instance
		// contains all the
		// positioning information ( and more ). Remember Model==heavy
		// ModelInstance==Light
		instances.add(new ModelInstance(box, 0, .25f, 0));
		instances.add(new ModelInstance(sphere, 0, 0, 0));
		instances.add(new ModelInstance(sphere2, 0, 0, 0));
		ModelLoader loader = new ObjLoader();
		model = loader.loadModel(Gdx.files.internal(object));
		// model.materials.get(0).set(new IntAttribute(IntAttribute.CullFace,
		// 0));
		instances.add(new ModelInstance(model, 0, 0, 0));
		BoundingBox bbox = new BoundingBox();
		instances.get(0).calculateBoundingBox(bbox);
		Gdx.app.debug(TAG, "Bound Model= " + bbox);
		// Finally we want some light, or we wont see our color. The
		// environment
		// gets passed in during
		// the rendering process. Create one, then create an Ambient (
		// non-positioned, non-directional ) light.
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	public void render(float deltaTime) {
		// For some flavor, lets spin our camera around the Y axis by 1
		// degree
		// each time render is called
		camera3D.rotateAround(Vector3.Zero, new Vector3(1, 1, 0), 1f);
		x += 0.05;
		instances.get(1).transform.setToTranslation((float) Math.sin(x) * 5, (float) Math.cos(x) * 5, 2.5f - (float) Math.sin(x) * 5);
		instances.get(2).transform.setToTranslation((float) Math.cos(x) * 5, (float) Math.sin(x) * 5, (float) Math.cos(x) * 5 - 2.5f);
		if (moveOnScreen) {
			// UpdateCameraPosition();

			vect3 = new Vector3(-posx * 1 / size, -posy * 1 / size, 0f);
			camera3D.unproject(vect3);
			camera3D.position.set(0, 0, 1 / size);
			camera3D.lookAt(new Vector3(0f, 0f, 0f).add(vect3));
		}
		// When you change the camera details, you need to call update();
		// Also note, you need to call update() at least once.
		camera3D.update();

		// Like spriteBatch, just with models! pass in the box Instance and
		// the
		// environment
		modelBatch.begin(camera3D);
		modelBatch.render(instances, environment);
		modelBatch.end();

	}

	private void UpdateCameraPosition() {
		if (posx > Gdx.graphics.getWidth() / 2.0f)
			incx = -1.0f / 100;
		if (posx < -Gdx.graphics.getWidth() / 2.0f)
			incx = 1.0f / 100;
		if (posy > Gdx.graphics.getHeight() / 2.0f)
			incy = -1.0f / 100;
		if (posy < -Gdx.graphics.getHeight() / 2.0f)
			incy = 1.0f / 100;
		posx += incx;
		posy += incy;

	}

	public void setMoveOnScreen(boolean b) {
		moveOnScreen = b;

	}
}
