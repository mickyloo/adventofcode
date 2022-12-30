package day19

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.system.measureTimeMillis
import java.util.concurrent.Executors

fun main() {
    val re = Regex("""Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""")

    val blueprints = File("src/main/kotlin/day19/input.txt")
        .readLines()
        .map {
            val matches = re.matchEntire(it)!!.destructured.toList().map { it.toInt() }
            Blueprint(matches[0], mapOf(
                Bot.ORE to Cost(matches[1], 0, 0),
                Bot.CLAY to Cost(matches[2], 0, 0),
                Bot.OBSIDIAN to Cost(matches[3], matches[4], 0),
                Bot.GEODE to Cost(matches[5], 0, matches[6])
            ))
        }

//    val elapsed1 = measureTimeMillis { part1(blueprints) }
//    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(blueprints.take(3)) }
    println("Part2: $elapsed2 ms")
}

fun part1(blueprints: List<Blueprint>) {
    var results = blueprints.associate { it.num to 0 }.toMutableMap()
    runBlocking {
        blueprints.forEach {
            async(Dispatchers.Default) {
                results[it.num] = it.num * it.run(24)
            }
        }
    }
    println(results.values.sum())
}

fun part2(blueprints: List<Blueprint>) {
    var results = mutableListOf(0, 0, 0)
    runBlocking {
        blueprints.forEachIndexed { index, blueprint ->
            async(Dispatchers.Default) {
                results[index] = blueprint.run(32)
            }
        }
    }
    println(results)
    println(results[0] * results[1] * results[2])
}

data class Resource(val ore: Int, val clay: Int, val obsidian: Int, val geode: Int) {
    operator fun plus(wf: Workforce): Resource =
        Resource(ore + wf.ore, clay + wf.clay, obsidian + wf.obsidian, geode + wf.geode)

    operator fun minus(cost: Cost): Resource =
        Resource(ore - cost.ore, clay - cost.clay, obsidian - cost.obsidian, geode)

    fun canBuy(cost: Cost): Boolean =
        ore >= cost.ore && clay >= cost.clay && obsidian >= cost.obsidian
}
data class Workforce(val ore: Int, val clay: Int, val obsidian: Int, val geode: Int)
data class State(val remaining: Int, val resource: Resource, val workforce: Workforce) {
    fun buy(bot: Bot, cost: Cost): State {
        val newResource = resource - cost + workforce
        val newWorkforce = when(bot) {
            Bot.ORE -> workforce.copy(ore = workforce.ore + 1)
            Bot.CLAY -> workforce.copy(clay = workforce.clay + 1)
            Bot.OBSIDIAN -> workforce.copy(obsidian = workforce.obsidian + 1)
            Bot.GEODE -> workforce.copy(geode = workforce.geode + 1)
        }
        return State(remaining - 1, newResource, newWorkforce)
    }
    fun stay(): State = State(
        this.remaining - 1,
        this.resource + this.workforce,
        this.workforce
    )
}

private val INITIAL_RESOURCE = Resource(0, 0, 0, 0)
private val INITIAL_WORKFORCE = Workforce(1, 0, 0, 0)

enum class Bot {
    ORE, CLAY, OBSIDIAN, GEODE
}
data class Cost(val ore: Int, val clay: Int, val obsidian: Int)

data class Blueprint(val num: Int, val cost: Map<Bot, Cost>) {

    private val maxBots = mapOf(
        Bot.ORE to cost.maxOf { (_, cost) -> cost.ore },
        Bot.CLAY to cost.maxOf { (_, cost) -> cost.clay },
        Bot.OBSIDIAN to cost.maxOf { (_, cost) -> cost.obsidian },
        Bot.GEODE to Int.MAX_VALUE
    )

    private fun tryBuy(state: State, bot: Bot): State {
        val numBot = when(bot) {
            Bot.ORE -> state.workforce.ore
            Bot.CLAY -> state.workforce.clay
            Bot.OBSIDIAN -> state.workforce.obsidian
            Bot.GEODE -> state.workforce.geode
        }
        if (numBot >= maxBots[bot]!!) {
            return state
        }

        val cost = cost[bot]!!
        if (state.resource.canBuy(cost)) {
            return state.buy(bot, cost)
        }
        return state
    }

    fun run(minutes: Int = 24) : Int {
        val work = ArrayDeque<State>()
        var maxGeode = (minutes downTo 0).associateWith { 0 }.toMutableMap()
        work.add(State(minutes, INITIAL_RESOURCE, INITIAL_WORKFORCE))
        val buyOrder = listOf(Bot.OBSIDIAN, Bot.CLAY, Bot.ORE)

        work@ while(work.isNotEmpty()) {
            val state = work.removeLast()

            val maxPossible = state.remaining + state.resource.geode
            if (maxPossible < maxGeode[state.remaining]!!) {
                //println("skip $state")
                continue
            }

            maxGeode[state.remaining] = maxOf(maxGeode[state.remaining]!!, state.resource.geode)
            if (state.remaining < 1) {
                continue
                println(maxGeode)
            }

            // do geode bot first
            val newStateForGeode = tryBuy(state, Bot.GEODE)
            if (newStateForGeode != state) {
                work.add(newStateForGeode)
            } else {
                // only consider other bots if not buying geode bot
                work.add(state.stay())
                for(bot in buyOrder) {
                    val newState = tryBuy(state, bot)
                    if (newState != state) {
                        work.add(newState)
                    }
                }
            }
        }
        println(maxGeode)
        return maxGeode[0]!!
    }
}