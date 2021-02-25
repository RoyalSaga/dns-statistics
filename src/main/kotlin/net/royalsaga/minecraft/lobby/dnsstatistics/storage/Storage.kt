package net.royalsaga.minecraft.lobby.dnsstatistics.storage

import net.royalsaga.minecraft.lobby.dnsstatistics.DnsStatisticsPlugin
import net.royalsaga.minecraft.lobby.dnsstatistics.todayDate
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.logging.Level

class Storage(val plugin: DnsStatisticsPlugin) {

    private val file = File(plugin.dataFolder, "statistics.yml")
    private var yaml: YamlConfiguration

    init {
        if (!file.exists()) {
            try {
                file.parentFile.mkdirs()
                file.createNewFile()
            } catch (e: IOException) {
                plugin.logger.log(Level.SEVERE, "An error occurred while creating ${file.path}", e)
            }
        }

        yaml = YamlConfiguration.loadConfiguration(file)
    }

    /**
     * Increase by 1 the amount of users that used the given address
     * @param address address used to join the server
     */
    fun increase(address: String) {
        val path = address.replace('.', '~')
            .replace(":25565", "")
            .replace(':', ';')
            .replace("tcpshield~", "")

        val today = todayDate()

        yaml["total.$path"] = yaml.getInt("total.$path") + 1
        yaml["$today.$path"] = yaml.getInt("$today.$path") + 1
        yaml.save(file)
    }

    /**
     * Get the total statistics ordered by the amount of users
     * @see [getStatistics]
     * @return list of Pair<Address, AmountOfUsers>
     */
    fun getTotalStatistics(): List<Pair<String, Int>> {
        return getStatistics("total")
    }

    /**
     * Get the statistics of a certain day ordered by amount of users
     * @param day day of which statistics will be calculated
     * @return list of Pair<Address, AmountOfUsers>
     */
    fun getStatistics(day: String): List<Pair<String, Int>> {
        val section = yaml.getConfigurationSection(day) ?: return emptyList()

        return section.getKeys(false)
            .mapNotNull { it.replace('~', '.').replace(';', ':') to section.getInt(it) }
            .sortedByDescending { it.second }
            .toList()
    }

}