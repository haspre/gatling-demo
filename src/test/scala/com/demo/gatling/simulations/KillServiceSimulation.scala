package com.demo.gatling.simulations

import com.demo.gatling.configurations.{Environment, Headers}
import com.demo.gatling.scenarios.{CreateAndAccessUnicornsScenario, FilterUnicornsScenario}
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class KillServiceSimulation extends Simulation {

  val httpConf = http
    .baseURL(Environment.baseUrl)
    .contentTypeHeader(Headers.contentType)
    .acceptHeader(Headers.acceptType)

  val scenarios = List(
    CreateAndAccessUnicornsScenario.createAndAccessUnicornsScenario.inject(
      atOnceUsers(500)),
    FilterUnicornsScenario.filterUnicornsScenario.inject(
      atOnceUsers(150), constantUsersPerSec(20) during 20)
  )

  setUp(scenarios)
    .protocols(httpConf)
    .assertions(
      global.responseTime.max.lte(Environment.maxResponseTimeInMs.toInt)
    )
}

