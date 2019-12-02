package co.com.scalatraining.monads

import org.scalatest.FunSuite

class HigherOrdersFunctionTest extends FunSuite{
  test("tipos de funcion") {
    val obtenerPrimeraLetra: String => Option[Char] = (s: String) => s.headOption


    def obtenerPrimeraLetra1(s:String)= s.headOption
    val x: String => Option[Char] = obtenerPrimeraLetra
  }


  test("Funcion de alto orden como parametro") {

    case class Persona(primerNombre:String,segundoNombre:String,edad:Int)

    def obtenerNombre(primerNombre: String, segundoNombre: String ) = s"$primerNombre $segundoNombre"

    val listaPersonas = List(
      Persona("Berta","Del Socorro",21),
      Persona("Maruja","Blanca",23),
      Persona("Carla","Peña",19),
      Persona("Gloria","Mosa",5)
    )
    println(  listaPersonas.map(persona => obtenerNombre(persona.primerNombre,persona.segundoNombre)) )
  }
}
