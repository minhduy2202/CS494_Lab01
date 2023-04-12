package packet;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

public class Packet {
    private int packetId;
    private int dataLength = 0;
    private HashMap<String, String> data = null;

    public Packet(int packetId) {
        this.packetId = packetId;
        this.data = new HashMap<>();

        updateDataLength();
    }

    public Packet(int packetId, HashMap<String, String> data) {
        this.packetId = packetId;
        this.data = data;

        updateDataLength();
    }

    public Packet(byte[] bytes) {
        int packetIdStrLength = ByteBuffer.wrap(bytes, 0, 4).getInt();
        String packetIdStr = new String(bytes, 4, packetIdStrLength);

        int dataLengthStrLength = ByteBuffer.wrap(bytes, 4 + packetIdStrLength, 4).getInt();
        String dataLengthStr = new String(bytes, 8 + packetIdStrLength, dataLengthStrLength);

        this.packetId = Integer.parseInt(packetIdStr);
        this.dataLength = Integer.parseInt(dataLengthStr);

        this.data = new HashMap<>();

        String dataString = new String(bytes, 8 + packetIdStrLength + dataLengthStrLength, dataLength);
        int start_idx = dataString.indexOf('{');
        int end_idx = dataString.lastIndexOf('}');

        if (start_idx < end_idx) {
            String value = dataString.substring(start_idx + 1, end_idx);
            String[] keyValuePairs = value.split(",");

            for (String pair : keyValuePairs) {
                String[] entry = pair.split("=");
                if (entry.length < 2) {
                    continue;
                }
                // join all the values after the first one
                this.data.put(
                        entry[0].trim(),
                        String.join(
                                "=",
                                Arrays.copyOfRange(entry, 1, entry.length)).trim());
            }
        }
        updateDataLength();
    }

    @Override
    public String toString() {
        return "Packet{" +
                "packageId=" + packetId +
                ", data='" + data + '\'' +
                '}';
    }

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
        updateDataLength();
    }

    public int getPacketSize() {
        updateDataLength();
        return dataLength + 8 + String.valueOf(packetId).length() + String.valueOf(dataLength).length();
    }

    public void addKey(String key, String value) {
        this.data.put(key, value);
        updateDataLength();
    }

    public void removeKey(String key) {
        this.data.remove(key);
        updateDataLength();
    }

    public String getKey(String key) {
        return this.data.get(key);
    }

    public void updateKey(String key, String value) {
        this.data.replace(key, value);
        updateDataLength();
    }

    public byte[] getBytes() {
        updateDataLength();

        String packetIdStr = String.valueOf(this.packetId);
        int packetIdStrSize = packetIdStr.length();
        String dataLengthStr = String.valueOf(this.dataLength);
        int dataLengthStrSize = dataLengthStr.length();

        byte[] bytes = new byte[packetIdStrSize + dataLengthStrSize + dataLength + 8];

        // first 4 bytes is length of packet id string
        ByteBuffer.wrap(bytes, 0, 4).putInt(packetIdStrSize);

        // next is the packet id string
        ByteBuffer.wrap(bytes, 4, packetIdStrSize).put(packetIdStr.getBytes());

        // next 4 bytes is length of data length string
        ByteBuffer.wrap(bytes, 4 + packetIdStrSize, 4).putInt(dataLengthStrSize);

        // next is the data length string
        ByteBuffer.wrap(
                bytes,
                8 + packetIdStrSize,
                dataLengthStrSize).put(dataLengthStr.getBytes());

        // next is the data string
        ByteBuffer.wrap(
                bytes,
                8 + packetIdStrSize + dataLengthStrSize,
                dataLength).put(data.toString().getBytes());

        assert bytes.length == 8 + packetIdStrSize + dataLengthStrSize + dataLength;

        return bytes;
    }

    public byte[] toBytes() {
        return getBytes();
    }

    private void updateDataLength() {
        this.dataLength = data.toString().getBytes().length;
    }
}
