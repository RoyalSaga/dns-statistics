package net.royalsaga.minecraft.lobby.dnsstatistics.storage

import net.royalsaga.minecraft.lobby.dnsstatistics.DnsStatisticsPlugin
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.logging.Level

class Storage(plugin: DnsStatisticsPlugin) {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    private val zoneId = ZoneId.of("Europe/Bucharest")

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

    fun increase(dns: String) {
        val path = dns.replace('.', '~')
            .replace(":25565", "")
            .replace(':', ';')
            .replace("tcpshield~", "")
        val today = dateTimeFormatter.format(Instant.now().atZone(zoneId))

        yaml["total.$path"] = yaml.getInt("total.$path") + 1
        yaml["$today.$path"] = yaml.getInt("$today.$path") + 1
        yaml.save(file)
    }

    fun getTotalStatistics(): List<Pair<String, Int>> {
        return getStatistics("total")
    }

    fun getStatistics(day: String): List<Pair<String, Int>> {
        val section = yaml.getConfigurationSection(day) ?: return emptyList()
        
        return section.getKeys(false)
            .mapNotNull { it.replace('~', '.').replace(';', ':') to section.getInt(it) }
            .sortedByDescending { it.second }
            .toList()
    }

}