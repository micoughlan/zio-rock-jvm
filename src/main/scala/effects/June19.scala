package effects

import zio.ZIO

object June19 extends App {
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
}