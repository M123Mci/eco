package com.willfp.eco.internal.spigot.proxy.v26_2.common.packet

import com.willfp.eco.internal.spigot.proxy.v26_2.common.toNMS
import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.internal.spigot.proxy.v26_2.common.packet.display.PacketWindowItems
import com.willfp.eco.internal.spigot.proxy.v26_2.common.packet.display.frame.clearFrame
import com.willfp.eco.internal.spigot.proxy.v26_2.common.packet.display.frame.clearFrames
import com.willfp.eco.internal.spigot.proxy.v26_2.packet.PacketContainerClick
import com.willfp.eco.internal.spigot.proxy.v26_2.packet.PacketSetCursorItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

private const val BASE_NAME = "packet_handler"
private const val ECO_NAME = "eco_packets"

object PacketInjectorListener : Listener {
    fun initialize(plugin: EcoPlugin) {
        plugin.onEnable {
            plugin.eventManager.registerListener(this)
            Bukkit.getOnlinePlayers().forEach { inject(it) }
        }
        plugin.onDisable {
            Bukkit.getOnlinePlayers().forEach { remove(it) }
            clearFrames()
            PacketWindowItems.clear()
            PacketContainerClick.clear()
            PacketSetCursorItem.clear()
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        inject(event.player)
    }

    private fun inject(player: Player) {
        val channel = player.toNMS().connection.connection.channel
        channel.eventLoop().execute {
            if (channel.isActive && channel.pipeline().get(BASE_NAME) != null &&
                channel.pipeline().get(ECO_NAME) == null) {
                channel.pipeline().addBefore(BASE_NAME, ECO_NAME, EcoChannelDuplexHandler(player.uniqueId))
            }
        }
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        remove(event.player)
    }

    private fun remove(player: Player) {
        val channel = player.toNMS().connection.connection.channel
        val playerId = player.uniqueId
        channel.eventLoop().execute {
            if (channel.pipeline().get(ECO_NAME) != null) {
                channel.pipeline().remove(ECO_NAME)
            }
            clearFrame(playerId)
            PacketWindowItems.clear(playerId)
            PacketContainerClick.clear(playerId)
            PacketSetCursorItem.clear(playerId)
        }
    }
}
