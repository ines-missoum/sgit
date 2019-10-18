package fr.missoum.commands

import scala.annotation.tailrec
import scala.math.max

object SgitDiffImpl extends SgitDiff {

  /**
   *
   * @param contentFile1 the old file
   * @param contentFile2 the new content of the file
   * @return the old file with the diff lines in the right place
   */
  def diff(contentFile1: List[String], contentFile2: List[String]): List[String] = {
    if (contentFile1.isEmpty || contentFile1.filter(_.nonEmpty).isEmpty) contentFile2.map("+" + _)
    else {
      if (contentFile2.isEmpty || contentFile2.filter(_.nonEmpty).isEmpty) contentFile1.map("-" + _)
      else
        buildDiff(buildMatrix(contentFile1, contentFile2), contentFile1, contentFile2)
    }
  }

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
      //if end of line
      if (j == file1.length)
        buildMatrixRec(m, file1, file2, i + 1, 1)
      else buildMatrixRec(m, file1, file2, i, j + 1)
    }
  }

  /**
   *
   * @param matrix the matrix of find the longest common subsequence
   * @param file1  the old file
   * @param file2  the new content of the file
   * @return the old file with the diff lines in the right place
   */
  def buildDiff(matrix: Array[Array[Int]], file1: List[String], file2: List[String]): List[String] = {
    buildDiffRec(matrix, file1, file2, file2.length, file1.length, List[String]())
  }

  @tailrec
  def buildDiffRec(m: Array[Array[Int]], file1: List[String], file2: List[String], i: Int, j: Int, result: List[String]): List[String] = {
    if (i < 1 || j < 1)
      result
    else if (m(i)(j) == m(i)(j - 1))
      buildDiffRec(m, file1, file2, i, j - 1, ("-" + file1(j - 1)) +: result)
    else if (m(i)(j) == m(i - 1)(j))
      buildDiffRec(m, file1, file2, i - 1, j, ("+" + file2(i - 1)) +: result)
    else
    //else (m(i)(j) == (m(i - 1)(j - 1) + 1))
      buildDiffRec(m, file1, file2, i - 1, j - 1, file1(j - 1) +: result)
  }
}