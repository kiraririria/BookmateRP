package kiraririria.bookmaterp;

import kiraririria.discordipc.IPCClient;
import kiraririria.discordipc.IPCListener;
import kiraririria.discordipc.entities.RichPresence;
import kiraririria.discordipc.exceptions.NoDiscordClientException;

import java.time.OffsetDateTime;

public class DiscordIPC
{
    public static IPCClient Instance = null;
    public static RichPresence.Builder Builder = null;
    public static RichPresence Original = null;
    public static IPCClient StartIPC()
    {
        try
        {
            Instance = new IPCClient(1163124229525479425L);
            Instance.setListener(new IPCListener(){
                @Override
                public void onReady(IPCClient client)
                {
                    Builder = new RichPresence.Builder();
                    Builder.setState("Яндекс+Букмейт!")
                            .setDetails("Выбирает книгу")
                            .setStartTimestamp(OffsetDateTime.now())
                            .setSmallImage("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/App-icon-flat.svg/300px-App-icon-flat.svg.png", "bookmate!")
                            .setLargeImage("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/App-icon-flat.svg/300px-App-icon-flat.svg.png", "bookmate!")
                            .setMatchSecret("СекретныйМатч")
                            .setSpectateSecret("Секрет");
                    Original = Builder.build();
                    client.sendRichPresence(Original);
                }
            });
            Instance.connect();
        }
        catch (NoDiscordClientException e)
        {
            e.printStackTrace();
        }
        return Instance;
    }
}
