/* Based on example: https://github.com/akka/akka/tree/v2.0.3/akka-remote */

package cz.zst.box.akkaremote.slave

import com.typesafe.config.ConfigFactory
import akka.actor.{Props, Actor, ActorSystem}
import akka.actor.actorRef2Scala
import cz.zst.box.akkaremote.operations.Add
import cz.zst.box.akkaremote.operations.AddResult
import cz.zst.box.akkaremote.operations.SubtractResult

object SlaveApp extends App {
  val system = ActorSystem("CalculatorApplication", ConfigFactory.load.getConfig("calculator"))
  val actor = system.actorOf(Props[SimpleCalculatorActor], "simpleCalculator")
  println("slave started and waiting for work...")
}

class SimpleCalculatorActor extends Actor {
  def receive = {
    case Add(n1, n2) =>
      println("Calculating %d + %d".format(n1, n2))
      sender ! AddResult(n1, n2, n1 + n2)
      println("Calculating %d - %d".format(n1, n2))
      sender ! SubtractResult(n1, n2, n1 - n2)
  }
}