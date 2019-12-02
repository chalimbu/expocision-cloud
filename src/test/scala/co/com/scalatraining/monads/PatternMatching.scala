package co.com.scalatraining.classes

import org.scalatest.funsuite.AnyFunSuite

/**
  * Created by judacu on 25/07/17.
  */
class PatternMatching extends AnyFunSuite{

  test("El Pattern Maching tiene un uso parecido al Switch de Java, con esteroides") {
    case class Pet(name: String)
    case class House(name: String)
    case class Apartment(house: House,pet :Pet)


    def simplifyTop(apartment: Apartment): String = apartment match {
      case Apartment(House("Santa Monica"),Pet("Mono")) => "Casa de Juan Diego"
      case Apartment(House("Robledo"),Pet("Kathy")) => "Casa de Juan Cadavid"
      case Apartment(_,Pet("Mono")) => "Perro de Juan"
      case _ => "Casa ni perro de nadie"
    }

    assertResult("Perro de Juan"){
      simplifyTop(Apartment(House("Santa M"),Pet("Mono")))
    }
  }

  test("Pattern with The Option type") {
    val capitals = Map("France" -> "Paris", "Japan" -> "Tokyo")

    def show(x: Option[String]) = x match {
      case Some(s) => s
      case None => "?"
    }

    assert(show(capitals get "Japan") == "Tokyo")
    assert(show(capitals get "Noth Pole") == "?")
  }


  test("Matching type only") {
    sealed trait Device

    case class Phone(model: String) extends Device {
      def screenOff = "Turning screen off"
    }

    case class Computer(model: String) extends Device {
      def screenSaverOn = "Turning screen saver on..."
    }

    def goIdle(device: Device) = device match {
      case p: Phone => p.screenOff
      case c: Computer => c.screenSaverOn
    }

    assert(goIdle(Computer("Mac")) == "Turning screen saver on...")
    println(goIdle(Phone("Iphone")) == "Turning screen off")
  }



}