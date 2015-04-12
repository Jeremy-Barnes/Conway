
package conway;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.profiling.GL20Profiler;
import com.sun.java.swing.plaf.windows.resources.windows;

public class Core extends Game {

	SpriteBatch batch;
	ShapeRenderer r;

	/* ********************* STATISTICS AND TIMEKEEPING VARIABLES ************************ */
	private double secondsElapsed = 0.0;
	private float deltaTime = 0f;
	private short frameCount = 0;
	private short tickCount = 0;
	private double sinceLastTick = 0.0;


	public int cells[][]; //2 = alive, 1 = to die this round, 0 = dead, -1 = to be created
	public int gridX = 100;
	public int gridY = 100;

	@Override
	public void create() {
		batch = new SpriteBatch();
		r = new ShapeRenderer();
		cells = new int[gridX][gridY];

		for (int x = 0; x < gridX; x++) {
			for (int y = 0; y < gridY; y++) {
				if (Math.random() >= .5) {
					cells[x][y] = 5;
				} else {
					cells[x][y] = 0;
				}
			}
		}
	}

	public void render() {

		deltaTime = Gdx.graphics.getDeltaTime();
		secondsElapsed += deltaTime;
		sinceLastTick += deltaTime;

		Gdx.gl.glClearColor(0, 0, 0, 1); //black background
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (sinceLastTick > .1) {
			tick(1); //tick all subcomponents
			tickCount++;
			sinceLastTick -= .1;
		}

		Gdx.graphics.setTitle("Ticks: " + tickCount + " Frames: " + frameCount + "     FPS: " + (frameCount * 1. / secondsElapsed) + "         UPS: " + (tickCount * 1. / secondsElapsed));

		if (secondsElapsed > 30.0) {
			tickCount = 0;
			secondsElapsed = 0;
			frameCount = 0;
		}
		r.begin(ShapeType.Filled);
		r.setColor(Color.CYAN);
		for (int x = 0; x < gridX; x++) {
			for (int y = 0; y < gridY; y++) {
				if (cells[x][y] == 2) {
					r.rect(x *(1000/gridX), y* (1000/gridY), 1000/gridX, 1000/gridY);
				}
			}
		}
		r.end();
		frameCount++;
	}

	public void tick(float dt) {
		for (int x = 0; x < gridX; x++) {
			for (int y = 0; y < gridY; y++) {
				int neighbors = getLiveNeighbors(x, y);
				if (cells[x][y] == 2 && (neighbors < 2 || neighbors > 3)) {
					cells[x][y] = 1;
				} else if (neighbors == 3) {
					cells[x][y] = -1;
				}
			}
		}

		for (int x = 0; x < gridX; x++) {
			for (int y = 0; y < gridY; y++) {
				if (cells[x][y] == 1) {
					cells[x][y] = 0;
				}
				if (cells[x][y] == -1) {
					cells[x][y] = 2;
				}
			}
		}
	}

	public int getLiveNeighbors(int x, int y) {

		int north = y - 1;
		int west = x - 1;
		int east = x + 1;
		int south = y + 1;
		int neighbors = 0;

		if (isValidTile(west, north) && this.cells[west][north] > 0) {
			neighbors++;
		}
		if (isValidTile(x, north) && this.cells[x][north] > 0) {
			neighbors++;
		}

		if (isValidTile(east, north) && this.cells[east][north] > 0) {
			neighbors++;
		}

		if (isValidTile(west, y) && this.cells[west][y] > 0) {
			neighbors++;
		}

		if (isValidTile(east, y) && this.cells[east][y] > 0) {
			neighbors++;
		}

		if (isValidTile(west, south) && this.cells[west][south] > 0) {
			neighbors++;
		}

		if (isValidTile(x, south) && this.cells[x][south] > 0) {
			neighbors++;
		}
		if (isValidTile(east, south) && this.cells[east][south] > 0) {
			neighbors++;
		}
		return neighbors;
	}

	public boolean isValidTile(int x, int y) {
		if (x < 0 || x >= gridX || y < 0 || y >= gridY) {
			return false;
		}
		return true;
	}

}//class
