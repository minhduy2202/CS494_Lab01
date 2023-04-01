package Client.Packet;

public  enum ServerPacketType{
    INVALID(-1),
    RECEIVE_TEST_DATA(0);

    private int packetId;

    private ServerPacketType(int packetId) {
        this.packetId = packetId;
    }

    public int getId() {
        return packetId;
    }
}
