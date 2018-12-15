package yt.syntax.bungeesocket.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;
import yt.syntax.bungeesocket.api.BungeeSocket;
import yt.syntax.bungeesocket.api.RemoteCommand;
import yt.syntax.bungeesocket.misc.Helper;

import java.util.Arrays;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 15:36
 */
public class BungeeDispatchCommand extends Command
{
    /*
     * Set the command name
     */
    public BungeeDispatchCommand ( )
    {
        super ( "bungeedispatch" );
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
        if ( ! ( sender instanceof ConsoleCommandSender ) )
        {
            Helper.send ( sender, Helper.PREFIX + "§cFor security reasons only the console can execute this command." );
        }
        else
        {
            if ( args.length == 0 )
            {
                Helper.send (
                        sender,
                        Helper.PREFIX + "§cCorrect usage » /%s (%sName) <command without slash>",
                        this.getName ( ),
                        Helper.PLAYER_KEY
                );
                return;
            }
            String player = null;
            if ( args[ 0 ].startsWith ( Helper.PLAYER_KEY ) ) player = args[ 0 ].replace ( Helper.PLAYER_KEY, "" );
            final String key = Helper.join ( player != null ? Arrays.copyOfRange ( args, 1, args.length ) : args );
            BungeeSocket.sendCommand ( new RemoteCommand ( key, player ) );
            Helper.send (
                    sender,
                    "Sending bukkit command '/%s' to '%s'.",
                    key,
                    player != null ? player.toUpperCase ( ) : "CONSOLE"
            );
        }
    }
}
