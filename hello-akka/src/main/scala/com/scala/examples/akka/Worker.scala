package com.scala.examples.akka

import com.scala.examples.akka.Pi._
import akka.actor.Actor

class Worker extends Actor {

  // calculatePiFor ...

  def receive = {
    case Work(start, nrOfElements) =>
      // sender represents sender of Work message, we send response to it
      sender ! Result(calculatePiFor(start, nrOfElements)) // perform the work
  }
  
  def calculatePiFor(start: Int, nrOfElements: Int): Double = {
    var acc = 0.0
    for (i <- start until (start + nrOfElements))
      acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
    acc
  }
}