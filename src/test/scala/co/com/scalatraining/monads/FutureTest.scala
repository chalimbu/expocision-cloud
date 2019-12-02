package co.com.scalatraining.monads
import org.scalatest.FunSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class FutureSuite extends FunSuite {

  test("Un futuro es un bloque de codigo que se ejecuta en un hilo diferente al actual") {
    var welcome = "Hello World"

    val saludo = Future {
      Thread.sleep(500)
      println(s"Hilo nuevo ${Thread.currentThread().getName}" )
      welcome += " in S4N"
      welcome
    }

    println(s"no bloqueo del hilo ${Thread.currentThread().getName}")

    assert(welcome == "Hello World")
    val resultado = Await.result(saludo, Duration.Inf)
    assert(resultado == "Hello World in S4N")
  }

  test("Se debe poder encadenar Future con for-comp. Caso todos exitosos") {

    val f1 = Future {
      1
    }
    val f2 = Future {
      3
    }

    val result = for {
      x <- f1
      y <- f2
    } yield x + y

    val resultadoFuture = Await.result(result, Duration.Inf)

    assert(resultadoFuture == 4)

  }

  test("Se debe poder encadenar Future con for-comp. Caso al menos un fallido") {
    val f1 = Future {
      1
    }
    val f2 = Future {
      1 / 0
    }

    val result = for {
      x <- f1
      y <- f2
    } yield x + y

    assertThrows[ArithmeticException] {
      val resultadoFuture = Await.result(result, Duration.Inf)
    }
  }


  test("Se debe poder manejar el error de un Future de forma funcional sincronicamente. Use recover") {
    val valFuture = Future {
      Thread.sleep(1000)
      2 / 0
    }.recover {
      case e: ArithmeticException => "Math Error"
    }

    val result = Await.result(valFuture, Duration.Inf)

    assert(result == "Math Error")
  }

  test("Se debe poder manejar el error de un Future de forma funcional asincronamente. Use recoverWith") {
    val valFuture = Future {
      Thread.sleep(1000)
      2 / 0
    }.recoverWith {
      case e: ArithmeticException => {
        Future {
          1
        }
      }
    }
    val result = Await.result(valFuture, Duration.Inf)

    assert(result == 1)

  }

  test("Los future **iniciados** fuera de un for-comp deben iniciar al mismo tiempo") {
    println(s"Tiempo antes de que se ejecute el Future ${System.currentTimeMillis()}")
    val f1 = Future {
      Thread.sleep(100)
      1
    }
    val f2 = Future {
      Thread.sleep(100)
      2
    }
    val f3 = Future {
      Thread.sleep(100)
      1
    }

    val resultado = for {
      a <- f1
      b <- f2
      c <- f3
    } yield a + b + c

    val res = Await.result(resultado, Duration.Inf)

    println(s"Tiempo despues de que se inició el Future ${System.currentTimeMillis()}")

    assert(res == 4)
  }

  test("Los future **definidos** fuera de un for-comp deben iniciar secuencialmente") {

    val timeExpected = 300

    def f1 = Future {
      Thread.sleep(100)
      1
    }

    def f2 = Future {
      Thread.sleep(100)
      2
    }

    def f3 = Future {
      Thread.sleep(100)
      1
    }

    println(s"Tiempo antes de que se ejecute el Future ${System.currentTimeMillis()}")

    val resultado = for {
      a <- f1
      b <- f2
      c <- f3
    } yield (a + b + c)


    val res = Await.result(resultado, Duration.Inf)

    println(s"Tiempo despues de que se inició el Future ${System.currentTimeMillis()}")

    assert(res == 4)
  }


}
