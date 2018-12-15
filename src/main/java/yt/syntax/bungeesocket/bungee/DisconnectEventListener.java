package yt.syntax.bungeesocket.bungee;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.log4j.Log4j2;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 14:56
 */
@Log4j2
public class DisconnectEventListener implements DisconnectListener
{
    @Override
    public void onDisconnect ( final SocketIOClient client )
    {
        log.info ( String.format ( "Server from %s disconnected!", client.getRemoteAddress ( ) ) );
    }
}
