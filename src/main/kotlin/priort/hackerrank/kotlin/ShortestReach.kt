package priort.hackerrank.kotlin

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

data class Edge(val start: Int, val end: Int)

typealias Graph = Map<Int, Set<Int>>

fun List<Edge>.toGraph(): Map<Int, Set<Int>> =
    HashMap(flatMap { listOf(it.start to it.end, it.end to it.start) }.let {
        edgesAsPairs ->
            edgesAsPairs
                .groupBy { it.first }
                .toMap()
                .mapValues { edges -> edges.value.map { it.second }.toSet() }

    })

fun bfs(numNodes: Int, numEdges: Int, edges: Array<Array<Int>>, startNode: Int): Array<Int> =
    shortestReach(
        numNodes,
        edges.map { Edge(it[0], it[1]) }.toGraph(),
        startNode
    )


fun shortestReach(numNodes: Int, graph: Graph, startNode: Int): Array<Int> {
    val shortestReachCache = HashMap<Path, Int>()
    return (1..numNodes)
        .asSequence()
        .filter { it != startNode }
        .map { shortestReach(numNodes, startNode, it, graph, shortestReachCache) }
        .toList()
        .toTypedArray()
}

data class Path(val start: Int, val end: Int)

fun shortestReach(numNodes: Int, start: Int, end: Int, graph: Graph, shortestReachCache: HashMap<Path, Int>): Int {
    val alreadyCalculated = shortestReachCache[Path(start, end)]
    if (alreadyCalculated != null)
        return alreadyCalculated

    val edgeWeight = 6
    val alreadyVisited = HashSet<Int>()

    val nodesToVisitQueue = LinkedList<Int>()
    nodesToVisitQueue.addLast(start)

    val distanceSoFarToNodeQueue = LinkedList<Int>()
    distanceSoFarToNodeQueue.addLast(0)

    do {
        val next = nodesToVisitQueue.first
        val distanceSoFarToNext = distanceSoFarToNodeQueue.first
        nodesToVisitQueue.removeFirst()
        distanceSoFarToNodeQueue.removeFirst()
        if (!alreadyVisited.contains(next)) {
            if (next == end) {
                val shortestR = distanceSoFarToNext * edgeWeight
                shortestReachCache[Path(start, end)] = shortestR
                return shortestR
            } else {
                alreadyVisited.add(next)
                val neighbours = graph[next] ?: emptySet()
                neighbours.forEach {
                    distanceSoFarToNodeQueue.addLast(distanceSoFarToNext + 1)
                    if (shortestReachCache[Path(start, it)] == null)
                        shortestReachCache[Path(start, it)] = (distanceSoFarToNext + 1) * edgeWeight
                    nodesToVisitQueue.addLast(it)
                }
            }
        }
    } while (nodesToVisitQueue.isNotEmpty())
    shortestReachCache[Path(start, end)] = -1
    return -1
}