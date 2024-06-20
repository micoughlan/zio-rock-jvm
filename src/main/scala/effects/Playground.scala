package effects

import java.time.Instant

object Playground extends App {
  case class MyIO[A](unsafeRun: () => A) {
    def map[B](f: A => B): MyIO[B] =
      MyIO(() => f(unsafeRun()))

    def flatMap[B](f: A => MyIO[B]): MyIO[B] =
      MyIO(() => f(unsafeRun()).unsafeRun())
  }

  case class MyChainIO[A, B](unsafeRun: A => B) {
    def map[C](f: B => C): MyChainIO[A, C] =
      MyChainIO(b => f(unsafeRun(b)))

    def flatMap[C](f: B => MyChainIO[A, C]): MyChainIO[A, C] =
      MyChainIO(b => f(unsafeRun(b)).unsafeRun(b))
  }

  val currentTime = MyIO[Instant](() => {
    Instant.now()
  })

  currentTime.unsafeRun()

  def measure[A](computation: MyIO[A]): MyIO[(Long, A)] = {
    for {
      startTime         <- currentTime
      computationResult <- computation
      endTime           <- currentTime
    } yield (endTime.getEpochSecond - startTime.getEpochSecond, computationResult)
  }

  def readUserName: MyChainIO[Unit, String] = MyChainIO(_ => {
    println("Please enter your user name")
    scala.io.StdIn.readLine()
  })

  def printString: MyChainIO[String, String] = MyChainIO((string: String) => {
    println(s"Hello $string")
    string
  })

  println("TEST")
//  readUserName.flatMap((a: String) => printString.map(a => a)).unsafeRun()
//  measure(readUserName).unsafeRun()
}
