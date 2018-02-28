//creates a bunch of jobs from a template job

template_name = "batch-test-template"
base_name = "run-tests-"
jenkins = jenkins.model.Jenkins.instance;
(1..100).each {
  copied_name = base_name + it
  println('copying ' + template_name + ' to ' + copied_name)
  try {
    def job = jenkins.copy(jenkins.getItem(template_name), copied_name);
    job.save()
  } catch (IllegalArgumentException e) {}
}
