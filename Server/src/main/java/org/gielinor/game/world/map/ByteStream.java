package org.gielinor.game.world.map;

public class ByteStream {

    private byte[] buffer;
    private int offset;

    public ByteStream(byte[] buffer) {
        this.buffer = buffer;
        this.offset = 0;
    }

    public byte getByte() {
        return this.buffer[(this.offset++)];
    }

    public byte[] getBytes() {
        int i = this.offset;
        while (this.buffer[(this.offset++)] != 10) {
            ;
        }
        byte[] abyte0 = new byte[this.offset - i - 1];
        System.arraycopy(this.buffer, i, abyte0, i - i, this.offset - 1 - i);
        return abyte0;
    }

    public int getInt() {
        return (getUByte() << 24) + (getUByte() << 16) + (getUByte() << 8)
            + getUByte();
    }

    public long getLong() {
        return (getUByte() << 56) + (getUByte() << 48) + (getUByte() << 40)
            + (getUByte() << 32) + (getUByte() << 24) + (getUByte() << 16)
            + (getUByte() << 8) + getUByte();
    }

    public String getNString() {
        int i = this.offset;
        while (this.buffer[(this.offset++)] != 0) {
            ;
        }
        return new String(this.buffer, i, this.offset - i - 1);
    }

    public int getShort() {
        int val = (getByte() << 8) + getByte();
        if (val > 32767) {
            val -= 65536;
        }
        return val;
    }

    public int getUByte() {
        return this.buffer[(this.offset++)] & 0xFF;
    }

    public int getUShort() {
        return (getUByte() << 8) + getUByte();
    }

    public int getUSmart() {
        int i = this.buffer[this.offset] & 0xFF;
        if (i < 128) {
            return getUByte();
        }
        return getUShort() - 32768;
    }

    public int length() {
        return this.buffer.length;
    }

    public byte[] read(int length) {
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = this.buffer[(this.offset++)];
        }
        return b;
    }

    public void setOffset(int position) {
        this.offset = position;
    }

    public void setOffset(long position) {
        this.offset = (int) position;
    }

    public void skip(int length) {
        this.offset += length;
    }
}
