import jenkins.model.Jenkins

println("====== Slave Executors ======")
println()

// Jenkins Master and slaves
def regularSlaves = Jenkins.instance.computers.grep{
  it.online && it.displayName != "master"
}
int regularSlaveExecutorCount = regularSlaves.inject(0, {a, c -> a + c.numExecutors})

println("| Node Name\t| Type\t\t| Executors\t|")
regularSlaves.each {
 println "| ${it.displayName}\t\t| ${it.class.simpleName}\t| ${it.numExecutors}\t\t|" 
}
println()

println("Total: " + regularSlaveExecutorCount + " executors on " + regularSlaves.size() + " slaves.")
