package pl.michalkowol.module

import java.io.Closeable
import javax.sql.DataSource

import com.typesafe.config.{ConfigFactory, Config}
import com.zaxxer.hikari.HikariDataSource
import pl.michalkowol.Stage
import scaldi.Module

class UtilsModule extends Module {
  bind[Config] to ConfigFactory.load
  bind[DataSourceProvider] to injected[DataSourceProvider]
  bind[DataSource] to new DataSourceProvider(config = inject[Config]).create destroyWith (_.close())
  bind[Stage] to injected[Stage]
}

class DataSourceProvider(config: Config) {
  def create: DataSource with Closeable = {
    Class.forName(config.getString("db.default.driver"))
    val ds = new HikariDataSource()
    ds.setJdbcUrl(config.getString("db.default.url"))
    ds.setUsername(config.getString("db.default.username"))
    ds.setPassword(config.getString("db.default.password"))
    ds
  }
}
