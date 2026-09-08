package com.willfp.eco.internal.spigot.proxy.v26_2

import com.willfp.eco.core.entities.ai.EntityController
import com.willfp.eco.internal.spigot.proxies.EntityControllerFactoryProxy
import com.willfp.eco.internal.spigot.proxy.v26_2.entity.EcoEntityController
import org.bukkit.entity.Mob

class EntityControllerFactory : EntityControllerFactoryProxy {
    override fun <T : Mob> createEntityController(entity: T): EntityController<T> {
        return EcoEntityController(entity)
    }
}
