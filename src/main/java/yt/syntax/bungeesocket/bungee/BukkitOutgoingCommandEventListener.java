package yt.syntax.bungeesocket.bungee;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import lombok.extern.log4j.Log4j2;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import yt.syntax.bungeesocket.api.RemoteCommand;

import java.util.UUID;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 15:08
 */
@Log4j2
public class BukkitOutgoingCommandEventListener implements DataListener < String >
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
        final RemoteCommand command = RemoteCommand.fromJson ( string );
        final IngoingCommandEvent event = new IngoingCommandEvent ( command ).call ( );
        if ( ! event.isCancelled ( ) )
        {
            final String playerName = command.getPlayer ( );
            final boolean playerSet = playerName != null;
            final String key = command.getCommand ( );

            final ProxiedPlayer player = playerSet ? this.getPlayer ( playerName ) : null;
            if ( playerSet && player == null || ! player.isConnected ( ) ) log.error ( String.format (
                    "Could not dispatch command '/%s' for '%s'. (Player doesn't exist!)",
                    key,
                    playerName
            ) );
            else
            {
                ProxyServer.getInstance ( ).getPluginManager ( ).dispatchCommand (
                        playerSet ? player : ProxyServer.getInstance ( ).getConsole ( ),
                        key
                );
                log.info ( String.format (
                        "Dispatched command '/%s' for player '%s'.",
                        key,
                        playerSet ? playerName.toUpperCase ( ) : "CONSOLE"
                ) );
            }
        }
    }

    /**
     * Gets {@link ProxiedPlayer} from string (uuid or name)
     *
     * @param string is the uuid or name
     * @return is the player
     */
    private ProxiedPlayer getPlayer ( final String string )
    {
        final ProxiedPlayer player = ProxyServer.getInstance ( ).getPlayer ( string );
        if ( player != null ) return player;

        final UUID uuid;
        try
        {
            uuid = UUID.fromString ( string );
        }
        catch ( final IllegalArgumentException e )
        {
            return null;
        }
        return ProxyServer.getInstance ( ).getPlayer ( uuid );
    }

}
