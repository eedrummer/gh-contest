package parser

import java.io._
import tokyocabinet._

/**
 * This object takes the data.txt file and loads it into a Tokyo Cabinet
 * Hash database. The key to the database is the repo id, and the value
 * is the total number of watchers of the repo
 * 
 * The application takes the location of the data.txt file as a CLI
 * argument.
 */
object RepoSlurp {
  import sbinary.DefaultProtocol._
  import sbinary.Operations._
  import helper.FileHelper._
  
  def main(args : Array[String]) : Unit = {
    val database = new HDB
	database.open("repos.hcb", HDB.OWRITER | HDB.OCREAT)
	val userFile = new File(args(0))
    userFile.eachLine { line =>
    	val components = line.split(":")
    	addToRepoList(components(1), database)
    }
	database.close
  }
  
  def addToRepoList(repoId : String, database : HDB) = {
    val repoIdBytes = toByteArray(repoId)
    val bytes = database.get(repoIdBytes)
    if (bytes != null) {
      val repoCount = fromByteArray[Int](bytes) 
      database.put(repoIdBytes, toByteArray(repoCount + 1))
    }
    else {
      database.put(repoIdBytes, toByteArray(1))
    }
  }
}
