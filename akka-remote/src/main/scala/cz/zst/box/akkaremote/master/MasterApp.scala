/* Based on example: https://github.com/akka/akka/tree/v2.0.3/akka-remote */

package cz.zst.box.akkaremote.master

import scala.util.Random

import com.typesafe.config.ConfigFactory

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import cz.zst.box.akkaremote.operations.Add
import cz.zst.box.akkaremote.operations.AddResult
import cz.zst.box.akkaremote.operations.MathOp
import cz.zst.box.akkaremote.operations.MathResult
import cz.zst.box.akkaremote.operations.Subtract
import cz.zst.box.akkaremote.operations.SubtractResult

object MasterApp extends App {
  val system = ActorSystem("LookupApplication", ConfigFactory.load.getConfig("remotelookup"))
  
  /** Master actor */
  val actor = system.actorOf(Props[LookupActor], "lookupActor")
  
  /** Remote slave actor */
  val remoteSlaveActor = system.actorFor("akka://CalculatorApplication@127.0.0.1:2556/user/simpleCalculator")

  println("Master started")
  
  while (true) {
    if (Random.nextInt(100) % 2 == 0) doSomething(Add(Random.nextInt(100), Random.nextInt(100)))
    else doSomething(Subtract(Random.nextInt(100), Random.nextInt(100)))

    Thread.sleep(200)
  }

  def doSomething(op: MathOp) = {
    actor ! (remoteSlaveActor, op)
  }

}

/**
 * Master actor sending requests for computation processing
 * and receiving results.
 */
class LookupActor extends Actor {
  def receive = {
    case (actor: ActorRef, op: MathOp) => actor ! op
    case result: MathResult => result match {
      case AddResult(n1, n2, r) => println("Add result: %d + %d = %d".format(n1, n2, r))
      case SubtractResult(n1, n2, r) => println("Sub result: %d - %d = %d".format(n1, n2, r))
    }
  }
}

