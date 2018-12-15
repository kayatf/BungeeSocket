package yt.syntax.bungeesocket.bungee;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.handler.SocketIOException;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;
import yt.syntax.bungeesocket.misc.Channel;
import yt.syntax.bungeesocket.misc.Environment;
import yt.syntax.bungeesocket.misc.Helper;
import yt.syntax.bungeesocket.misc.Spiget;

import java.io.IOException;
import java.util.Properties;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 12:11
 */
public class BungeeSocketPlugin extends Plugin
{

    /* instance of the plugin */
    @Getter
    private static BungeeSocketPlugin plugin;

    /* instance of the socket server */
    @Getter
    private SocketIOServer server;

    /* class loader */
    private final ClassLoader loader = this.getClass ( ).getClassLoader ( );

    /*
     * Start routine of the plugin
     */
    @Override
    public void onEnable ( )
    {

        plugin = this;

        System.setProperty ( "bstats.relocatecheck", "false" );
        new Metrics ( plugin );

        Helper.initLogger ( this.loader );
        Helper.ENVIRONMENT = Environment.BUNGEECORD;

        final Properties properties;
        try
        {
            properties = Helper.initConfig ( this.loader, this.getDataFolder ( ) );
        }
        catch ( final IOException e )
        {
            e.printStackTrace ( );
            return;
        }

        ProxyServer.getInstance ( ).getPluginManager ( ).registerCommand ( plugin, new BungeeDispatchCommand ( ) );
        ProxyServer.getInstance ( ).getPluginManager ( ).registerCommand ( plugin, new BungeeSocketCommand ( ) );

        final Configuration config = new Configuration ( );
        config.setHostname ( properties.getProperty ( "socket.hostname", "127.0.0.1" ) );
        config.setPort ( Integer.parseInt ( properties.getProperty ( "socket.port", "17500" ) ) );

        this.server = new SocketIOServer ( config );

        this.server.addConnectListener ( new ConnectEventListener ( ) );
        this.server.addDisconnectListener ( new DisconnectEventListener ( ) );

        this.server.addEventListener (
                Channel.BUKKIT_OUTGOING_OBJECT.getName ( ),
                String.class,
                new BukkitOutgoingObjectEventListener ( )
        );

        this.server.addEventListener (
                Channel.BUKKIT_OUTGOING_COMMAND.getName ( ),
                String.class,
                new BukkitOutgoingCommandEventListener ( )
        );

        this.server.startAsync ( );

        if ( Boolean.valueOf ( properties.getProperty ( "updater", "true" ) ) )
        {
            new Thread ( ( ) -> {

                try
                {
                    Thread.sleep ( 1500L );
                }
                catch ( final InterruptedException ignored )
                {
                }

                Spiget.check ( Helper.SPIGOT_RESOURCE_ID, this.getDescription ( ), null );

            } ).start ( );
        }

    }

    /*
     * Shutdown routine of the plugin
     */
    @Override
    public void onDisable ( )
    {
        if ( this.server != null ) this.server.stop ( );
    }

    /**
     * Emit a json object to all spigot clients
     *
     * @param object is the json object
     */
    public void emit ( final Channel channel, final JsonObject object )
    {
        if ( this.server == null ) throw new SocketIOException ( "Server is not connected!" );
        this.server.getBroadcastOperations ( ).sendEvent ( channel.getName ( ), object.toString ( ) );
    }

}
