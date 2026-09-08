package com.willfp.eco.internal.spigot.proxy.v26_2.packet

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.packet.PacketEvent
import com.willfp.eco.core.packet.PacketListener
import com.willfp.eco.internal.spigot.proxy.v26_2.common.asBukkitStack
import com.willfp.eco.internal.spigot.proxy.v26_2.common.packet.display.frame.DisplayFrame
import com.willfp.eco.internal.spigot.proxy.v26_2.common.packet.display.frame.lastDisplayFrame
import net.minecraft.core.component.TypedDataComponent
import net.minecraft.network.HashedStack
import net.minecraft.network.HashedPatchMap
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.HashOps
import org.bukkit.craftbukkit.entity.CraftPlayer
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap


object PacketSetCursorItem : PacketListener {
    private val componentHashes = ConcurrentHashMap<UUID, Cache<TypedDataComponent<*>, Int>>()

    fun clear(playerId: UUID) {
        componentHashes.remove(playerId)
    }

    fun clear() {
        componentHashes.clear()
    }

    private fun hashGenerator(player: ServerPlayer): HashedPatchMap.HashGenerator {
        val cache = componentHashes.computeIfAbsent(player.uuid) {
            Caffeine.newBuilder().maximumSize(256).build()
        }
        val ops = player.registryAccess().createSerializationContext(HashOps.CRC32C_INSTANCE)
        // 与 26.2 容器同步器使用相同的编码和 CRC32C 算法，不读取其私有缓存。
        return HashedPatchMap.HashGenerator { component ->
            cache.get(component) {
                it.encodeValue(ops).getOrThrow { message ->
                    IllegalArgumentException("无法计算物品组件哈希: $message")
                }.asInt()
            }!!
        }
    }

    override fun onSend(event: PacketEvent) {
        val packet = event.packet.handle as? ClientboundSetCursorItemPacket ?: return

        val contents = packet.contents

        if (contents.isEmpty) {
            return
        }

        val item = contents.asBukkitStack()
        val player = event.player
        val serverPlayer = (player as CraftPlayer).handle

        val hashes = hashGenerator(serverPlayer)

        val original = HashedStack.create(packet.contents, hashes)

        Display.display(item, player)

        val displayed = HashedStack.create(packet.contents, hashes)

        PacketContainerClick.map(player.uniqueId, original, displayed as HashedStack.ActualItem)

        player.lastDisplayFrame = DisplayFrame.EMPTY
    }
}
