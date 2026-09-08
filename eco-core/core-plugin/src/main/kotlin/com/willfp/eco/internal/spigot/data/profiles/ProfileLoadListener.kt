package com.willfp.eco.internal.spigot.data.profiles

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.util.PlayerUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import io.papermc.paper.event.connection.configuration.PlayerConnectionInitialConfigureEvent
import org.bukkit.event.player.PlayerQuitEvent

class ProfileLoadListener(
    private val plugin: EcoPlugin,
    private val handler: ProfileHandler
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onLogin(event: PlayerConnectionInitialConfigureEvent) {
        handler.unloadPlayer(requireNotNull(event.connection.profile.id))
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onLeave(event: PlayerQuitEvent) {
        handler.unloadPlayer(event.player.uniqueId)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        plugin.scheduler.on(event.player).runLater(5) {
            PlayerUtils.updateSavedDisplayName(event.player)
        }
    }
}
