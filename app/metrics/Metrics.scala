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

package metrics

import com.codahale.metrics.Timer
import com.codahale.metrics.Timer.Context
import com.kenshoo.play.metrics.MetricsRegistry

trait Metrics {
  def incrementSuccessCitizenDetail(): Unit
  def incrementFailedCitizenDetail(): Unit
  def citizenDetailStartTimer(): Timer.Context
}

object Metrics extends Metrics {

  override def incrementSuccessCitizenDetail(): Unit = MetricsRegistry.defaultRegistry.counter("citizen-detail-success").inc()

  override def incrementFailedCitizenDetail(): Unit = MetricsRegistry.defaultRegistry.counter("citizen-detail-failed").inc()

  override def citizenDetailStartTimer(): Context = MetricsRegistry.defaultRegistry.timer("citizen-detail-response-timer").time()

}
