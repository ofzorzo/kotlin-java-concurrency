import kotlinx.coroutines.*

//libraries for file and time manipulation
import java.io.File
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
//library for Dispatcher creation
import java.util.concurrent.Executors

suspend fun main() {
    val philosophers = 101
    val jobs: MutableList<Job> = mutableListOf()
    val forks = BooleanArray(philosophers) {false}
    val dateFormat = SimpleDateFormat("dd-MM-yyyy_hh-mm-ss_zzz")
    val date = dateFormat.format(Date())
    val path = Paths.get("").toAbsolutePath().toString()+"\\philosophers\\"
    val pool = Executors.newFixedThreadPool(philosophers).asCoroutineDispatcher()

    for (i in 0 until philosophers) {
        jobs += GlobalScope.launch(pool){
            println("Job $i created")
            think(path+date+"_philosophers.txt", i, forks)
        }
    }
    for ((jobs_index, job) in jobs.withIndex()) {
        job.join()
        println("Job $jobs_index executed join().")
    }
    pool.close()

    analyzer(path+date+"_philosophers.txt", philosophers)
}

fun think(fileName: String, id: Int, forks: BooleanArray){
    val myfile = File(fileName)
    val timeToRun = 12
    val beg = System.currentTimeMillis()
    var end = beg
    while ((end - beg) / 1000 <= timeToRun) {
        if(id==0){
            synchronized(forks[ (id+1) % forks.size ]){
                synchronized(forks[id]){ //eat
                    myfile.appendText("Philosopher $id\n")
                }
            }
        }
        else{
            synchronized(forks[id]){
                synchronized(forks[ (id+1) % forks.size ]){ //eat
                    myfile.appendText("Philosopher $id\n")
                }
            }
        }
        end = System.currentTimeMillis()
    }
}

fun analyzer(fileName: String, philosophers: Int){
    val myfile = File(fileName)
    val timesAteArray = IntArray(philosophers) {0}
    myfile.forEachLine {
        timesAteArray[it.split(' ')[1].toInt()] += 1
    }
    for((philosopher, timesAte) in timesAteArray.withIndex()){
        println("Philosopher $philosopher ate $timesAte times.")
    }
}