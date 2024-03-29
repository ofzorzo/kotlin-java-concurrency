import kotlinx.coroutines.*
import kotlin.math.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis
import kotlin.Pair

//libraries for file manipulation
import java.io.File
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

suspend fun main() {
    val n = 33554432 // has to be a power of 2
    val p = 32
    val arr = randArr(n)
    val ans = LongArray(n)
    val logN = log2(n.toDouble()).toInt()
    var nivel = 0
    //var seqAns: Long = 0

    //file creation:
    //val dateFormat = SimpleDateFormat("dd-MM-yyyy_hh-mm-ss_zzz")
    //val date = dateFormat.format(Date())
    //val path = Paths.get("").toAbsolutePath().toString()+"\\sumArrays\\"
    //arrToFile(path+date+"_sumArray.txt", arr)
    /*
    var duration = measureTimeMillis {
        seqAns = seqSum(arr)
    }

    */
    //println("Soma de elementos sequencial realizada em ${duration/1000.0} segundos.")

    var duration = measureTimeMillis {
        while (nivel < logN) {
            val threadsToRun = min(p.toFloat(), n / (2).toFloat().pow(nivel+1)).toInt()
            val jobs: MutableList<Job> = mutableListOf()
            for (i in 0 until threadsToRun) {
                jobs += GlobalScope.launch {
                    sum(nivel, arr, ans, i, n, p)
                }
            }
            val results: MutableList<Pair<Int, Long>> = mutableListOf()
            for (job in jobs) {
                job.join()
            }
            for (i in 0 until n) {
                arr[i] = ans[i]
            }
            nivel += 1
        }
    }
    println("Soma de elementos paralela realizada em ${duration/1000.0} segundos.")
    //println("Resultado sequencial: $seqAns")
    println("Resultado paralelo: ${arr[0]}")
}
fun sum(h: Int, arr: LongArray, ans:LongArray, id: Int, n: Int, p: Int){
    val sums = n / 2.toDouble().pow(1+h).toInt()
    val sumsToRun = max(sums / p, 1)

    for (i in 0 until sumsToRun) {
        val index = id * sumsToRun + i
        ans[index] = arr[2*index] + arr[2*index + 1]
    }
}

fun seqSum(arr: LongArray): Long{
    var sum: Long = 0
    for(num in arr){
        sum += num
    }
    return sum
}

fun randArr(n: Int): LongArray {
    val arr = LongArray(n)
    for (i in 0 until n) {
        arr[i] = Random.nextLong(1, 15) // values from 1 to RANGE
    }
    return arr
}

fun arrToFile(fileName: String, arr: LongArray){
    val myfile = File(fileName)

    myfile.printWriter().use { out ->
        for(num in arr) {
            out.print("$num ")
        }
    }
}

fun printArr(arr: LongArray){
    for(num in arr){
        print("$num ")
    }
}