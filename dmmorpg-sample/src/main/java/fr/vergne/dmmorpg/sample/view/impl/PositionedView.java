package fr.vergne.dmmorpg.sample.view.impl;

import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.view.View;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldPosition;
import fr.vergne.dmmorpg.sample.world.WorldUpdate;

public class PositionedView implements View<Graphics> {

	private final World world;
	private final WorldPosition center;
	private final Collection<Listener<? super WorldUpdate>> listeners = new LinkedList<>();
	private final Listener<WorldUpdate> updateListener = update -> Updatable.fireUpdate(listeners, update);
	private final Scaler scaler = new Scaler();

	public PositionedView(World world, WorldPosition center) {
		this.world = world;
		this.world.listenUpdate(updateListener);
		this.center = center;
		this.scaler.listenUpdate(updateListener);
	}

	@Override
	protected void finalize() throws Throwable {
		world.unlistenUpdate(updateListener);
		super.finalize();
	}

	@Override
	public void render(Graphics g) {
		new Renderer().render(g, scaler.getScale(), world, center);
	}

	public void increaseScale() {
		scaler.increaseScale();
	}

	public void decreaseScale() {
		scaler.decreaseScale();
	}

	@Override
	public void listenUpdate(Listener<? super WorldUpdate> listener) {
		listeners.add(listener);
	}

	@Override
	public void unlistenUpdate(Listener<? super WorldUpdate> listener) {
		listeners.remove(listener);
	}

	@Override
	public String toString() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("world", world);
		data.put("center", center);
		return "View" + data;
	}
}
