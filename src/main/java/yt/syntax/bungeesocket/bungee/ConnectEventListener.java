package yt.syntax.bungeesocket.bungee;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import lombok.extern.log4j.Log4j2;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 14:55
 */
@Log4j2
public class ConnectEventListener implements ConnectListener
{
    @Override
    public void onConnect ( final SocketIOClient client )
    {
        log.info ( String.format ( "Server connected from %s.", client.getRemoteAddress ( ) ) );
    }
}
