package yt.syntax.bungeesocket.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginDescription;
import yt.syntax.bungeesocket.misc.Helper;
import yt.syntax.bungeesocket.misc.Spiget;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 18:36
 */
public class BungeeSocketCommand extends Command
{

    /* instance of the plugin */
    private final BungeeSocketPlugin plugin = BungeeSocketPlugin.getPlugin ( );

    /*
     * Set the command information
     */
    public BungeeSocketCommand ( )
    {
        super ( "bungeesocket", "bungeesocket.command", "bsocket", "bs" );
    }

    /**
     * On execution of the command
     *
     * @param sender is the command sender
     * @param args   are the arguments
     */
    @Override
    public void execute ( final CommandSender sender, final String[] args )
    {

        final PluginDescription description = this.plugin.getDescription ( );

        if ( args.length != 0 && args[ 0 ].equalsIgnoreCase ( "update" ) ) Spiget.check (
                Helper.SPIGOT_RESOURCE_ID,
                description,
                sender
        );
        else
        {
            Helper.send (
                    sender,
                    Helper.PREFIX + "§7BungeeSocket §4%s §7by §4%s§7:",
                    description.getVersion ( ),
                    description.getAuthor ( )
            );
            Helper.send ( sender, Helper.PREFIX + "Check for newer versions with §4/%s update", this.getName ( ) );
            Helper.send (
                    sender,
                    Helper.PREFIX + "Server running at port §4%s",
                    this.plugin.getServer ( ).getConfiguration ( ).getPort ( )
            );
            Helper.send (
                    sender,
                    Helper.PREFIX + "Nodes connected: §4%s",
                    this.plugin.getServer ( ).getAllClients ( ).size ( )
            );
        }
    }
}
