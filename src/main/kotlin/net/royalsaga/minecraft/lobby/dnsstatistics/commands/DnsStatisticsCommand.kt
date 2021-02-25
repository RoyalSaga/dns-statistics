package net.royalsaga.minecraft.lobby.dnsstatistics.commands

import net.royalsaga.minecraft.lobby.dnsstatistics.storage.Storage
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class DnsStatisticsCommand(private val storage: Storage) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("dnsstatistics.command")) {
            sender.sendMessage("No permission!")
            return true
        }

        var total = 0
        val message = storage.getTotalStatistics().joinToString(separator = "\n") {
            total += it.second
            "&8* &7${it.first} &9${it.second}"
        }

        sender.sendMessage("&7Total: &9$total".color())
        sender.sendMessage(message.color())
        return true
    }

    private fun String.color(): String = ChatColor.translateAlternateColorCodes('&', this)

}