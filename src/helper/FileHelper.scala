package helper

import java.io._

/**
 * Grabbed from http://www.artima.com/forums//flat.jsp?forum=283&thread=243344 
 */
class FileHelper(file : File) {
  def eachLine(proc : String=>Unit) : Unit = {
    val br = new BufferedReader(new FileReader(file))
    try{ while(br.ready) proc(br.readLine) }
    finally{ br.close }
  }
}

object FileHelper {
  implicit def file2helper(file : File) = new FileHelper(file)
}

