package fr.missoum

import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class MainSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "Main class"

  it should "execute init command when argument is init" in {
    //given
    val arg = Array("init")
    val mockExecutor = mock[CommandExecutor]
    val classTested = Main
    classTested.executor = mockExecutor
    //when
    val main = Main.main(arg)
    //then
    mockExecutor.executeInit() was called

  }

}
