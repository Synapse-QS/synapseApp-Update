import io.github.jan.supabase.realtime.RealtimeChannel
import java.lang.reflect.Method

fun main() {
    val clazz = RealtimeChannel::class.java
    clazz.methods.forEach { println(it.name + " " + it.returnType) }
}
