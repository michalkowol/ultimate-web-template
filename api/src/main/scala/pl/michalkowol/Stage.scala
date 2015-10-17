package pl.michalkowol

import com.typesafe.config.Config

class Stage(config: Config) {

  private val stage: String = config.getString("pl.michalkowol.stage")

  def inLocalStage: Boolean = stage == "local"
  def inDevStage: Boolean = stage == "dev"
  def inQaStage: Boolean = stage == "qa"
  def inProdStage: Boolean = stage == "prod"
}
