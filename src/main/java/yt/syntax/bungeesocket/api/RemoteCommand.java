package yt.syntax.bungeesocket.api;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import yt.syntax.bungeesocket.misc.Helper;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 13:30
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class RemoteCommand
{

    /* command to execute without slash */
    @NonNull
    private final String command;

    /* player for whom the command should be executed */
    private String player;

    /**
     * Serializes the object into a json string
     *
     * @return is the json string
     */
    public JsonObject toJson ( )
    {
        return Helper.GSON.toJsonTree ( this ).getAsJsonObject ( );
    }

    /**
     * Gets command from serialized json string
     *
     * @param json is the serialized json string
     */
    public static RemoteCommand fromJson ( final String json )
    {
        return Helper.GSON.fromJson ( json, RemoteCommand.class );
    }

}
