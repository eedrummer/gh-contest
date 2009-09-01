package parser

import java.io._
import tokyocabinet._

/**
 * This object takes the repos.txt file and loads it into a Tokyo Cabinet
 * Table database. The key to the database is the repo id, and there are
 * two columns: name => the name of the repo
 *              count => the total number of watchers of the repo
 * 
 * The application takes the location of the repos.txt file as a CLI
 * argument.
 * 
 * It pulls the repo count form the repo hash database built by the
 * RepoSlurp object
 */
object RepoTableSlurp {
  import sbinary.DefaultProtocol._
  import sbinary.Operations._
  import helper.FileHelper._
  
  def main(args : Array[String]) : Unit = {
    val repoCountDatabase = new HDB
	repoCountDatabase.open("repos.hcb", HDB.OREADER)
	val repoDatabase = new TDB
	repoDatabase.open("repos.tcb", TDB.OWRITER | TDB.OCREAT )
	val repoFile = new File(args(0))
    repoFile.eachLine { line =>
    	val components = line.split(":")
    	addToRepoList(components(0), components(1), repoCountDatabase, repoDatabase)
    }
	repoCountDatabase.close
	repoDatabase.setindex("count", TDB.ITOPT)
	repoDatabase.close
  }
  
  def addToRepoList(repoId : String, repoName : String, repoCountDatabase : HDB, repoDatabase : TDB) = {
    val repoIdBytes = toByteArray(repoId)
    val repoCount = fromByteArray[Int](repoCountDatabase.get(repoIdBytes))
    var map = new java.util.HashMap[String, Any]
    map.put("name", repoName)
    map.put("count", repoCount)
    repoDatabase.put(repoIdBytes, map)
  }
}
