package pl.michalkowol.play

import pl.michalkowol.play.filter.{RequestTimeLoggingFilter, MDCFilter}
import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter
import play.filters.cors.CORSFilter

class Filters(corsFilter: CORSFilter, mdcFilter: MDCFilter, requestTimeLoggingFilter: RequestTimeLoggingFilter) extends HttpFilters {
  override def filters: Seq[EssentialFilter] = Seq(corsFilter, mdcFilter, requestTimeLoggingFilter)
}