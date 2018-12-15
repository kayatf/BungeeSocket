package yt.syntax.bungeesocket.bukkit;

import com.google.gson.JsonObject;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.client.SocketIOException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import yt.syntax.bungeesocket.api.RemoteCommand;
import yt.syntax.bungeesocket.misc.Channel;
import yt.syntax.bungeesocket.misc.Environment;
import yt.syntax.bungeesocket.misc.Helper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.UUID;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 12:11
 */
@Log4j2
public class BungeeSocketPlugin extends JavaPlugin
{

    /* instance of the plugin */
    @Getter
    private static BungeeSocketPlugin plugin;

    /* instance of the socket client */
    @Getter
    private Socket client;

    /* properties of the config */
    @Getter
    private Properties properties;

    /* class loader */
    private final ClassLoader loader = this.getClass ( ).getClassLoader ( );

    /*
     * Start routine of the plugin
     */
    @Override
    public void onEnable ( )
    {

        plugin = this;
        Helper.initLogger ( this.loader );
        Helper.ENVIRONMENT = Environment.BUKKIT;

        try
        {
            this.properties = Helper.initConfig ( this.loader, this.getDataFolder ( ) );
        }
        catch ( final IOException e )
        {
            e.printStackTrace ( );
            return;
        }

        this.getCommand ( "bukkitdispatch" ).setExecutor ( new BukkitDispatchCommand ( ) );

        final IO.Options options = new IO.Options ( );
        options.forceNew = true;
        options.reconnection = true;

        try
        {
            this.client = IO.socket ( String.format (
                    "http://%s:%s",
                    properties.getProperty ( "socket.hostname", "127.0.0.1" ),
                    properties.getProperty ( "socket.port", "17500" )
            ), options );
        }
        catch ( final URISyntaxException e )
        {
            e.printStackTrace ( );
            return;
        }

        this.client.on ( Channel.BUNGEE_OUTGOING_OBJECT.getName ( ), objects -> {
            log.info ( "Received incoming object from ProxyServer." );
            final JsonObject object = Helper.JSON_PARSER.parse ( ( String ) objects[ 0 ] ).getAsJsonObject ( );
            new IngoingObjectEvent ( object ).call ( );
        } );

        this.client.on ( Channel.BUNGEE_OUTGOING_COMMAND.getName ( ), objects -> {
            final RemoteCommand command = RemoteCommand.fromJson ( ( String ) objects[ 0 ] );
            final IngoingCommandEvent event = new IngoingCommandEvent ( command ).call ( );
            if ( ! event.isCancelled ( ) )
            {
                final String playerName = command.getPlayer ( );
                final boolean playerSet = playerName != null;
                final String key = command.getCommand ( );

                final Player player = playerSet ? this.getPlayer ( playerName ) : null;
                if ( playerSet && player == null || ! player.isOnline ( ) ) log.error ( String.format (
                        "Could not dispatch command '/%s' for '%s'. (Player doesn't exist!)",
                        key,
                        playerName
                ) );
                else
                {
                    Bukkit.dispatchCommand ( playerSet ? player : Bukkit.getConsoleSender ( ), key );
                    log.info ( String.format (
                            "Dispatched command '/%s' for player '%s'.",
                            key,
                            playerSet ? playerName.toUpperCase ( ) : "CONSOLE"
                    ) );
                }
            }
        } );

        this.client.connect ( );

    }

    /*
     * Shutdown routine of the plugin
     */
    @Override
    public void onDisable ( )
    {
        if ( this.client != null && this.client.connected ( ) ) this.client.close ( );
    }

    /**
     * Emit a json object to all spigot clients
     *
     * @param object is the json object
     */
    public void emit ( final Channel channel, final JsonObject object )
    {
        if ( this.client == null || ! this.client.connected ( ) )
            throw new RuntimeException ( new SocketIOException ( "Server is not connected!" ) );
        this.client.emit ( channel.getName ( ), object.toString ( ) );
    }

    /**
     * Gets {@link Player} from string (uuid or name)
     *
     * @param string is the uuid or name
     * @return is the player
     */
    private Player getPlayer ( final String string )
    {
        final Player player = Bukkit.getPlayer ( string );
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
        return Bukkit.getPlayer ( uuid );
    }
}
