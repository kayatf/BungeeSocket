package yt.syntax.bungeesocket.bungee;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import yt.syntax.bungeesocket.api.RemoteCommand;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 15:24
 */
@Getter
@RequiredArgsConstructor
public class IngoingCommandEvent extends Event implements Cancellable
{

    /* is the event cancelled */
    @Setter
    private boolean cancelled = false;

    /* command object */
    @NonNull
    private final RemoteCommand command;

    /*
     * Call the event
     */
    public IngoingCommandEvent call ( )
    {
        return ProxyServer.getInstance ( ).getPluginManager ( ).callEvent ( this );
    }

}
