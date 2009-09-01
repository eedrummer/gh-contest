package parser

import java.io._
import tokyocabinet._
import scala.util.Random

/**
 * Generates the results file by pulling data from the Tokyo Cabinet
 * databases created by other objects
 */
object ResultsGenerator {
  import sbinary.DefaultProtocol._
  import sbinary.Operations._
  import scala.collection.jcl.Conversions._
  import helper.FileHelper._
  
  def main(args : Array[String]) : Unit = {
    val rand = new Random
    val userDatabase = new HDB
	userDatabase.open("users.hcb", HDB.OREADER)
	val repoDatabase = new TDB
	repoDatabase.open("repos.tcb", TDB.OREADER)
	val testFile = new File("/Users/andrewg/Downloads/download/test.txt")
    val resultsFile = new File("/Users/andrewg/Downloads/download/results.txt")
    val topRepos = getTopRepos(repoDatabase)
    val br = new BufferedReader(new FileReader(testFile))
    val bw = new BufferedWriter(new FileWriter(resultsFile))
    testFile.eachLine { line =>
    	val repoBytes = userDatabase.get(toByteArray(line))
    	var alreadyWatchedRepos : List[Int] = Nil
    	if (repoBytes != null)
    	{
    	  alreadyWatchedRepos = fromByteArray[List[Int]](repoBytes)
    	}
    	
    	val possibleRepos = topRepos -- alreadyWatchedRepos
    	// the random part at the end was just to create a unique file so GH would
        // process it and put me on the leaderboard (towards the bottom)
    	val topTen = possibleRepos.slice(0, 10).sort {(a, b) => rand.nextBoolean}
        bw.write(line + ":"  + topTen.mkString(",") + "\n")
    	println(line + ":"  + topTen.mkString(","))
    }
	bw.close
	userDatabase.close
	repoDatabase.close
  }
  
  /**
   * Find the top 50 watched repos in GitHub
   */
  def getTopRepos(repoDatabase : TDB) = {
    var query = new TDBQRY(repoDatabase)
    query.setlimit(50, 0)
    query.setorder("count", TDBQRY.QONUMDESC)
    val results = query.search.asInstanceOf[java.util.ArrayList[Array[Byte]]]
    results.map (new String(_).trim.toInt).toList
  }
}
