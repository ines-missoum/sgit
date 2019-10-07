package fr.missoum

import java.security.MessageDigest

object HashHelper{

  def hashFile(contentFile:String):String ={
    new String(MessageDigest.getInstance("SHA-1").digest((contentFile).getBytes).map(b => String.format("%02x", Byte.box(b))).mkString)
  }

}