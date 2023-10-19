package kiraririria.discordipc.entities.pipe;

import kiraririria.discordipc.IPCClient;
import kiraririria.discordipc.entities.Callback;
import kiraririria.discordipc.entities.Packet;
import org.json.JSONException;
import org.json.JSONObject;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.HashMap;

public class UnixPipe extends Pipe
{ private final AFUNIXSocket socket;

    UnixPipe(IPCClient ipcClient, HashMap<String, Callback> callbacks, String location) throws IOException
    {
        super(ipcClient, callbacks);

        socket = AFUNIXSocket.newInstance();
        socket.connect(AFUNIXSocketAddress.of(Paths.get(location)));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public Packet read() throws IOException, JSONException
    {
        InputStream is = socket.getInputStream();

        while(is.available() == 0 && status == PipeStatus.CONNECTED)
        {
            try {
                Thread.sleep(50);
            } catch(InterruptedException ignored) {}
        }

        if(status==PipeStatus.DISCONNECTED)
            throw new IOException("Disconnected!");

        if(status==PipeStatus.CLOSED)
            return new Packet(Packet.OpCode.CLOSE, null);

        byte[] d = new byte[8];
        is.read(d);
        ByteBuffer bb = ByteBuffer.wrap(d);

        Packet.OpCode op = Packet.OpCode.values()[Integer.reverseBytes(bb.getInt())];
        d = new byte[Integer.reverseBytes(bb.getInt())];

        is.read(d);
        Packet p = new Packet(op, new JSONObject(new String(d)));
        if(listener != null)
            listener.onPacketReceived(ipcClient, p);
        return p;
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        socket.getOutputStream().write(b);
    }

    @Override
    public void close() throws IOException
    {
        send(Packet.OpCode.CLOSE, new JSONObject(), null);
        status = PipeStatus.CLOSED;
        socket.close();
    }
}
