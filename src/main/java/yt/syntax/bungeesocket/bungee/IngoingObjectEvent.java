package yt.syntax.bungeesocket.bungee;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Event;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 14:59
 */
@AllArgsConstructor
public class IngoingObjectEvent extends Event
{

    /* json object of the ingoing message */
    @Getter
    private final JsonObject object;

    /*
     * Call the event
     */
    public void call ( )
    {
        ProxyServer.getInstance ( ).getPluginManager ( ).callEvent ( this );
    }
}
