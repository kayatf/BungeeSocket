package yt.syntax.bungeesocket.misc;

import lombok.Getter;

/**
 * SpigotMC
 *
 * @author Daniel Riegler
 * created on 07.11.2018 / 14:19
 */
public enum Channel
{
    BUNGEE_OUTGOING_COMMAND ( "BungeeSocket@BungeeOutgoingCommand" ),
    BUNGEE_OUTGOING_OBJECT ( "BungeeSocket@BungeeOutgoingObject" ),
    BUKKIT_OUTGOING_COMMAND ( "BungeeSocket@BukkitOutgoingCommand" ),
    BUKKIT_OUTGOING_OBJECT ( "BungeeSocket@BukkitOutgoingObject" );

    @Getter
    protected final String name;

    Channel ( final String name )
    {
        this.name = name;
    }
}
