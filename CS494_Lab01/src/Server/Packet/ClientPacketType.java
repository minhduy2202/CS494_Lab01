package Server.Packet;

public enum ClientPacketType{
    INVALID(-1),
    SEND_TEST_DATA(0),
    HAND_SHAKE_SERVER(1),
    SEND_USER_NAME(2),
    GIVE_ANSWER(3);

    private int packetId;

    private ClientPacketType(int packetId) {
        this.packetId = packetId;
    }

    public int getId() {
        return packetId;
    }
}