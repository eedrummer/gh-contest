package parser

import java.io._
import tokyocabinet._

/**
 * This object takes the data.txt file and loads it into a Tokyo Cabinet
 * Hash database. The key to the database is the user id, and the value
 * is a List of repo ids being watched
 * 
 * The application takes the location of the data.txt file as a CLI
 * argument.
 */
object Slurp {
  import sbinary.DefaultProtocol._
  import sbinary.Operations._
  import helper.FileHelper._
  
  def main(args : Array[String]) : Unit = {
    val database = new HDB
	database.open("users.hcb", HDB.OWRITER | HDB.OCREAT)
	val userFile = new File(args(0))
    userFile.eachLine { line =>
    	val components = line.split(":")
    	addToUserList(components(0), components(1), database)
    }
	database.close
  }
  
  def addToUserList(userId : String, repoId : String, database : HDB) = {
    val userIdBytes = toByteArray(userId)
    val bytes = database.get(userIdBytes)
    val repoInt = repoId.toInt
    if (bytes != null) {
      val oldRepos = fromByteArray[List[Int]](bytes)
      val newRepos = repoInt :: oldRepos 
      database.put(userIdBytes, toByteArray(newRepos))
    }
    else {
      val repos = List(repoInt)
      database.put(userIdBytes, toByteArray(repos))
    }

      
  }
}
