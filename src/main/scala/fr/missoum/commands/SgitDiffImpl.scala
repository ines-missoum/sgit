package fr.missoum.commands

import scala.annotation.tailrec
import scala.math.max

object SgitDiffImpl extends SgitDiff {

  /**
   *
   * @param contentFile1 the old file
   * @param contentFile2 the new content of the file
   * @return the matrix build to find the longest common subsequence
   */
  def buildMatrix(contentFile1: List[String], contentFile2: List[String]): Array[Array[Int]] = {
    //do limit cases : length of files
    val matrix = Array.ofDim[Int](contentFile2.length + 1, contentFile1.length + 1)
    buildMatrixRec(matrix, contentFile1, contentFile2, 1, 1)
  }

  @tailrec
  def buildMatrixRec(matrix: Array[Array[Int]], file1: List[String], file2: List[String], i: Int, j: Int): Array[Array[Int]] = {
    val m = matrix
    if (i > file2.length || j > file1.length)
      m
    else {
      if (file2(i - 1).equals(file1(j - 1)))
        m(i)(j) = m(i - 1)(j - 1) + 1
      else m(i)(j) = max(m(i)(j - 1), m(i - 1)(j))
      //end of line
      if (j == file1.length)
        buildMatrixRec(m, file1, file2, i + 1, 1)
      else buildMatrixRec(m, file1, file2, 1, j + 1)
    }
  }

  def diff(): List[String] = {
    List[String]()
  }
}