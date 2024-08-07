package effects

import zio._

import java.time.InstantSource.system
import scala.annotation.tailrec

object June19  {
  def seqTaskLast[R, E, A, B](zioA: ZIO[R, E, A], zioB: ZIO[R, E, B]): ZIO[R, E, B] = for {
    _ <- zioA
    b <- zioB
  } yield b

  def seqTaskFirst[R, E, A, B](zioA: ZIO[R, E, A], zioB: ZIO[R, E, B]): ZIO[R, E, A] = for {
    a <- zioA
    _ <- zioB
  } yield a

  def runForever[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, A] =
    zio *> runForever(zio)

  val endlessLoop = runForever {
    ZIO.succeed {
      println("Nik is here!")
      Thread.sleep(1000)
    }
  }

  def convert[R, E, A, B](zio: ZIO[R, E, A], value: B): ZIO[R, E, B] =
    zio.as(value)

  def asUnit[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, Unit] =
    zio.unit

//  @tailrec
  def sumZIO_v2(n:Long) :UIO[Long] = {
    if(n == 0) ZIO.succeed(0L)
    else for{
      current <- ZIO.succeed(n)
      prev <- sumZIO_v2(n -1)
    } yield current + prev
  }

  def sumZio(n:Int) :UIO[Long] =
    sumZioInternal(n,0)

  @tailrec
  def sumZioInternal(n:Int,acc : Long):UIO[Long] =
    if(n == 0)
      ZIO.succeed(acc)
    else
      sumZioInternal(n-1,acc + n )

  def fibbo(i: Int) :UIO[Long] = ???/*{
    if (i == 1) ZIO.succeed(1L)
    else if(i == 2) ZIO.succeed(3L)
    else for {
prev <- fibbo(i-1)
      prev2 <- fibbo(i-2)
  }yield prev+prev2*/

  }

  //  def main(args:Array[String]) =
//    ???
   def main(args : Array[String]): Unit = {

    val runtime = Runtime.default
    Unsafe.unsafe{implicit u =>
      val start = system.millis()
      println(runtime.unsafe.run(fibbo(100)))
//      println(runtime.unsafe.run(sumZio(2000000)))
      println(s"took ${system.millis() - start}")
    }
  }
//  ZIO.suspend()
}

//Success(2000001000000)
//took 56