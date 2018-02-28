// deletes jobs with a specific name

Jenkins.instance.getAllItems(AbstractProject.class).each {it ->
  if(it.fullName.contains('run-tests-')) {
    println('deleting ' + it.fullName)
    it.delete()
  }
}