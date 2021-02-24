package net.royalsaga.minecraft.lobby.dnsstatistics

import net.royalsaga.minecraft.lobby.dnsstatistics.commands.DnsStatisticsCommand
import net.royalsaga.minecraft.lobby.dnsstatistics.listeners.PlayerLoginListener
import net.royalsaga.minecraft.lobby.dnsstatistics.storage.Storage
import org.bukkit.plugin.java.JavaPlugin

class DnsStatisticsPlugin : JavaPlugin() {

    override fun onEnable() {
        val storage = Storage(this)

        server.pluginManager.registerEvents(PlayerLoginListener(storage), this)
        getCommand("dnsstatistics")?.setExecutor(DnsStatisticsCommand(storage))
    }

}