import kotlinx.coroutines.*
import java.io.File
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy_hh-mm-ss_zzz")
    val date = dateFormat.format(Date())
    val path = Paths.get("").toAbsolutePath().toString()+"\\matrices\\"
    val n = 128
    val p = 8
    val q = n/p
    val matrix1 = randMatrix(n)
    matrixToFile(path+date+"_matrix1.txt", matrix1)
    val matrix2 = randMatrix(n)
    matrixToFile(path+date+"_matrix2.txt", matrix2)
    val ansParallel = Array(n) {LongArray(n)} //creates array of n LongArrays, each of size n
    val ansSequential = Array(n) {LongArray(n)}
    val latch = CountDownLatch(p)

    var duration = measureTimeMillis {
        for (i in 0 until p) {
            GlobalScope.launch {
                /** comment line below when benchmarking **/
                //println("Começou thread $i")
                /** comment this line when benchmarking **/
                /** comment line above when benchmarking **/

                mult(matrix1, matrix2, ansParallel, i, q)

                /** comment line below when benchmarking **/
                //println("Terminou thread $i")
                /** comment this line when benchmarking **/
                /** comment line above when benchmarking **/
                latch.countDown()
            }
        }
        latch.await()
    }
    var timeElapsed = duration/1000.0
    println("Multiplicação de matrizes paralela realizada em $timeElapsed segundos.")
    matrixToFile(path+date+"_parallel_ans.txt", ansParallel)

    duration = measureTimeMillis {
        seqMult(matrix1, matrix2, ansSequential)
    }
    timeElapsed = duration/1000.0
    println("")
    println("Multiplicação de matrizes sequencial realizada em $timeElapsed segundos.")
    matrixToFile(path+date+"_sequential_ans.txt", ansSequential)
}

fun mult(matrix1: Array<LongArray>, matrix2: Array<LongArray>, ans: Array<LongArray>, id: Int, q: Int){
    val begin = q*id
    val end = (q*id)+q-1
    for(row in begin until end+1){
        for(col in 0 until matrix1.size){
            var sum: Long = 0
            for(i in 0 until matrix1.size){
                sum += matrix1[row][i] * matrix2[i][col] // matrix1 with fixed row and matrix2 with fixed column
            }
            ans[row][col] = sum
        }
    }
}

fun seqMult(matrix1: Array<LongArray>, matrix2: Array<LongArray>, ans: Array<LongArray>){
    for(row in 0 until matrix1.size) {
        for(col in 0 until matrix1.size) {
            var sum: Long = 0
            for(i in 0 until matrix1.size){
                sum += matrix1[row][i] * matrix2[i][col]
            }
            ans[row][col] = sum
        }
    }
}

fun matrixToFile(fileName: String, matrix: Array<LongArray>){
    val myfile = File(fileName)

    myfile.printWriter().use { out ->
        for(row in 0 until matrix.size) {
            if(row != 0)
                out.println("")
            for (col in 0 until matrix.size) {
                if(col == 0)
                    out.print(matrix[row][col].toString())
                else
                    out.print(" " + matrix[row][col].toString())
            }
        }
    }
}

fun randMatrix(n: Int): Array<LongArray>{
    val matrix = Array(n) {LongArray(n)} //creates array of n LongArrays, each of size n
    for(i in 0 until n){
        for(j in 0 until n){
            matrix[i][j] = Random.nextLong(1, 15)
        }
    }
    return matrix
}

fun printMatrix(matrix: Array<LongArray>){
    val n: Int = matrix.size
    for(i in 0 until n){
        for(j in 0 until n){
            print(matrix[i][j].toString() + "\t")
        }
        println("")
    }
}