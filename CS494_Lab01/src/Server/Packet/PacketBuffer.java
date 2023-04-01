package Server.Packet;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static Server.Constants.BUFFER_SIZE;

public class PacketBuffer {
    private final ByteBuffer buffer;

    public PacketBuffer(){
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }
    public PacketBuffer(int id){
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.buffer.putInt(id);
    }

    public PacketBuffer(ByteBuffer buffer){
        this.buffer = buffer;
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

//    public void setBuffer(ByteBuffer buffer){
//        this.buffer = buffer;
//    }
    public static ClientPacketType lookupPacket(int id) {
        for (ClientPacketType p : ClientPacketType.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return ClientPacketType.INVALID;
    }
}


