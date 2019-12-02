package co.com.scalatraining.monads

import org.scalatest.FunSuite
import scala.language.implicitConversions

class ImplicitsTest extends FunSuite {
  test("paso de implicitos en el scope de las variables") {
    implicit val conts = 5
    //implicit val conts2 = 6

    def multiplicar(numero:Int)(implicit constante:Int) = numero*constante

    val result = multiplicar(5)
    assert( result == 25 )
  }

  test("Funciones con funciones en sus parametros") {
   implicit def convertirListaString(x: List[String]): String = {
     x.mkString("-")
   }

    val x: String = List("Hola","Como", "Estan")
    println(x)
  }
}
