package com.willfp.eco.internal.spigot.proxy.v26_2.common.ai.entity

import com.willfp.eco.core.entities.ai.entity.EntityGoalBreatheAir
import com.willfp.eco.internal.spigot.proxy.v26_2.common.ai.EntityGoalFactory
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.BreathAirGoal
import net.minecraft.world.entity.ai.goal.Goal

object BreatheAirGoalFactory : EntityGoalFactory<EntityGoalBreatheAir> {
    override fun create(apiGoal: EntityGoalBreatheAir, entity: PathfinderMob): Goal {
        return BreathAirGoal(
            entity
        )
    }

    override fun isGoalOfType(goal: Goal) = goal is BreathAirGoal
}
