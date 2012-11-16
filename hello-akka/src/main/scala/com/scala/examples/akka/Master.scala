package com.scala.examples.akka

import com.scala.examples.akka.Pi.Calculate
import com.scala.examples.akka.Pi.PiApproximation
import com.scala.examples.akka.Pi.Result
import com.scala.examples.akka.Pi.Work

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.routing.RoundRobinRouter
import akka.util.duration.longToDurationLong

/**
 * Delegates work to workers in "router" (to childs of router).
 * ActorRef is reference givable to another actor.
 * @author Radek Beran
 *
 */
class Master(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: ActorRef) extends Actor {

  var pi: Double = _
  var nrOfResults: Int = _
  val start: Long = System.currentTimeMillis

  // Created workerRouter actor is child of Master actor
  val routerProps: Props = Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers))
  //routerProps.routerConfig match {
  //  case r : RoundRobinRouter => println("nrOfInstances:" + r.nrOfInstances)
  //}
  val workerRouter: ActorRef = context.actorOf(
    routerProps, name = "workerRouter")

  def receive = {
  	case Calculate =>
  	  for (i <- 0 until nrOfMessages) {
  	    workerRouter ! Work(i * nrOfElements, nrOfElements)
  	  }
  	case Result(value) =>
  	  pi += value
  	  nrOfResults += 1
      if (nrOfResults == nrOfMessages) {
        // Send the result to the listener
        listener ! PiApproximation(pi, duration = (System.currentTimeMillis - start).millis)
        // Stops this actor and all its supervised children
        context.stop(self)
      }
  }
}