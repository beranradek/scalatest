/* Based on example: https://github.com/akka/akka/tree/v2.0.3/akka-remote */

package cz.zst.box.akkaremote.operations

/**
 * Definice poskytovanych operaci a jejich vysledku.
 */

trait MathOp
case class Add(nbr1: Int, nbr2: Int) extends MathOp
case class Subtract(nbr1: Int, nbr2: Int) extends MathOp

trait MathResult
case class AddResult(nbr: Int, nbr2: Int, result: Int) extends MathResult
case class SubtractResult(nbr1: Int, nbr2: Int, result: Int) extends MathResult