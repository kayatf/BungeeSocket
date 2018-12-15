package yt.syntax.bungeesocket.misc;

import com.google.common.collect.Iterators;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginDescription;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.Consumer;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 17:24
 */
@NoArgsConstructor ( access = AccessLevel.PRIVATE )
public class Spiget
{

    /* spiget version history endpoint (restful service) */
    private static final String SPIGET_VERSION_ENDPOINT = "https://api.spiget.org/v2/resources/%s/versions?size=500";

    /* spigot resource page url */
    private static final String SPIGOT_RESOURCE_URL = "https://www.spigotmc.org/resources/%s";

    /**
     * Check for updates
     *
     * @param resource      is the spigot resource id
     * @param description   is the {@link PluginDescription} of the plugin
     * @param commandSender is the command sender
     */
    public static void check (
            final int resource,
            final PluginDescription description,
            final CommandSender commandSender
    )
    {

        final CommandSender sender = commandSender != null ? commandSender : ProxyServer.getInstance ( ).getConsole ( );

        Helper.send ( sender, Helper.PREFIX + "Checking for new versions..." );
        try
        {
            request ( String.format ( SPIGET_VERSION_ENDPOINT, resource ), element -> {
                final JsonObject object = Iterators.getLast ( element.getAsJsonArray ( ).iterator ( ) ).getAsJsonObject ( );
                final String version = object.get ( "name" ).getAsString ( );

                if ( version.equals ( description.getVersion ( ) ) )
                {
                    Helper.send ( sender, Helper.PREFIX + "You're running the latest version!" );
                }
                else
                {

                    Helper.send ( sender, Helper.PREFIX + "§7An update to version §4%s §7is available!", version );

                    Helper.send (
                            sender,
                            Helper.PREFIX + "Download at: §4%s",
                            String.format ( SPIGOT_RESOURCE_URL, resource )
                    );

                }

            } );
        }
        catch ( final Exception e )
        {
            Helper.send ( sender, Helper.PREFIX + "§cCould not run version check!" );
        }
    }

    /**
     * Get {@link JsonElement} from restful service
     *
     * @param endpoint is the restful endpoint url
     * @param consumer is the result
     * @throws IOException
     */
    private static void request (
            final String endpoint,
            final Consumer < JsonElement > consumer
    ) throws IOException, JsonParseException
    {
        final URL url = new URL ( endpoint );
        final StringBuilder builder = new StringBuilder ( );
        try ( final BufferedReader reader = new BufferedReader ( new InputStreamReader ( url.openStream ( ) ) ) )
        {
            int i;
            while ( ( i = reader.read ( ) ) != - 1 ) builder.append ( ( char ) i );
        }
        finally
        {
            consumer.accept ( Helper.JSON_PARSER.parse ( builder.toString ( ) ) );
        }
    }

}
