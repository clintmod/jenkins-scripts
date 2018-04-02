Jenkins.instance.getItems()
  .findAll { it instanceof AbstractProject }
  .findAll { !it.getFullName().contains("exclude name") }
  .each {
    it.disable()
    println("Disabled job: [$it.fullName]")
  }
