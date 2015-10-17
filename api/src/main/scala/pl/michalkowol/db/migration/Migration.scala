package pl.michalkowol.db.migration

import java.io.File

import org.flywaydb.core.Flyway
import pl.michalkowol.Stage
import pl.michalkowol.module.DataSourceProvider
import play.api.Logger

import scala.io.Source
import scala.util.control.NonFatal

class Migration(dataSourceProvider: DataSourceProvider, stage: Stage) {

  private val dataSource = dataSourceProvider.create
  private val flyaway = createFlyway

  def createFlyway: Flyway = {
    val flyway = new Flyway()
    flyway.setDataSource(dataSource)
    flyway
  }

  def migrate(): Unit = flyaway.migrate()
  def clean(): Unit = flyaway.clean()
  def populateWithData(): Unit = {
    Logger("DbPopulate").info("Execute data population script")
    execSql(sql)
  }

  private def sql: String = {
    val filename = Seq("db", "init.sql").mkString(File.separator)
    loadFile(filename)
  }

  private def loadFile(filename: String): String = {
    Source.fromInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)).mkString
  }

  private def execSql(sql: String): Unit = {
    val connection = dataSource.getConnection
    connection.setAutoCommit(false)
    val statement = connection.createStatement()
    try {
      statement.execute(sql)
      connection.commit()
    } catch {
      case NonFatal(e) =>
        connection.rollback()
        dataSource.close()
        throw e
    } finally {
      statement.close()
      connection.close()
    }
  }

  def init(): Unit = try {
    if (stage.inLocalStage) clean()
    migrate()
    if (stage.inLocalStage) populateWithData()
  } finally {
    dataSource.close()
  }

  init()
}
