package pl.michalkowol.module

import pl.michalkowol.db.migration.Migration
import scaldi.Module

class MigrationModule extends Module {
  bind[Migration] toNonLazy injected[Migration]
}
