package pl.michalkowol.module

import pl.michalkowol.controller.{Blog, Weather, People}
import scaldi.Module

class ControllersModule extends Module {
  bind[People] to injected[People]
  bind[Weather] to injected[Weather]
  bind[Blog] to injected[Blog]
}
