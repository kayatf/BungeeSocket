package yt.syntax.bungeesocket.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 12:18
 */
@NoArgsConstructor ( access = AccessLevel.PRIVATE )
public class Helper
{

    /* environment of the plugin */
    public static Environment ENVIRONMENT;

    /* instance of gson */
    public static final Gson GSON = new GsonBuilder ( ).serializeNulls ( ).create ( );

    /* instance of the json parser */
    public static final JsonParser JSON_PARSER = new JsonParser ( );

    /* prefix of the plugin */
    public static final String PREFIX = "§8[§cBungeeSocket§8]§7 ";

    /* key for defining a player */
    public static final String PLAYER_KEY = "--player=";

    /* resource id of the plugin */
    public static final int SPIGOT_RESOURCE_ID = 38376; // Todo change resource id to own one haha xd

    /**
     * Joins multiple strings together
     *
     * @param strings are the strings
     * @return is the output string
     */
    public static String join ( final String... strings )
    {
        final StringJoiner joiner = new StringJoiner ( " " );
        for ( final String string : strings ) joiner.add ( string );
        return joiner.toString ( );
    }

    /**
     * Initializes the logger
     *
     * @param loader is the class loader
     */
    public static void initLogger ( final ClassLoader loader )
    {
        try
        {
            Configurator.setRootLevel ( Level.INFO );
            try ( final InputStream stream = loader.getResourceAsStream ( "log4j2.xml" ) )
            {
                Configurator.initialize ( loader, new ConfigurationSource ( stream ) );
            }
        }
        catch ( final IOException e )
        {
            e.printStackTrace ( );
        }
    }

    /**
     * Loads config from properties file
     *
     * @param loader is the class loader
     * @param folder is the data folder
     * @return are the properties
     * @throws IOException
     */
    public static Properties initConfig ( final ClassLoader loader, final File folder ) throws IOException
    {
        folder.mkdirs ( );
        final File file = new File ( folder, "config.properties" );
        if ( ! file.exists ( ) )
        {
            try ( final InputStream stream = loader.getResourceAsStream ( file.getName ( ) ) )
            {
                Files.copy ( stream, file.toPath ( ) );
            }
        }
        try ( final FileReader reader = new FileReader ( file ) )
        {
            final Properties properties = new Properties ( );
            properties.load ( reader );
            return properties;
        }
    }

    /**
     * Send message to {@link net.md_5.bungee.api.CommandSender}
     *
     * @param sender  is the command sender
     * @param text    is the message
     * @param strings can be arguments
     */
    public static void send (
            final net.md_5.bungee.api.CommandSender sender,
            final String text,
            final Object... strings
    )
    {
        sender.sendMessage ( new TextComponent ( String.format ( text, strings ) ) );
    }

    /**
     * Send message to {@link org.bukkit.entity.Player}
     *
     * @param sender  is the command sender
     * @param text    is the message
     * @param strings can be arguments
     */
    public static void send (
            final org.bukkit.command.CommandSender sender,
            final String text,
            final Object... strings
    )
    {
        sender.sendMessage ( String.format ( text, strings ) );
    }

}
