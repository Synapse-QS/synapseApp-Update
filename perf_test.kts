import kotlin.system.measureTimeMillis
import kotlin.random.Random

data class JsonObjectMock(val postId: String, val type: String)

fun main() {
    val random = Random(42)
    val totalReactions = 100_000
    val totalPosts = 5_000

    val allPosts = (1..totalPosts).map { it.toString() }

    // Generate random reactions
    val reactions = (1..totalReactions).map {
        JsonObjectMock(
            postId = allPosts[random.nextInt(allPosts.size)],
            type = "LIKE"
        )
    }

    // Chunk logic: simulating 20 chunks as in code (chunked(20))
    val chunkIds = allPosts.shuffled(random).take(20)

    // APPROACH 1: Old Approach (Nested Loop)
    val timeOld = measureTimeMillis {
        val result = chunkIds.map { postId ->
            val postReactions = reactions.filter { it.postId == postId }
            val summary = postReactions
                .groupBy { it.type }
                .mapValues { it.value.size }
            postId to summary
        }
    }

    // APPROACH 2: New Approach (Group By)
    val timeNew = measureTimeMillis {
        val reactionsByPostId = reactions.groupBy { it.postId }
        val result = chunkIds.map { postId ->
            val postReactions = reactionsByPostId[postId] ?: emptyList()
            val summary = postReactions
                .groupBy { it.type }
                .mapValues { it.value.size }
            postId to summary
        }
    }

    println("Old approach took: $timeOld ms")
    println("New approach took: $timeNew ms")
    val improvement = if (timeOld > 0) ((timeOld - timeNew).toDouble() / timeOld * 100) else 0.0
    println(String.format("Improvement: %.2f%%", improvement))
}

main()
