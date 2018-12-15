package yt.syntax.bungeesocket.bukkit;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import yt.syntax.bungeesocket.api.RemoteCommand;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 16:39
 */
@Getter
@RequiredArgsConstructor
public class IngoingCommandEvent extends Event implements Cancellable
{

    /* handler list of the event */
    private final HandlerList handlers = new HandlerList ( );

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
        Bukkit.getPluginManager ( ).callEvent ( this );
        return this;
    }

}
