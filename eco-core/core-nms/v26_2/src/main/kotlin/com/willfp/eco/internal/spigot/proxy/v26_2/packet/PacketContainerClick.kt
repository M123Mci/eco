package com.willfp.eco.internal.spigot.proxy.v26_2.packet

import com.willfp.eco.core.packet.PacketEvent
import com.willfp.eco.core.packet.PacketListener
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.internal.spigot.proxy.v26_2.common.packet.display.frame.DisplayFrame
import com.willfp.eco.internal.spigot.proxy.v26_2.common.packet.display.frame.lastDisplayFrame
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import net.minecraft.network.HashedStack
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket

object PacketContainerClick : PacketListener {
    // 按玩家隔离并限制缓存大小；完整键避免不同物品哈希冲突。
    private val originals = ConcurrentHashMap<UUID, Cache<HashedStack.ActualItem, HashedStack>>()

    fun map(playerId: UUID, original: HashedStack, displayed: HashedStack.ActualItem) {
        originals.computeIfAbsent(playerId) {
            Caffeine.newBuilder().maximumSize(1024).build()
        }.put(displayed, original)
    }

    fun clear(playerId: UUID) {
        originals.remove(playerId)
    }

    fun clear() {
        originals.clear()
    }

    override fun onReceive(event: PacketEvent) {
        val packet = event.packet.handle as? ServerboundContainerClickPacket ?: return

        val carried = packet.carriedItem as? HashedStack.ActualItem ?: return
        val player = event.player
        val original = originals[player.uniqueId]?.getIfPresent(carried) ?: return

        event.packet.handle = ServerboundContainerClickPacket(
            packet.containerId,
            packet.stateId,
            packet.slotNum,
            packet.buttonNum,
            packet.containerInput,
            packet.changedSlots,
            original
        )

        player.lastDisplayFrame = DisplayFrame.EMPTY
    }
}
