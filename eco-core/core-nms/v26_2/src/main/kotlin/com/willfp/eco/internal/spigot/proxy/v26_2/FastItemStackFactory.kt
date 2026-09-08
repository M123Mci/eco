package com.willfp.eco.internal.spigot.proxy.v26_2

import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.internal.spigot.proxies.FastItemStackFactoryProxy
import com.willfp.eco.internal.spigot.proxy.v26_2.common.item.EcoFastItemStack
import org.bukkit.inventory.ItemStack

class FastItemStackFactory : FastItemStackFactoryProxy {
    override fun create(itemStack: ItemStack): FastItemStack {
        return EcoFastItemStack(itemStack)
    }
}
