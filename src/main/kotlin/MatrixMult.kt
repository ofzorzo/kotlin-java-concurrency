import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

import java.io.File
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

suspend fun main() { // main() has to be a suspend function so we can call join() from it
    val n = 1024
    val p = 8
    val q = n/p
    val jobs: MutableList<Job> = mutableListOf()
    val matrix1 = randMatrix(n)
    val matrix2 = randMatrix(n)

    // creation of files:
    val dateFormat = SimpleDateFormat("dd-MM-yyyy_hh-mm-ss_zzz")
    val date = dateFormat.format(Date())
    val path = Paths.get("").toAbsolutePath().toString()+"\\matrices\\"
    matrixToFile(path+date+"_matrix1.txt", matrix1)
    matrixToFile(path+date+"_matrix2.txt", matrix2)

    val ansParallel = Array(n) {LongArray(n)} //creates array of n LongArrays, each of size n
    var duration = measureTimeMillis {
        for (i in 0 until p) {      // botar runBlocking antes dessa linha pra testar oq eu disse abaixo!
            jobs += GlobalScope.launch{ // se fizéssemos runBlocking e dentro dele um for de launch, sem o globalscope, cada coroutine estaria no escopo desse runBlocking e, portanto, terminariam de executar, mas não estariam em paralelo. Se usarmos o GlobalScope dentro de um runBlocking, entretanto, elas não estarão no escopo dele e serão paralelas; contudo, o runBlocking não irá esperar elas terminarem de executar, irá apenas esperar cada coroutine ser criada pelo for do runBlocking.
                /** comment line below when benchmarking **/
                println("Começou thread $i")
                /** comment this line when benchmarking **/
                /** comment line above when benchmarking **/

                mult(matrix1, matrix2, ansParallel, i, q)

                /** comment line below when benchmarking **/
                println("Terminou thread $i")
                /** comment this line when benchmarking **/
                /** comment line above when benchmarking **/
            }
        }
        for ((jobs_index, job) in jobs.withIndex()) {
            job.join()
            println("Job $jobs_index executed join().")
        }
    }
    println("Multiplicação de matrizes paralela realizada em ${duration/1000.0} segundos.")
    matrixToFile(path+date+"_parallel_ans.txt", ansParallel)

    val ansSequential = Array(n) {LongArray(n)}
    duration = measureTimeMillis {
        seqMult(matrix1, matrix2, ansSequential)
    }
    println("Multiplicação de matrizes sequencial realizada em ${duration/1000.0} segundos.")
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