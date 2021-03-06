assemblyMergeStrategy in assembly := {
  case PathList("org", "apache", "commons", "logging", xs @ _*) => MergeStrategy.first
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

test in assembly := {}
//unmanagedResourceDirectories in Compile += baseDirectory.value / "../ui/dist/"