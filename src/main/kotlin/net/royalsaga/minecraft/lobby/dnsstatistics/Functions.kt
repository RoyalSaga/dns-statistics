package net.royalsaga.minecraft.lobby.dnsstatistics

import org.bukkit.ChatColor
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

/**
 * The [ZoneId] of my server used to grab [todayDate],
 * change it after your willing
 */
private val zoneId = ZoneId.of("Europe/Bucharest")

private val datePattern = Pattern.compile("^(?:\\d{4})\\.(?:[0-1]\\d)\\.(?:[0-3]\\d)$")

/**
 * Get the today date formatted as [dateTimeFormatter]
 */
internal fun todayDate() = dateTimeFormatter.format(Instant.now().atZone(zoneId))

/**
 * Color a string using [ChatColor.translateAlternateColorCodes]
 */
internal fun String.color() = ChatColor.translateAlternateColorCodes('&', this)

/**
 * Check if a string doesn't match [datePattern]
 */
internal fun String.isNotDate() = !datePattern.matcher(this).find()