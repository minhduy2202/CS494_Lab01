package Client.Packet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Packet implements AutoCloseable {

    private List<Byte> _buffer;
    private byte[] _readableBuffer;
    private int _readPointer = 0;
    private boolean _disposed = false;

    public int getLength() {
        return _buffer.size();
    }

    public byte[] getArray() {
        _readableBuffer = toByteArray(_buffer);
        return _readableBuffer;
    }

    public int getUnreadLength() {
        return _buffer.size() - _readPointer;
    }

    public Packet() {
        _buffer = new ArrayList<>();
        _readPointer = 0;
    }

    public Packet(int id) {
        _buffer = new ArrayList<>();
        _readPointer = 0;

        putInt(id);
    }

    public Packet(byte[] data) {
        _buffer = new ArrayList<>();
        _readPointer = 0;

        assignBytes(data);
    }

    public void assignBytes(byte[] data) {
        putBytes(data);
        _readableBuffer = toByteArray(_buffer);
    }

    // Length is always at the beginning of the packet
    public void insertLength() {
        byte[] lengthBytes = ByteBuffer.allocate(4).putInt(_buffer.size()).array();
        for (int i = lengthBytes.length - 1; i >= 0; i--) {
            _buffer.add(0, lengthBytes[i]);
        }
    }

    public void insertInt(int value) {
        byte[] intBytes = ByteBuffer.allocate(4).putInt(value).array();
        for (int i = intBytes.length - 1; i >= 0; i--) {
            _buffer.add(0, intBytes[i]);
        }
    }

    // Reset the packet (delete all contents)
    public void reset() {
        _buffer.clear();
        _readableBuffer = null;
        _readPointer = 0;
    }

    // Unread the last integer
    public void revert() {
        _readPointer -= 4;
    }

    // Write Data
    public void putByte(byte value) {
        _buffer.add(value);
    }

    public void putBytes(byte[] value) {
        for (byte b : value) {
            _buffer.add(b);
        }
    }

    public void putShort(short value) {
        byte[] shortBytes = ByteBuffer.allocate(2).putShort(value).array();
        putBytes(shortBytes);
    }

    public void putInt(int value) {
        byte[] intBytes = ByteBuffer.allocate(4).putInt(value).array();
        putBytes(intBytes);
    }

    public void putLong(long value) {
        byte[] longBytes = ByteBuffer.allocate(8).putLong(value).array();
        putBytes(longBytes);
    }

    public void putFloat(float value) {
        byte[] floatBytes = ByteBuffer.allocate(4).putFloat(value).array();
        putBytes(floatBytes);
    }

    public void putBool(boolean value) {
        byte[] boolBytes = new byte[]{(byte) (value ? 1 : 0)};
        putBytes(boolBytes);
    }

    public void putString(String value) {
        byte[] stringBytes = value.getBytes();
        putInt(stringBytes.length);
        putBytes(stringBytes);
    }

    // Read Data
    public byte readByte(boolean moveNext) throws Exception {
        if (_buffer.size() > _readPointer) {
            byte value = _readableBuffer[_readPointer];
            if (moveNext) {
                _readPointer += 1;
            }
            return value;
        } else {
            throw new Exception("Read byte error!");
        }
    }

    public byte[] readBytes(int length, boolean moveNext) throws Exception {
        if (_buffer.size() > _readPointer) {
            byte[] value = new byte[length];
            System.arraycopy(_readableBuffer, _readPointer, value, 0, length);
            if (moveNext) {
                _readPointer += length;
            }
            return value;
        } else {
            throw new Exception("Read byte array error!");
        }
    }

    public short readShort(boolean moveNext) throws Exception {
        if (_buffer.size() > _readPointer) {
            short value = ByteBuffer.wrap(_readableBuffer, _readPointer, 2).getShort();
            if (moveNext) {
                _readPointer += 2;
            }
            return value;
        } else {
            throw new Exception("Failed to read short");
        }
    }

    public int readInt(boolean moveNext) throws Exception {
        if (_buffer.size() > _readPointer) {
            int value = ByteBuffer.wrap(_readableBuffer, _readPointer, 4).getInt();
            if (moveNext) {
                _readPointer += 4;
            }
            return value;
        } else {
            throw new Exception("Failed to read int");
        }
    }

    public long readLong(boolean moveNext) throws Exception {
        if (_buffer.size() > _readPointer) {
            long value = ByteBuffer.wrap(_readableBuffer, _readPointer, 8).getLong();
            if (moveNext) {
                _readPointer += 8;
            }
            return value;
        } else {
            throw new Exception("Failed to read long");
        }
    }

    public float readFloat(boolean moveNext) throws Exception {
        if (_buffer.size() > _readPointer) {
            float value = ByteBuffer.wrap(_readableBuffer, _readPointer, 4).getFloat();
            if (moveNext) {
                _readPointer += 4;
            }
            return value;
        } else {
            throw new Exception("Failed to read float");
        }
    }

    public boolean readBool(boolean moveNext) throws Exception {
        if (_buffer.size() > _readPointer) {
            boolean value = (_readableBuffer[_readPointer] != 0);
            if (moveNext) {
                _readPointer += 1;
            }
            return value;
        } else {
            throw new Exception("Failed to read bool");
        }
    }

    public String readString(boolean moveNext) throws Exception {
        try {
            int length = readInt(false); // Get the length of the string
            String value = new String(_readableBuffer, _readPointer + 4, length, "ASCII"); // Convert the bytes to a string
            if (moveNext && value.length() > 0) {
                _readPointer += length + 4;
            }
            return value;
        } catch (Exception e) {
            throw new Exception("Failed to read string", e);
        }
    }

    protected void dispose(boolean isDisposing) {
        if (!_disposed) {
            if (isDisposing) {
                _buffer = null;
                _readableBuffer = null;
                _readPointer = 0;
            }
            _disposed = true;
        }
    }

    public void dispose() {
        dispose(true);
    }

    @Override
    public void close() throws Exception {
        dispose(true);
    }

    private byte[] toByteArray(List<Byte> list) {
        byte[] byteArray = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            byteArray[i] = list.get(i);
        }
        return byteArray;
    }

    public static ServerPacketType lookupPacket(int id) {
        for (ServerPacketType p : ServerPacketType.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return ServerPacketType.INVALID;
    }
}


