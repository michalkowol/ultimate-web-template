package pl.michalkowol.module

import pl.michalkowol.play.filter.{RequestTimeLoggingFilter, MDCFilter}
import scaldi.Module

class FiltersModule extends Module {
  bind[MDCFilter] to injected[MDCFilter]
  bind[RequestTimeLoggingFilter] to injected[RequestTimeLoggingFilter]
}
