

package co.com.scalatraining.monads

import java.util.OptionalInt

import org.scalatest.FunSuite

class OptionSuite extends FunSuite {
  case class User(
                   id: Int,
                   firstName: String,
                   lastName: String,
                   age: Int,
                   gender: Option[String])

  object UserRepository {
    private val users = Map(1 -> User(1, "John", "Doe", 32, Some("male")),
      2 -> User(2, "Johanna", "Doe", 30, None))
    def findById(id: Int): Option[User] = users.get(id)
    def findAll = users.values
  }

  test("Se debe poder crear un Option con valor"){
    val value: Option[String] = Option("Hola Mundo")
    assert(value == Option("Hola Mundo"))
  }

  test("Se debe poder crear un Option para denotar que no hay valor"){
    val value: Option[Int] = None
    assert(value == None)
  }

  test("Es inseguro acceder al valor de un Option con get. Verificar con assertThrows"){
    val option = Option(1,3,4,5,6)
    assert((1,3,4,5,6) == option.get)

    val option2 = None
    assertThrows[NoSuchElementException] {
      option2.get
    }

  }


  test("Se debe poder hacer pattern match sobre un Option") {
    case class Car(id : String,model : Int, brand : Option[String])
    val cars = Map(1 -> Car("IYO803",2017,Option("Sandero")),
      2 -> Car("BUL145",1995,Option("Festiva")),
      3 -> Car("AAA111",2000,None))
    def kindBrand(brand : Option[String]) = brand match {
      case Some(brand) => brand
      case None => "not specified"
    }

    var value = kindBrand(cars.get(1).get.brand)
    assert(value == "Sandero")

    value = kindBrand(cars.get(3).get.brand)
    assert(value == "not specified")
  }

  test("Se debe poder saber si un Option tiene valor con isDefined") {

    val user1 = UserRepository.findById(1)
    if (user1.isDefined) {
      assert(user1.get.firstName ==  "John")
    }
  }

  test("Se debe poder acceder al valor de un Option de forma segura con getOrElse") {
    var value = Option(3)
    assert(value.get == 3)
    value = None
    assert(value.getOrElse(0) == 0)
  }

  test("Un Option se debe poder transformar con un map") {
    val age = UserRepository.findById(1).map(_.age > 30)
    assert(age == Some(true))
  }

  test("Un Option se debe poder transformar con flatMap en otro Option") {
    val user = UserRepository.findById(1)
    assert(user.flatMap(x => x.gender) == Some("male"))
  }

  test("Un Option se debe poder filtrar con filter") {
    val value = Option(4,3,4,5)

    assert(value.filter(x => x._1 > x._2) == Some((4,3,4,5)))

  }

  test("Combinar varios Option en un for-comp. Caso todos Some") {
    val value1 = Option(1)
    val value2: Option[Int] = None

    val result = for {
      x <-value1
      y <-value1
      z <-value1
    } yield (x + y + z)
    assert(result == Some(3))
  }

  test("Combinar varios Option en un for-comp. Caso al menos un None") {
    val value1 = Option(1)
    val value2: Option[Int] = None

    val result = for {
      x <-value1
      y <-value1
      z <-value2
    } yield (x + y + z)
    assert(result == None)
  }

  test("Obtener el promedio de los pares"){
    // Obtenga el promedio de los pares de la siguiente lista
    val lo: Seq[Option[Int]] = (1 to 10).map(Option(_))
    val value = lo.flatMap( _ .filter( _  % 2 == 0))
    assert(value.sum / value.length == 6)
  }

}


