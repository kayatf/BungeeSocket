package yt.syntax.bungeesocket.bukkit;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 16:36
 */
@AllArgsConstructor
public class IngoingObjectEvent extends Event
{

    /* handler list of the event */
    @Getter
    private final HandlerList handlers = new HandlerList ( );

    /* json object of the ingoing message */
    @Getter
    private final JsonObject object;

    /*
     * Call the event
     */
    public void call ( )
    {
        Bukkit.getPluginManager ( ).callEvent ( this );
    }

}
