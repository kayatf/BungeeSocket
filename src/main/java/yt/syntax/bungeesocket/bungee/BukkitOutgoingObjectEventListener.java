package yt.syntax.bungeesocket.bungee;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import yt.syntax.bungeesocket.misc.Helper;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 15:03
 */
@Log4j2
public class BukkitOutgoingObjectEventListener implements DataListener < String >
{

    /**
     * On incoming event
     *
     * @param client  is the socket client
     * @param string  is the result as string
     * @param request is the request
     * @throws Exception
     */
    @Override
    public void onData ( final SocketIOClient client, final String string, final AckRequest request ) throws Exception
    {
        log.info ( String.format ( "Received incoming object from %s.", client.getRemoteAddress ( ) ) );
        final JsonObject object = Helper.JSON_PARSER.parse ( string ).getAsJsonObject ( );
        new IngoingObjectEvent ( object ).call ( );
    }

}
