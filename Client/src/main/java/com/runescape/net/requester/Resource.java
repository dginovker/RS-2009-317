package com.runescape.net.requester;

import com.runescape.collection.Cacheable;

public final class Resource extends Cacheable {

    public int dataType;
    public byte[] buffer;
    public int ID;
    public boolean incomplete;
    int loopCycle;
    public String name;

    public Resource() {
        incomplete = true;
    }
}
