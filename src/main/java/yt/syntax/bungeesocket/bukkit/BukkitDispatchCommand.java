package yt.syntax.bungeesocket.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import yt.syntax.bungeesocket.api.BungeeSocket;
import yt.syntax.bungeesocket.api.RemoteCommand;
import yt.syntax.bungeesocket.misc.Helper;

import java.util.Arrays;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 18:28
 */
public class BukkitDispatchCommand implements CommandExecutor
{

    /**
     * On execution of the command
     *
     * @param sender  is the command sender
     * @param command is the command
     * @param s       is the label
     * @param args    are the arguments
     */
    @Override
    public boolean onCommand ( final CommandSender sender, final Command command, final String s, final String[] args )
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
                        command.getName ( ),
                        Helper.PLAYER_KEY
                );
                return true;
            }
            String player = null;
            if ( args[ 0 ].startsWith ( Helper.PLAYER_KEY ) ) player = args[ 0 ].replace ( Helper.PLAYER_KEY, "" );
            final String key = Helper.join ( player != null ? Arrays.copyOfRange ( args, 1, args.length ) : args );
            BungeeSocket.sendCommand ( new RemoteCommand ( key, player ) );
            Helper.send (
                    sender,
                    "Sending bungeecord command '/%s' to '%s'.",
                    key,
                    player != null ? player.toUpperCase ( ) : "CONSOLE"
            );
        }
        return true;
    }
}
