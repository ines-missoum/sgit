package fr.missoum.commands

import scala.annotation.tailrec
import scala.math.max

object SgitDiffImpl extends SgitDiff {

  def buildMatrix(contentFile1: List[String], contentFile2: List[String]): Array[Array[Int]] = {
    //do limit cases : length of files
    val matrix = Array.ofDim[Int](contentFile1.length, contentFile2.length)
    buildMatrixRec(matrix, contentFile1, contentFile2, 1, 1)
  }

  @tailrec
  def buildMatrixRec(m: Array[Array[Int]], file1: List[String], file2: List[String], i: Int, j: Int): Array[Array[Int]] = {
    if (i == file1.length - 1 && j == file2.length - 1)
      m
    else {
      if (file1(i).equals(file2(j)))
        m(i)(j) = m(i - 1)(j - 1) + 1
      else m(i)(j) = max(m(i)(j - 1), m(i - 1)(j))
      if (i == file1.length - 1)
        buildMatrixRec(m, file1, file2, 1, j + 1)
      else buildMatrixRec(m, file1, file2, i + 1, j)
    }
  }

  def diff(): List[String] = {

  }
}