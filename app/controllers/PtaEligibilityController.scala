/*
 * Copyright 2016 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import scala.concurrent.Future
import actions.AuthorisedActions
import actions.MarriageAllowanceRegime
import connectors.ApplicationAuditConnector
import connectors.ApplicationAuthConnector
import forms.EligibilityCalculatorForm.calculatorForm
import services.EligibilityCalculatorService
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.frontend.controller.FrontendController
import config.ApplicationConfig
import utils.TamcBreadcrumb
import actions.JourneyEnforcers
import details.CitizenDetailsService
import uk.gov.hmrc.play.frontend.auth.AuthContext

/**
 * In future we may need to change the name of this class to EligibilityContoller,
 * when project decides to move away from Gov.uk journey
 */
object PtaEligibilityController extends PtaEligibilityController {
  override val auditConnector = ApplicationAuditConnector
  override lazy val maAuthRegime = MarriageAllowanceRegime
  override val authConnector = ApplicationAuthConnector
  override val citizenDetailsService = CitizenDetailsService
  override val ivUpliftUrl = ApplicationConfig.ivUpliftUrl
}

trait PtaEligibilityController extends FrontendController with AuthorisedActions with TamcBreadcrumb with JourneyEnforcers {

  val eligibilityCalculatorService = EligibilityCalculatorService
  val authConnector: ApplicationAuthConnector
  val auditConnector: AuditConnector

  def calculator() = TamcAuthPersonalDetailsAction {
    implicit auth =>
      implicit request =>
        implicit details =>
          Future { Ok(views.html.pta.calculator(calculatorForm = calculatorForm)) }
  }

  def calculatorAction() = TamcAuthPersonalDetailsAction {
    implicit auth =>
      implicit request =>
        implicit details =>
          Future {
            calculatorForm.bindFromRequest.fold(
              formWithErrors =>
                BadRequest(views.html.pta.calculator(calculatorForm = formWithErrors)),
              calculatorInput =>
                Ok(views.html.pta.calculator(
                  calculatorForm = calculatorForm.fill(calculatorInput),
                  calculationResult = Some(eligibilityCalculatorService.calculate(calculatorInput)))))
          }
  }
}
