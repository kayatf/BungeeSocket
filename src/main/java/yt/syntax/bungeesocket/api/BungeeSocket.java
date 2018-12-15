package yt.syntax.bungeesocket.api;

import com.corundumstudio.socketio.Configuration;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import yt.syntax.bungeesocket.misc.Channel;
import yt.syntax.bungeesocket.misc.Environment;
import yt.syntax.bungeesocket.misc.Helper;

import java.net.InetSocketAddress;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 13:28
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BungeeSocket
{

    /**
     * Sends {@link JsonObject} to either the proxy server or the spigot servers
     *
     * @param object  is the json object
     * @param channel is the event channel
     */
    private static void emit ( final Channel channel, final JsonObject object )
    {
        try
        {
            switch ( Helper.ENVIRONMENT )
            {
                case BUNGEECORD:
                    getBungeeInstance ( ).emit ( channel, object );
                    break;
                case BUKKIT:
                    getBukkitInstance ( ).emit ( channel, object );
                    break;
            }
        }
        catch ( final Exception e )
        {
            log.error ( String.format ( "Could not emit object %s!", String.valueOf ( object ) ), e );
        }
    }

    /**
     * Get the instance of the bungeecord plugin
     *
     * @return is the plugin
     */
    private static yt.syntax.bungeesocket.bungee.BungeeSocketPlugin getBungeeInstance ( )
    {
        return yt.syntax.bungeesocket.bungee.BungeeSocketPlugin.getPlugin ( );
    }

    /**
     * Get the instance of the bukkit plugin
     *
     * @return is the plugin
     */
    private static yt.syntax.bungeesocket.bukkit.BungeeSocketPlugin getBukkitInstance ( )
    {
        return yt.syntax.bungeesocket.bukkit.BungeeSocketPlugin.getPlugin ( );
    }

    /**
     * Sends {@link JsonObject} to either the proxy server or the spigot servers
     *
     * @param object is the json object
     */
    public static void sendObject ( final JsonObject object )
    {
        final Channel channel = Helper.ENVIRONMENT == Environment.BUNGEECORD
                ? Channel.BUNGEE_OUTGOING_OBJECT
                : Channel.BUKKIT_OUTGOING_OBJECT;
        emit ( channel, object );
    }

    /**
     * Sends {@link RemoteCommand} to either the proxy server or the spigot servers
     *
     * @param command is the command to dispatch
     */
    public static void sendCommand ( final RemoteCommand command )
    {
        final Channel channel = Helper.ENVIRONMENT == Environment.BUNGEECORD
                ? Channel.BUNGEE_OUTGOING_COMMAND
                : Channel.BUKKIT_OUTGOING_COMMAND;
        emit ( channel, command.toJson ( ) );
    }

    /**
     * Get {@link InetSocketAddress} from the socket server
     *
     * @return is the address
     */
    public static InetSocketAddress getServer ( )
    {
        InetSocketAddress address = null;
        switch ( Helper.ENVIRONMENT )
        {
            case BUNGEECORD:
                final var bungee = getBungeeInstance ( );
                if ( bungee.getServer ( ) != null )
                {
                    final Configuration config = bungee.getServer ( ).getConfiguration ( );
                    address = InetSocketAddress.createUnresolved ( config.getHostname ( ), config.getPort ( ) );
                }
                break;
            case BUKKIT:
                final var bukkit = getBukkitInstance ( );
                final String hostname = bukkit.getProperties ( ).getProperty ( "socket.hostname", "127.0.0.1" );
                final int port = Integer.parseInt ( bukkit.getProperties ( ).getProperty (
                        "socket.port",
                        "17500"
                ) );
                address = InetSocketAddress.createUnresolved ( hostname, port );
                break;
        }
        return address;
    }

}
