package com.willfp.eco.internal.spigot

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import io.papermc.paper.event.connection.PlayerConnectionValidateLoginEvent
import com.willfp.eco.util.StringUtils

object ServerLocking : Listener {
    @Volatile
    private var lockReason: String? = null

    @EventHandler
    fun handle(event: PlayerConnectionValidateLoginEvent) {
        val reason = lockReason ?: return
        event.kickMessage(StringUtils.toComponent(reason))
    }

    fun lock(reason: String) {
        lockReason = reason
    }

    fun unlock() {
        lockReason = null
    }
}
