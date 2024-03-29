package packet;

import session.ClientSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ConcurrentLinkedQueue;


//a NIO handler for writing messages to socket channel
public class PacketWriter {

    private ClientSession clientSession;
    private ConcurrentLinkedQueue<ByteBuffer> queue = new ConcurrentLinkedQueue<>();

    //writes to the socket if there's no more message then de registers the OP_WRITE
    public PacketWriter(ClientSession clientSession) {
        this.clientSession = clientSession;
    }

    public void dequeueAndWrite(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = queue.peek();

        //if the queue is empty then we no longer need to write to it
        if(byteBuffer == null){
            key.interestOps(SelectionKey.OP_READ);
            return;
        }

        System.out.println(" Send: " + new String(byteBuffer.array()));

        //write as much as it can to the socketChannel
        clientSession.writeToSocket(byteBuffer);

        //if all of the message has been sent remove it from the queue
        if(!byteBuffer.hasRemaining()){
            queue.poll();
        }else {
            byteBuffer.compact();
        }

    }

    //enqueues a message to be written when ever possible
    public void enqueueMessage(String msg){
        ByteBuffer buffer = ByteBuffer.allocate(msg.length() + 1);
        buffer.put(msg.getBytes());
//        buffer.put((byte) 0x00);
        buffer.flip();
        queue.add(buffer);
        clientSession.getMainSelector().wakeup();
    }

    public void enqueuePacket(Packet packet) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(packet.getBytes())
;
        buffer.flip();
        queue.add(buffer);
        clientSession.getMainSelector().wakeup();
    }

}

