import jenkins.model.Jenkins
import hudson.model.AbstractProject

def num_jobs = build.getEnvironment(listener).get("NUM_JOBS")

//update the global env property 
instance = Jenkins.getInstance()
globalNodeProperties = instance.getGlobalNodeProperties()
envVarsNodePropertyList = globalNodeProperties.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)
envVars = envVarsNodePropertyList.get(0).getEnvVars()
envVars.put("NUM_RUN_TESTS_JOBS", num_jobs)
instance.save()

base_name = "rb-unit-tests-"

// delete the jobs
instance.getAllItems(AbstractProject.class).each {it ->
  if(it.fullName.contains(base_name)) {
    println('deleting ' + it.fullName)
    it.delete()
  }
}
//create the jobs
template_name = "batch-test-template"

(1..num_jobs.toInteger()).each {
  copied_name = base_name + it
  println('copying ' + template_name + ' to ' + copied_name)
  try {
    def job = instance.copy(instance.getItem(template_name), copied_name);
    job.save()
  } catch (IllegalArgumentException e) {}
}