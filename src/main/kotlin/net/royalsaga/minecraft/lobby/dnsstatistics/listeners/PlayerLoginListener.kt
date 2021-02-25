package net.royalsaga.minecraft.lobby.dnsstatistics.listeners

import net.royalsaga.minecraft.lobby.dnsstatistics.storage.Storage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

class PlayerLoginListener(private val storage: Storage) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun PlayerLoginEvent.onEvent() {
        Bukkit.getScheduler().runTaskAsynchronously(storage.plugin, Runnable {
            if (result != PlayerLoginEvent.Result.ALLOWED || player.hasPlayedBefore()) {
                return@Runnable
            }

            storage.increase(hostname)
        })
    }

}