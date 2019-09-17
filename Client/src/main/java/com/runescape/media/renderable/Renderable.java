package com.runescape.media.renderable;

import com.runescape.collection.Cacheable;
import com.runescape.media.Vertex;

public class Renderable extends Cacheable {

    public int modelHeight;
    public Vertex[] vertexes;

    public Renderable() {
        modelHeight = 1000;
    }

    public void renderAtPoint(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int newUid) { // TODO Check this out
        Model model = getRotatedModel();
        if (model != null) {
            modelHeight = model.modelHeight;
            model.renderAtPoint(i, j, k, l, i1, j1, k1, l1, i2, newUid);
        }
    }

    public Model getRotatedModel() {
        return null;
    }
}