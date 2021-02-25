package net.royalsaga.minecraft.lobby.dnsstatistics.commands

import net.royalsaga.minecraft.lobby.dnsstatistics.color
import net.royalsaga.minecraft.lobby.dnsstatistics.isNotDate
import net.royalsaga.minecraft.lobby.dnsstatistics.storage.Storage
import net.royalsaga.minecraft.lobby.dnsstatistics.todayDate
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class DnsStatisticsCommand(private val storage: Storage) : CommandExecutor, TabCompleter {

    private val completion: MutableList<String> = mutableListOf("today", "total", "yyyy.MM.dd")

    override fun onCommand(sender: CommandSender, c: Command, l: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("dnsstatistics.command")) {
            sender.sendMessage("No permission!")
            return true
        }

        if (args.isEmpty() || args[0] == "total") {
            val statistics = storage.getTotalStatistics()

            if (statistics.isEmpty()) {
                sender.sendMessage("&cNo total statistics found.".color())
                return true
            }

            val (total, message) = statistics.format()

            sender.sendMessage("&7Total: &9$total".color())
            sender.sendMessage(message.color())
            return true
        }

        val date = args[0]

        if (date == "today") {
            val todayDate = todayDate()
            val statistics = storage.getStatistics(todayDate)

            if (statistics.isEmpty()) {
                sender.sendMessage("&cNo statistics found for today.".color())
                return true
            }

            val (total, message) = statistics.format()

            sender.sendMessage("&7Today ($todayDate): &9$total".color())
            sender.sendMessage(message.color())
            return true
        }

        if (date.isNotDate()) {
            sender.sendMessage("&c$date doesn't match the format yyyy.MM.dd".color())
            return true
        }

        val statistics = storage.getStatistics(date)

        if (statistics.isEmpty()) {
            sender.sendMessage("&cNo statistics found for $date.".color())
            return true
        }

        val (total, message) = statistics.format()

        sender.sendMessage("&7$date: &9$total".color())
        sender.sendMessage(message.color())
        return true
    }

    override fun onTabComplete(sender: CommandSender, c: Command, al: String, a: Array<out String> ): MutableList<String> {
        if (!sender.hasPermission("dnsstatistics.command")) {
            return mutableListOf()
        }

        return completion
    }

    private fun List<Pair<String, Int>>.format(): Pair<Int, String> {
        var total = 0
        val message = joinToString(separator = "\n") {
            total += it.second
            "&8* &7${it.first} &9${it.second}"
        }

        return total to message
    }

}