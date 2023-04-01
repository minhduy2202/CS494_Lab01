package Client.Packet;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static Client.Constants.BUFFER_SIZE;

public class PacketBuffer {
    private final ByteBuffer buffer;

    public PacketBuffer(){
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }
    public PacketBuffer(int id){
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.buffer.putInt(id);
    }

    public void putInt(int value){
        this.buffer.putInt(value);
    }

    public int readInt(){
        return this.buffer.getInt();
    }

    public void putString(String str, Charset charset){
        byte[] bytes = str.getBytes(charset);
        this.buffer.putInt(bytes.length);
        buffer.put(bytes);
        System.out.println("Str size " + bytes.length);
    }

    public String readString(Charset charset){
        int length = this.buffer.getInt();
        byte[] bytes = new byte[length];
        this.buffer.get(bytes);
        return new String(bytes, charset);
    }

    public ByteBuffer getBuffer() {
        return buffer;
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
