package com.willfp.eco.internal.spigot.integrations.bstats

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.bstats.EcoMetricsChart
import org.bstats.bukkit.Metrics
import org.bstats.charts.AdvancedBarChart
import org.bstats.charts.AdvancedPie
import org.bstats.charts.CustomChart
import org.bstats.charts.DrilldownPie
import org.bstats.charts.MultiLineChart
import org.bstats.charts.SimpleBarChart
import org.bstats.charts.SimplePie
import org.bstats.charts.SingleLineChart
import java.util.Properties

object MetricHandler {
    fun createMetrics(plugin: EcoPlugin) {
        val metrics = Metrics(plugin, plugin.bStatsId)

        for (chart in plugin.customCharts) {
            metrics.addCustomChart(chart.toBStatsChart())
        }

        metrics.addCustomChart(
            AdvancedPie("integrations_active") {
                plugin.loadedIntegrations
                    .associateWith { 1 }
                    .ifEmpty { null }
            }
        )

        metrics.addCustomChart(
            SimplePie("platform") {
                plugin.getPublishPlatform().displayName
            }
        )
    }

    private fun EcoPlugin.getPublishPlatform(): PublishPlatform {
        val props = Properties()

        val label = try {
            this.javaClass.getResourceAsStream("/platform")?.use { props.load(it) }
            props.getProperty("platform")
        } catch (e: Exception) {
            null
        } ?: return PublishPlatform.OTHER

        return PublishPlatform.fromMarkerLabel(label)
    }

    private fun EcoMetricsChart.toBStatsChart(): CustomChart = when (this) {
        is EcoMetricsChart.SimplePie -> SimplePie(id) { supplier() }
        is EcoMetricsChart.AdvancedPie -> AdvancedPie(id) { supplier() }
        is EcoMetricsChart.DrilldownPie -> DrilldownPie(id) { supplier() }
        is EcoMetricsChart.SingleLine -> SingleLineChart(id) { supplier() }
        is EcoMetricsChart.MultiLine -> MultiLineChart(id) { supplier() }
        is EcoMetricsChart.SimpleBar -> SimpleBarChart(id) { supplier() }
        is EcoMetricsChart.AdvancedBar -> AdvancedBarChart(id) { supplier() }
    }
}
