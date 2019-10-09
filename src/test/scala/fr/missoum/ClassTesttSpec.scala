
package fr.missoum
import org.scalatest.{FlatSpec,Matchers}

class ClassTesttSpec extends FlatSpec
  with Matchers {
  behavior of "Main"

  it should "ok test" in {
    assert(0 == 0)
  }

  it should "not ok test" in {
    "test" shouldEqual "test"
  }


}